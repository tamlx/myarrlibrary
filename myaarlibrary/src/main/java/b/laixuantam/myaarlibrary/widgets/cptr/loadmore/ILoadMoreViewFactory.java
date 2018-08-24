package b.laixuantam.myaarlibrary.widgets.cptr.loadmore;

import android.view.View;
import android.view.View.OnClickListener;

public interface ILoadMoreViewFactory {

    public ILoadMoreView madeLoadMoreView();

    public interface ILoadMoreView {

        public void init(FootViewAdder footViewHolder, OnClickListener onClickLoadMoreListener);

        public void showNormal();

        public void showNomore();

        public void showLoading();

        public void showFail(Exception e);

        public void setFooterVisibility(boolean isVisible);

    }

    public static interface FootViewAdder {

        public View addFootView(View view);

        public View addFootView(int layoutId);

    }

}
