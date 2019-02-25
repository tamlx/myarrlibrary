package b.laixuantam.myaarlibrary.widgets.tutorial;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

public class TutorialModel {

    /**
     * TutorialModel(R.id.button_menu, R.string.tutorial_menu, R.layout.view_tutorial_main_menu)
     * TutorialModel(R.id.button_notify, R.string.tutorial_notification, R.layout.view_tutorial_notification, true)
     * TutorialModel(R.id.text_price, R.string.tutorial_product_price_quantity, R.layout.view_tutorial_product_price_quantity, false, true)
     */

    private int componentAttached;
    private int content;
    private int fakeView;
    private boolean rlt;
    private boolean arrowBottom;
    private boolean actionBar;
    private int marginBottom;

    public TutorialModel(@IdRes int componentAttached, @StringRes int content, @LayoutRes int fakeView) {
        this.componentAttached = componentAttached;
        this.content = content;
        this.fakeView = fakeView;
        this.rlt = false;
    }


    public TutorialModel(@IdRes int componentAttached, @StringRes int content, @LayoutRes int fakeView, boolean rlt) {
        this.componentAttached = componentAttached;
        this.content = content;
        this.fakeView = fakeView;
        this.rlt = rlt;
    }

    public TutorialModel(@IdRes int componentAttached, @StringRes int content, @LayoutRes int fakeView, boolean rlt, boolean actionBar) {
        this.componentAttached = componentAttached;
        this.content = content;
        this.fakeView = fakeView;
        this.rlt = rlt;
        this.actionBar = actionBar;
    }


    public int getComponentAttached() {
        return componentAttached;
    }

    public int getContent() {
        return content;
    }

    public int getFakeView() {
        return fakeView;
    }

    public boolean isRlt() {
        return rlt;
    }

    public boolean isArrowBottom() {
        return arrowBottom;
    }

    public void setArrowBottom(boolean arrowBottom) {
        this.arrowBottom = arrowBottom;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public boolean isActionBar() {
        return actionBar;
    }

}
