package b.laixuantam.myaarlibrary.widgets.superadapter;

import android.support.v7.widget.RecyclerView;

import b.laixuantam.myaarlibrary.widgets.superadapter.animation.BaseAnimation;

interface IAnimation {

    void enableLoadAnimation();

    void enableLoadAnimation(long duration, BaseAnimation animation);

    void cancelLoadAnimation();

    void setOnlyOnce(boolean onlyOnce);

    void addLoadAnimation(RecyclerView.ViewHolder holder);

}
