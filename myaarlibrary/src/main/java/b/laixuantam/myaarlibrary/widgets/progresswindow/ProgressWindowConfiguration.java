package b.laixuantam.myaarlibrary.widgets.progresswindow;

import android.support.annotation.ColorRes;

public class ProgressWindowConfiguration {

    public @ColorRes
    int backgroundColor, progressColor, titleColor;

    public String title;

    public TYPE type;

    public ProgressWindowConfiguration() {

    }

    public enum TYPE {
        ClassicSpinner,
        FishSpinner,
        LineSpinner,
        None
    }

}
