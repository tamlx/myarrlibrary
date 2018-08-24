package b.laixuantam.myaarlibrary.widgets.calendar.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.calendar.listener.OnVagueDataSetChangeListener;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.DayOfMonth;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Month;
import b.laixuantam.myaarlibrary.widgets.calendar.entity.Date;
import b.laixuantam.myaarlibrary.widgets.calendar.listener.OnVagueDataSetChangeListener;

public class VagueAdapter<T>
{

    private int dayLayoutRes;

    protected T data;
    private List<OnVagueDataSetChangeListener> onVagueDataSetChangeListenerList = new ArrayList<>();

    /**
     * Initialization adapter.
     *
     * @param dayLayout layout for day of month
     */
    public VagueAdapter(@LayoutRes int dayLayout)
    {
        dayLayoutRes = dayLayout;
    }

    /**
     * Return layout for day of month.
     */
    public int getDayLayoutRes()
    {
        return dayLayoutRes;
    }

    /**
     * Return {@link #data}
     */
    public T getData()
    {
        return data;
    }

    /**
     * Set the data for display.
     *
     * @param data data
     */
    public void setData(T data)
    {
        this.data = data;
    }

    /**
     * Notification adapter update data.
     */
    public void notifyDataSetChanged()
    {
        for (OnVagueDataSetChangeListener listener : onVagueDataSetChangeListenerList)
        {
            listener.onVagueDataSetChange();
        }
    }

    /**
     * Notification adapter update data.
     *
     * @param year  year of date
     * @param month month of date
     */
    public void notifyDataSetChanged(int year, @Month int month)
    {
        for (OnVagueDataSetChangeListener listener : onVagueDataSetChangeListenerList)
        {
            listener.onVagueDataSetChange(year, month);
        }
    }

    /**
     * Set listener for response {@link #data} change.
     *
     * @param listener listener for {@link #data} change
     */
    public void addOnVagueDataSetChangeListener(OnVagueDataSetChangeListener listener)
    {
        onVagueDataSetChangeListenerList.add(listener);
    }

    /**
     * Bind date.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［JANUARY to DECEMBER］
     * @param dayOfMonth day of month
     */
    public void onBindDate(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth)
    {
        try
        {
            // show day of month
            TextView view = (TextView) itemView.findViewById(R.id.tv_day_of_month);
            view.setText(String.valueOf(dayOfMonth));
            view.setTextColor(Color.parseColor("#000000"));
            //            view.setAlpha(1f);
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException("Missing ID is tv_day_of_month's TextView!");
        }
    }

    /**
     * Bind date.
     * e.g. showing date is April, param year is March, param dayOfMonth is end date of March.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［JANUARY to DECEMBER］
     * @param dayOfMonth day of month
     */
    public void onBindStartOverflowDate(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth)
    {
        try
        {
            // show day of month
            TextView view = (TextView) itemView.findViewById(R.id.tv_day_of_month);
            view.setText(String.valueOf(dayOfMonth));
            view.setAlpha(0.5f);
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException("Missing ID is tv_day_of_month's TextView!");
        }
    }

    /**
     * Bind date.
     * e.g. showing date is April, param year is May, param dayOfMonth is end date of May.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［JANUARY to DECEMBER］
     * @param dayOfMonth day of month
     */
    public void onBindEndOverflowDate(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth)
    {
        try
        {
            // show day of month
            TextView view = (TextView) itemView.findViewById(R.id.tv_day_of_month);
            view.setText(String.valueOf(dayOfMonth));
            view.setAlpha(0.5f);
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException("Missing ID is tv_day_of_month's TextView!");
        }
    }

    /**
     * Bind custom data.
     * e.g. showing date is April, param year is March, param dayOfMonth is end date of March.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［JANUARY to DECEMBER］
     * @param dayOfMonth day of month
     */
    public void onBindVague(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth)
    {

    }

    /**
     * Bind custom data.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［JANUARY to DECEMBER］
     * @param dayOfMonth day of month
     */
    public void onBindVagueOfStartOverflowDate(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth)
    {
    }

    /**
     * Bind custom data.
     * e.g. showing date is April, param year is May, param dayOfMonth is end date of May.
     *
     * @param itemView   item view for day of month
     * @param year       year of date
     * @param month      month of date，［0 to 11］for［JANUARY to DECEMBER］
     * @param dayOfMonth day of month
     */
    public void onBindVagueOfEndOverflowDate(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth)
    {
    }

    /**
     * Set a mark for today.
     *
     * @param todayView item view of today
     */
    public void flagToday(View todayView)
    {
        todayView.setBackgroundResource(R.drawable.bg_today);
    }

    public void resetHightLight(View view)
    {
        view.setBackgroundColor(Color.TRANSPARENT);

        TextView tvDayView = (TextView) view.findViewById(R.id.tv_day_of_month);
        tvDayView.setBackgroundColor(Color.TRANSPARENT);
        tvDayView.setTextColor(Color.parseColor("#4d4d4d"));
    }

    public void hightLightItemSelected(View view)
    {
        view.setBackgroundResource(R.drawable.bg_day_selected);
    }

    /**
     * Set a mark for all date except today.
     *
     * @param dayView item view for date
     * @param date    date for flag
     */
    public void flagNotToday(View dayView, Date date)
    {
        dayView.setBackgroundColor(Color.TRANSPARENT);
    }

}
