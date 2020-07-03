package b.laixuantam.myaarlibrary.widgets.calendardaterangepicker.models;

import android.view.View;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import b.laixuantam.myaarlibrary.widgets.calendardaterangepicker.customviews.CustomTextView;

public class DayContainer {

    public RelativeLayout rootView;
    public CustomTextView tvDate;
    public View strip;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public DayContainer(final RelativeLayout rootView) {
        this.rootView = rootView;
        strip = rootView.getChildAt(0);
        tvDate = (CustomTextView) rootView.getChildAt(1);
    }

    public static int GetContainerKey(final Calendar cal) {
        final String str = simpleDateFormat.format(cal.getTime());
        return Integer.valueOf(str);
    }
}
