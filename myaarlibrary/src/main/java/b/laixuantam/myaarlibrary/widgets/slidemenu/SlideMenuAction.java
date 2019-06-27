package b.laixuantam.myaarlibrary.widgets.slidemenu;

import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface SlideMenuAction {

    int SLIDE_MODE_LEFT = 1001;
    int SLIDE_MODE_RIGHT = 1002;
    int SLIDE_MODE_LEFT_RIGHT = 1003;
    int SLIDE_MODE_NONE = 1004;

    void setSlideMode(@SlideMode int slideMode);

    void setSlidePadding(int slidePadding);

    void setSlideTime(int slideTime);

    void setParallaxSwitch(boolean parallax);

    void setContentAlpha(@FloatRange(from = 0f, to = 1.0f) float contentAlpha);

    void setContentShadowColor(@ColorRes int color);

    void setContentToggle(boolean contentToggle);

    void setAllowTogging(boolean allowTogging);

    View getSlideLeftView();

    View getSlideRightView();

    View getSlideContentView();

    void toggleLeftSlide();

    void openLeftSlide();

    void closeLeftSlide();

    boolean isLeftSlideOpen();

    void toggleRightSlide();

    void openRightSlide();

    void closeRightSlide();

    boolean isRightSlideOpen();

    void addOnSlideChangedListener(OnSlideChangedListener listener);

    @IntDef({SLIDE_MODE_LEFT, SLIDE_MODE_RIGHT, SLIDE_MODE_LEFT_RIGHT, SLIDE_MODE_NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SlideMode {

    }
}
