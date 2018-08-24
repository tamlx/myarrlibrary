package b.laixuantam.myaarlibrary.widgets.calendar.entity;

import b.laixuantam.myaarlibrary.widgets.calendar.annotation.DayOfMonth;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Month;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Week;
import b.laixuantam.myaarlibrary.widgets.calendar.util.DateUtil;

public class Date {

    private int year = 2017;
    @Month
    private int month = Month.JANUARY; // ［0 to 11］for［January to DECEMBER］
    @DayOfMonth
    private int dayOfMonth = 1;
    @Week
    private int week = Week.SUNDAY; // ［0 to 6］for［SUNDAY to SATURDAY］

    public Date() {
    }

    public Date(int year, @Month int month, @DayOfMonth int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Month
    public int getMonth() {
        return month;
    }

    public void setMonth(@Month int month) {
        this.month = month;
    }

    @DayOfMonth
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(@DayOfMonth int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * ［0 to 6］for［SUNDAY to SATURDAY］
     */
    @Week
    public int getWeek() {
        if (week >= 0) {
            return week;
        } else {
            week = DateUtil.getWeek(year, month, dayOfMonth);
            return week;
        }
    }

    public void setWeek(@Week int week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Date date = (Date) object;

        if (year != date.year) return false;
        if (month != date.month) return false;
        return dayOfMonth == date.dayOfMonth;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + dayOfMonth;
        return result;
    }

    /**
     * Compare a date with this date.
     *
     * @param date Date for compare
     */
    public boolean equalsMonthOfYear(Date date) {
        return equalsMonthOfYear(date.getYear(), date.getMonth());
    }

    /**
     * Compare year and month with this date.
     *
     * @param year  year for compare
     * @param month month for compare，0 to 11
     */
    public boolean equalsMonthOfYear(int year, @Month int month) {
        return this.year == year && this.month == month;
    }

}
