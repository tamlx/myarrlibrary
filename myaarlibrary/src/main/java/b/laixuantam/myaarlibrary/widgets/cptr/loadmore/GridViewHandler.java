package b.laixuantam.myaarlibrary.widgets.cptr.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

public class GridViewHandler implements LoadMoreHandler {

    private GridViewWithHeaderAndFooter mGridView;
    private View mFooter;

    @Override
    public boolean handleSetAdapter(View contentView, ILoadMoreViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        mGridView = (GridViewWithHeaderAndFooter) contentView;
        ListAdapter adapter = mGridView.getAdapter();
        boolean hasInit = false;
        if (loadMoreView != null) {
            final Context context = mGridView.getContext().getApplicationContext();
            loadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() {

                @Override
                public View addFootView(int layoutId) {
                    View view = LayoutInflater.from(context).inflate(layoutId, mGridView, false);
                    mFooter = view;
                    return addFootView(view);
                }

                @Override
                public View addFootView(View view) {
                    mGridView.addFooterView(view);
                    return view;
                }
            }, onClickLoadMoreListener);
            hasInit = true;
            if (null != adapter) {
                mGridView.setAdapter(adapter);
            }
        }
        return hasInit;
    }

    @Override
    public void addFooter() {
        if (mGridView.getFooterViewCount() <= 0 && null != mFooter) {
            mGridView.addFooterView(mFooter);
        }
    }

    @Override
    public void removeFooter() {
        if (mGridView.getFooterViewCount() > 0 && null != mFooter) {
            mGridView.removeFooterView(mFooter);
        }
    }

    @Override
    public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener) {
        GridViewWithHeaderAndFooter gridView = (GridViewWithHeaderAndFooter) contentView;
        gridView.setOnScrollListener(new GridViewOnScrollListener(onScrollBottomListener));
        gridView.setOnItemSelectedListener(new GridViewOnItemSelectedListener(onScrollBottomListener));
    }

    private class GridViewOnItemSelectedListener implements OnItemSelectedListener {
        private OnScrollBottomListener onScrollBottomListener;

        public GridViewOnItemSelectedListener(OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (adapterView.getLastVisiblePosition() + 1 == adapterView.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScorllBootom();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static class GridViewOnScrollListener implements OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;

        public GridViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition() + 1 == view.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScorllBootom();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}
