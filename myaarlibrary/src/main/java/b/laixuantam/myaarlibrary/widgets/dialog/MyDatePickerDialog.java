package b.laixuantam.myaarlibrary.widgets.dialog;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by laixuantam on 1/13/18.
 */

public class MyDatePickerDialog
{
    public interface MyDatePickerDialogListener
    {
        void onDateSelected(String niceDate, String dateTimeServer);

        void onTimeSelected(String time);
    }

    private Activity activity;

    private MyDatePickerDialogListener listener;

    public MyDatePickerDialog(Activity activity, MyDatePickerDialogListener listener)
    {
        this.activity = activity;
        this.listener = listener;
    }

    public void showDatePicker()
    {
        final Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfNice = new SimpleDateFormat("dd/MM/yyyy");
                String formatedDate = sdf.format(new Date(year - 1900, monthOfYear, dayOfMonth));
                String niceDateTime = sdfNice.format(new Date(year - 1900, monthOfYear, dayOfMonth));
                listener.onDateSelected(niceDateTime, formatedDate);

            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));


        String languageToLoad = "vi";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        dpd.onConfigurationChanged(config);
        dpd.setThemeDark(false);
        dpd.vibrate(false);
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(false);
        dpd.setAccentColor(Color.parseColor("#009688"));
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    public void showTimePicker()
    {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(new OnTimeSetListener()
        {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
            {
                String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                String time = hourString + ":" + minuteString;

                listener.onTimeSelected(time);
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        String languageToLoad = "vi";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        tpd.onConfigurationChanged(config);
        tpd.setThemeDark(false);
        tpd.vibrate(false);
        tpd.dismissOnPause(true);
        tpd.setAccentColor(Color.parseColor("#009688"));
        tpd.show(activity.getFragmentManager(), "ScheduleTime");
    }

}
