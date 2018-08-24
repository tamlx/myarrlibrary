package b.laixuantam.myaarlibrary.widgets.calendar.listener;

import android.view.View;

import b.laixuantam.myaarlibrary.widgets.calendar.annotation.DayOfMonth;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Month;

public interface OnDateClickedListener {

    /**
     * Call back when day of month be clicked.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［January to DECEMBER］
     * @param dayOfMonth day of month
     */
    void onDateClicked(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth);

}
