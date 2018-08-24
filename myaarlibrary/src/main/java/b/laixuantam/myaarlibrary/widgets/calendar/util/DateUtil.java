package b.laixuantam.myaarlibrary.widgets.calendar.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import b.laixuantam.myaarlibrary.widgets.calendar.annotation.DayOfMonth;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Month;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Week;
import b.laixuantam.myaarlibrary.widgets.calendar.entity.Date;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;

public class DateUtil
{

    /**
     * Return week of current date,［0 to 6］for［SUNDAY to SATURDAY］
     */
    @Week
    public static int getWeek()
    {
        Calendar calendar = Calendar.getInstance();
        return getWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Return week of some date,［0 to 6］for［SUNDAY to SATURDAY］
     *
     * @param year       year of date
     * @param month      month of date，0 to 11
     * @param dayOfMonth day of month，1 to 31
     */
    @Week
    public static int getWeek(int year, @Month int month, @DayOfMonth int dayOfMonth)
    {
        // Calendar 的月份从 0 开始，周从 ［0~6］表示［周日~周六］
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * Return week of the first date of current date,［0 to 6］for［SUNDAY to SATURDAY］
     */
    @Week
    public static int getFirstWeekOfMonth()
    {
        Calendar calendar = Calendar.getInstance();
        return getFirstWeekOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }

    /**
     * Return week of the first date of some date,［0 to 6］for［SUNDAY to SATURDAY］
     *
     * @param year  year of date
     * @param month month of date，0 to 11
     */
    @Week
    public static int getFirstWeekOfMonth(int year, @Month int month)
    {
        return getWeek(year, month, 1);
    }

    /**
     * Return the day count of month of current date.
     */
    public static int getDayCountOfMonth()
    {
        return Calendar.getInstance().getActualMaximum(Calendar.DATE);
    }

    /**
     * Return the day count of month of some date.
     *
     * @param date some date
     */
    public static int getDayCountOfMonth(Date date)
    {
        return getDayCountOfMonth(date.getYear(), date.getMonth());
    }

    /**
     * Return the day count of month of some date.
     *
     * @param year  year of date
     * @param month month of date， 0 to 11
     */
    public static int getDayCountOfMonth(int year, @Month int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * Return the last month of some month of date.
     *
     * @param date some date
     */
    public static Date lastMonth(Date date)
    {
        Date lastMonth = new Date();

        int intLastMonth = date.getMonth() - 1;
        if (intLastMonth < JANUARY)
        {
            lastMonth.setMonth(DECEMBER);
            lastMonth.setYear(date.getYear() - 1);
        }
        else
        {
            lastMonth.setMonth(intLastMonth);
            lastMonth.setYear(date.getYear());
        }
        return lastMonth;
    }

    /**
     * Return current date that 1 as day of month.
     */
    public static Date currentMonth()
    {
        Calendar calendar = Calendar.getInstance();
        return new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
    }

    /**
     * Return next month that 1 as day of month of some date.
     *
     * @param date some date
     */
    public static Date nextMonth(Date date)
    {
        Date nextMonth = new Date();

        int intNextMonth = date.getMonth() + 1;
        if (intNextMonth > DECEMBER)
        {
            nextMonth.setMonth(JANUARY);
            nextMonth.setYear(date.getYear() + 1);
        }
        else
        {
            nextMonth.setMonth(intNextMonth);
            nextMonth.setYear(date.getYear());
        }
        return nextMonth;
    }

    /**
     * Return today.
     */
    public static Date today()
    {
        Calendar calendar = Calendar.getInstance();
        return new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Format date imitate the template.
     *
     * @param calendar date for format
     * @param template template，e.g. "yyyyMMdd" or "yyyy-MM-dd"
     */
    public static String formatDate(Calendar calendar, String template)
    {
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        return format.format(new java.util.Date(calendar.getTimeInMillis()));
    }

    /**
     * Format date imitate the template.
     *
     * @param year       year of date
     * @param month      month of date
     * @param dayOfMonth day of month
     * @param template   template，e.g. "yyyyMMdd" or "yyyy-MM-dd"
     */
    public static String formatDate(int year, int month, int dayOfMonth, String template)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.US);
        return format.format(new java.util.Date(calendar.getTimeInMillis()));
    }

    /**
     * Return true Some date is future date（future of month of current date）
     *
     * @param date some date
     */
    public static boolean isFutureMonth(Date date)
    {
        Date curMonth = currentMonth();
        return
                // future year of current year of date
                date.getYear() > curMonth.getYear() ||
                        // future month of current month of current year
                        date.getYear() == curMonth.getYear() && date.getMonth() > curMonth.getMonth();
    }

    public static Date getMonthSelected(String dateString)
    {
        Calendar calendar = Calendar.getInstance();

        String[] monthAndYear = dateString.split("-");

        String year = monthAndYear[0];

        String month = monthAndYear[1];

        return new Date(Integer.valueOf(year), Integer.valueOf(month), 1);
    }

}