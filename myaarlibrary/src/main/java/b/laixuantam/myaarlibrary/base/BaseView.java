package b.laixuantam.myaarlibrary.base;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import b.laixuantam.myaarlibrary.widgets.scaletouchlistener.ScaleTouchListener;

public abstract class BaseView<C extends BaseUiContainer> implements BaseViewInterface {
    private View view;
    protected C ui;
    protected Handler handler = new Handler();
    protected ScaleTouchListener.Config confScaleTouch = new ScaleTouchListener.Config(100, 1f, 0.5f);
    private Context context;

    @Override
    @SuppressWarnings("unchecked")
    public final View inflate(LayoutInflater inflater, ViewGroup container) {
        this.context = inflater.getContext();
        this.view = inflater.inflate(getViewId(), container, false);

        if (container == null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            this.view.setLayoutParams(params);
        }

        this.ui = (C) getUiContainer();

        if (this.ui != null) {
            this.ui.bind(view);
        }

        return this.view;
    }

    @Override
    public String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    @Override
    public String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    @Override
    public int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    @Override
    public LayoutInflater getInflater() {
        return LayoutInflater.from(getContext());
    }

    @Override
    public View findViewById(@IdRes int id) {
        return this.view.findViewById(id);
    }

    @Override
    public void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void setInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setGone(View view) {
        view.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public View getView() {
        return this.view;
    }
}
