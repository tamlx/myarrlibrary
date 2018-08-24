package b.laixuantam.myaarlibrary.widgets.calendar.listener;


import b.laixuantam.myaarlibrary.widgets.calendar.entity.Date;

public interface OnMonthChangedListener {

    /**
     * Call back when current showing month changed.
     *
     * @param date showing date
     */
    void onMonthChanged(Date date);

}
