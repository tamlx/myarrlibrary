package b.laixuantam.myaarlibrary.widgets.ultils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.helper.NumericFormater;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Month;

/**
 * Created by LaiXuanTam on 3/23/2016.
 */
public class ConvertDate {

    public static Date getTimeMoment() {
        Calendar calendar = Calendar.getInstance();
        return new Date(calendar.getTimeInMillis());
    }

    public static String getDateFromTimestamp(long time) {
        try {
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static Date getDateFromString(String dateString, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateFromTimestamp(long time, String datetype) {
        try {
            DateFormat sdf = new SimpleDateFormat(datetype);
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static long getTimestampFormDate(String dateString) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Date getTimeMoment(Date time) {
        return new Date(time.getTime());
    }

    public static String getTimeDuration(Date time) {
        Date timeCurrent = ConvertDate.getTimeMoment();
        // calc duration for second
        double duration = Math.max(timeCurrent.getTime() - time.getTime(), 0) / 60000d;
        long h = (long) Math.floor(duration / 60);
        long m = (long) Math.floor(duration - h * 60);
        return (h > 0 ? h + "h " : "") + m + "p";
    }

    public static String getTimeDuration(Date timePayment, Date timeOpenTable) {
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

    public static String changeToNiceFormatDate(String dateString) {

        return convertDateInput(dateString, FORMAT_NICE_DATE);
    }

    public static long getTimestampFormDateAndTime(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static String convertDateTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy");
        outputDate.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Date date = null;
        String output = "";

        try {
            date = dateFormat.parse(time);
            output = outputDate.format(date);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return output;
    }

    public static String convertDateTime(Date date) {
        DateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return outputDate.format(date);
    }

    public static String convertTime(Date date) {
        DateFormat outputDate = new SimpleDateFormat("HH:mm");
        return outputDate.format(date);
    }

    public static String convertDate(Date date) {
        DateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy");
        return outputDate.format(date);
    }

    public static String convertHourTime(String time) {
        long ts = System.currentTimeMillis();
        Date localTime = new Date(ts);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmtTime = null;
        try {
            gmtTime = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convert UTC to Local Time
        SimpleDateFormat localFormat = new SimpleDateFormat("HH:mm");
        localFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        String output = localFormat.format(gmtTime);

        return output;
    }


    public static String formatDate(int year, @Month int month, int dayOfMonth, String template) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.getDefault());
        return format.format(new Date(calendar.getTimeInMillis()));
    }

    public static String getDistanceTime(long time) {

        if (time > 0) {

            try {
                Date date = new Date(time);

                Date current = getTimeMoment();

                long different = current.getTime() - date.getTime();

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = different / daysInMilli - 1;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;

                if (elapsedDays > 0) {
                    return "" + elapsedDays + " ngày trước";
                } else {
                    return ConvertDate.formatHourTime(time);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        MyLog.e("DateOutput", "startDate : " + startDate);
        MyLog.e("DateOutput", "endDate : " + endDate);
        MyLog.e("DateOutput", "different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public static boolean checkDateIncomming(String dateInput) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            Date date1 = simpleDateFormat.parse(dateInput);
            Date date2 = ConvertDate.getTimeMoment();

            long different = date1.getTime() - date2.getTime();


            if (different > 0) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String formatHourTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public static int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
