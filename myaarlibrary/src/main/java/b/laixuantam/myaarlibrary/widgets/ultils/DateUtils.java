package b.laixuantam.myaarlibrary.widgets.ultils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

import b.laixuantam.myaarlibrary.R;

//https://github.com/votingsystem/votingsystem-android-client
public class DateUtils {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZZ");
    private static final DateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZ");
    private static final DateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZ");

    private static final DateFormat isoDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

    static {
        isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String getTimeAgo(long time, Context ctx) {
        // TODO: use DateUtils methods instead
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now = Calendar.getInstance().getTimeInMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE) {
            return ctx.getString(R.string.title_just_now);
        } else if (diff < 2 * MINUTE) {
            return ctx.getString(R.string.title_a_minute_ago);
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + ctx.getString(R.string.title_minutes_ago);
        } else if (diff < 90 * MINUTE) {
            return ctx.getString(R.string.title_an_hour_ago);
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + ctx.getString(R.string.title_an_hours_ago);
        } else if (diff < 48 * HOUR) {
            return ctx.getString(R.string.day_title_yesterday);
        } else {
            return diff / DAY + ctx.getString(R.string.title_an_days_ago);
        }
    }

    private static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US)
    };


    public static String formatShortDate(Context context, Date date) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        return android.text.format.DateUtils.formatDateRange(context, formatter, date.getTime(), date.getTime(),
                android.text.format.DateUtils.FORMAT_ABBREV_ALL | android.text.format.DateUtils.FORMAT_NO_YEAR,
                getDisplayTimeZone().getID()).toString();
    }

    public static String formatShortTime(Context context, Date time) {
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        TimeZone tz = getDisplayTimeZone();
        if (tz != null) {
            format.setTimeZone(tz);
        }
        return format.format(time);
    }

    /**
     * Returns "Today", "Tomorrow", "Yesterday", or a short date format.
     */
    public static String formatHumanFriendlyShortDate(final Context context, long timestamp) {
        long localTimestamp, localTime;
        long now = Calendar.getInstance().getTimeInMillis();

        TimeZone tz = getDisplayTimeZone();
        localTimestamp = timestamp + tz.getOffset(timestamp);
        localTime = now + tz.getOffset(now);

        long dayOrd = localTimestamp / 86400000L;
        long nowOrd = localTime / 86400000L;

        if (dayOrd == nowOrd) {
            return context.getString(R.string.day_title_today);
        } else if (dayOrd == nowOrd - 1) {
            return context.getString(R.string.day_title_yesterday);
        } else if (dayOrd == nowOrd + 1) {
            return context.getString(R.string.day_title_tomorrow);
        } else {
            return formatShortDate(context, new Date(timestamp));
        }
    }

    public static boolean inRange(Date initDate, Date lapsedDate, long timeRange) {
        return initDate.getTime() - lapsedDate.getTime() < timeRange;
    }

    public static int getDayOfMonthFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonthFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Date getDate(String dateStr) {
        try {
            if (dateStr.endsWith("Z")) return isoDateFormat.parse(dateStr);
            else return dateFormat.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Date getXmlDate(String dateStr) {
        try {
            return xmlDateFormat.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getXmlDateStr(Date date) {
        try {
            return xmlDateFormat.format(date).replace("GMT", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getUTCDateStr(Date date) {
        try {
            return shortDateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Date getDate(String dateStr, String format) throws ParseException {
        try {
            DateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDateFromTimestamp(long time, String format) {
        try {
            DateFormat sdf = new SimpleDateFormat(format);
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static long getTimestampFormDate(String dateString, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getISODateStr(Date date) {
        return isoDateFormat.format(date);
    }

    public static String getDateStr(Date date) {
        return dateFormat.format(date);
    }

    public static String getDateStr(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String getElapsedTimeHoursMinutesFromMilliseconds(long milliseconds) {
        String format = String.format("%%0%dd", 2);
        long elapsedTime = milliseconds / 1000;
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);
        String time = hours + ":" + minutes;
        return time;
    }

    public static String getElapsedTimeHoursMinutesSecondsFromMilliseconds(long milliseconds) {
        String format = String.format("%%0%dd", 2);
        long elapsedTime = milliseconds / 1000;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);
        String time = hours + ":" + minutes + ":" + seconds;
        return time;
    }

    public static Calendar addDays(int numDias) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, numDias);
        return today;
    }

    public static Calendar addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal;
    }

    public static String getDayHourElapsedTime(Date date1, Date date2, Context context) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return getDayHourElapsedTime(cal1, cal2, context);
    }

    public static String getDayWeekDateStr(Date date, String hourFormat) {
        if (hourFormat == null) hourFormat = "HH:mm";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (Calendar.getInstance().get(Calendar.YEAR) != calendar.get(Calendar.YEAR))
            return getDateStr(date, "dd MMM yyyy' '" + hourFormat);
        else return getDateStr(date, "EEE dd MMM' '" + hourFormat);
    }

    public static String getDayWeekDateSimpleStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (Calendar.getInstance().get(Calendar.YEAR) != calendar.get(Calendar.YEAR))
            return getDateStr(date, "dd MMM yyyy");
        else return getDateStr(date, "EEE dd MMM");
    }

    public static Date getDayWeekDate(String dateStr) throws ParseException {
        try {
            return getDate(dateStr, "dd MMM yyyy' 'HH:mm");
        } catch (Exception ex) {
            Calendar resultCalendar = Calendar.getInstance();
            resultCalendar.setTime(getDate(dateStr, "EEE dd MMM' 'HH:mm"));
            resultCalendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            return resultCalendar.getTime();
        }
    }

    public static String getElapsedTimeStr(Date end) {
        Float hours = (end.getTime() - Calendar.getInstance().getTime().getTime()) / (60 * 60 * 1000F);
        return Integer.valueOf(hours.intValue()).toString();
    }

    public static String getYearDayHourMinuteSecondElapsedTime(Calendar cal1, Calendar cal2,
                                                               Context context) {
        long l1 = cal1.getTimeInMillis();
        long l2 = cal2.getTimeInMillis();
        long diff = l2 - l1;

        long secondInMillis = 1000;
        long minuteInMillis = secondInMillis * 60;
        long hourInMillis = minuteInMillis * 60;
        long dayInMillis = hourInMillis * 24;
        long yearInMillis = dayInMillis * 365;

        long elapsedYears = diff / yearInMillis;
        diff = diff % yearInMillis;
        long elapsedDays = diff / dayInMillis;
        diff = diff % dayInMillis;
        long elapsedHours = diff / hourInMillis;
        diff = diff % hourInMillis;
        long elapsedMinutes = diff / minuteInMillis;
        diff = diff % minuteInMillis;
        long elapsedSeconds = diff / secondInMillis;

        StringBuilder result = new StringBuilder();
        if (elapsedYears > 0) result.append(elapsedYears + ", "
                + context.getString(R.string.years_lbl));
        if (elapsedDays > 0) result.append(elapsedDays + ", "
                + context.getString(R.string.days_lbl));
        if (elapsedHours > 0) result.append(elapsedHours + ", "
                + context.getString(R.string.hours_lbl));
        if (elapsedMinutes > 0) result.append(elapsedMinutes + ", "
                + context.getString(R.string.minutes_lbl));
        if (elapsedSeconds > 0) result.append(elapsedSeconds + ", "
                + context.getString(R.string.seconds_lbl));
        return result.toString();
    }

    public static String getYearDayHourMinuteSecondElapsedTime(Date date1, Date date2,
                                                               Context context) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return getYearDayHourMinuteSecondElapsedTime(cal1, cal2, context);
    }

    public static String getDayHourElapsedTime(Calendar cal1, Calendar cal2, Context context) {
        long l1 = cal1.getTimeInMillis();
        long l2 = cal2.getTimeInMillis();
        long diff = l2 - l1;

        long secondInMillis = 1000;
        long minuteInMillis = secondInMillis * 60;
        long hourInMillis = minuteInMillis * 60;
        long dayInMillis = hourInMillis * 24;
        long yearInMillis = dayInMillis * 365;

        long elapsedDays = diff / dayInMillis;
        diff = diff % dayInMillis;
        long elapsedHours = diff / hourInMillis;
        diff = diff % hourInMillis;

        StringBuilder result = new StringBuilder();
        if (elapsedDays > 0)
            result.append(elapsedDays + " " + context.getString(R.string.days_lbl));
        if (elapsedHours > 0)
            result.append(elapsedHours + ", " + context.getString(R.string.hours_lbl));
        return result.toString();
    }

    public static Calendar getMonday(Calendar calendar) {
        Calendar result = (Calendar) calendar.clone();
        result.add(Calendar.DAY_OF_YEAR, -7);
        result.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result.set(Calendar.HOUR_OF_DAY, 24);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result;
    }

    public static TimeZone getDisplayTimeZone() {
        return TimeZone.getDefault();
    }

    public static Date getTimeMoment(Date time) {
        return new Date(time.getTime());
    }

    public static String getTimeDurationFromMoment(Date time) {
        Date timeCurrent = ConvertDate.getTimeMoment();
        // calc duration for second
        double duration = Math.max(timeCurrent.getTime() - time.getTime(), 0) / 60000d;
        long h = (long) Math.floor(duration / 60);
        long m = (long) Math.floor(duration - h * 60);
        return (h > 0 ? h + "h " : "") + m + "p";
    }

    public static String getTimeDurationBetweenTwoMoment(Date timePayment, Date timeOpenTable) {
        Date timeCurrent = getTimeMoment(timePayment);
        // calc duration for second
        double duration = Math.max(timeCurrent.getTime() - timeOpenTable.getTime(), 0) / 60000d;
        long h = (long) Math.floor(duration / 60);
        long m = (long) Math.floor(duration - h * 60);
        return (h > 0 ? h + "h " : "") + m + "p";
    }

    public static final String FORMAT_GET_DAY = "dd";
    public static final String FORMAT_GET_MONTH = "MM";
    public static final String FORMAT_GET_YEAR = "yyyy";
    public static final String FORMAT_GET_MONTH_YEAR = "yyyy-MM";
    public static final String FORMAT_NICE_DATE = "dd/MM/yyyy";

    public static String getDay(String dateString) {
        return convertDateInput(dateString, FORMAT_GET_DAY);
    }

    public static String convertDateInput(String dateString, String type) {
        DateFormat dateFormatDefault = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat(type);
        Date date = null;
        String output = "";

        try {
            date = dateFormatDefault.parse(dateString);
            output = dateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    public static String getMonthAndYear(String dateString) {
        return convertDateInput(dateString, FORMAT_GET_MONTH_YEAR);
    }

    public static String getYear(String dateString) {

        return convertDateInput(dateString, FORMAT_GET_YEAR);
    }

    public static String getMonth(String dateString) {

        return convertDateInput(dateString, FORMAT_GET_MONTH);
    }

    public static String getNiceFormatDate(String dateString) {

        return convertDateInput(dateString, FORMAT_NICE_DATE);
    }

    public static int getAge(String dobString, String format) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }


        return age;
    }
}
