package b.laixuantam.myaarlibrary.widgets.dialog;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import b.laixuantam.myaarlibrary.widgets.slidedatetimepicker.SlideDateTimeListener;
import b.laixuantam.myaarlibrary.widgets.slidedatetimepicker.SlideDateTimePicker;

/**
 * Created by laixuantam on 1/13/18.
 */

public class MyDatePickerDialog {
    public interface MyDatePickerDialogListener {
        void onDateSelected(String niceDate, String dateTimeServer);

        void onTimeSelected(String time);
    }

    private Activity activity;

    private MyDatePickerDialogListener listener;

    public MyDatePickerDialog(Activity activity, MyDatePickerDialogListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void showSpinDatePicker(FragmentManager fragmentManager, SlideDateTimeListener listener) {
        new SlideDateTimePicker.Builder(fragmentManager)
                .setListener(listener)
                .setInitialDate(new Date())
                .setTypeShowDialog(1)
                .build()
                .show();
    }

    public void showDatePicker() {
        final Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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

    public void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(new OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
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

    public void showSpinTimePicker(FragmentManager fragmentManager, SlideDateTimeListener listener) {
        new SlideDateTimePicker.Builder(fragmentManager)
                .setListener(listener)
                .setIs24HourTime(true)
                .setInitialDate(new Date())
                .setTypeShowDialog(2)
                .build()
                .show();
    }

}

/**

 private String scheduleDateSelected;
 private String scheduleTimeSelected;
 private boolean isSelectDate;
 private SlideDateTimeListener listener = new SlideDateTimeListener() {

@Override
public void onDateTimeSet(Date date) {

final Calendar now = Calendar.getInstance();
long timeToday = now.getTimeInMillis();
long different = date.getTime() - timeToday;

String dateToday = ConvertDate.getDateFromTimestamp(timeToday);

if (isSelectDate) {
DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
DateFormat sdfTimeServer = new SimpleDateFormat("yyyy-MM-dd");
String niceFormatDate = sdf.format(date);

if (dateToday.equalsIgnoreCase(niceFormatDate)) {
scheduleDateSelected = sdfTimeServer.format(date);
ui.tvBookingScheduleDate.setText(niceFormatDate);
activity.getBookingCustomerInfoModel().setSchedule_date(scheduleDateSelected);
} else {
if (different < 0) {
Toast.makeText(getContext(), "Ngày hẹn phải lớn hơn ngày hiện tại.", Toast.LENGTH_LONG).show();
return;
}else{
scheduleDateSelected = sdfTimeServer.format(date);
ui.tvBookingScheduleDate.setText(niceFormatDate);
activity.getBookingCustomerInfoModel().setSchedule_date(scheduleDateSelected);
}
}

} else {

String niceFormatDate = ConvertDate.changeToNiceFormatDate(scheduleDateSelected);

if (dateToday.equalsIgnoreCase(niceFormatDate)) {
if (different < 0) {
Toast.makeText(getContext(), "Giờ hẹn phải lớn hơn giờ hiện tại.", Toast.LENGTH_LONG).show();
return;
}
}

DateFormat timeFormat = new SimpleDateFormat("HH:mm");
scheduleTimeSelected = timeFormat.format(date);
ui.tvBookingScheduleTime.setText(scheduleTimeSelected);
activity.getBookingCustomerInfoModel().setSchedule_time(scheduleTimeSelected);
}
}

// Optional cancel listener
@Override
public void onDateTimeCancel() {
}
};

 */