package b.laixuantam.myaarlibrary.widgets.calendardaterangepicker.models;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public interface CalendarStyleAttributes {

    Typeface getFonts();

    void setFonts(@NonNull Typeface fonts);

    int getTitleColor();

    @NonNull
    Drawable getHeaderBg();

    int getWeekColor();

    int getRangeStripColor();

    int getSelectedDateCircleColor();

    int getSelectedDateColor();

    int getDefaultDateColor();

    int getDisableDateColor();

    int getRangeDateColor();

    float getTextSizeTitle();

    float getTextSizeWeek();

    float getTextSizeDate();

    void setWeekOffset(int weekOffset);

    int getWeekOffset();

    boolean isEditable();

    void setEditable(boolean editable);

    boolean isSwipeable();

    void setSwipeable(boolean Swipeable);

    boolean isShowHeader();

    void setShowHeader(boolean show_header);
}
