package b.laixuantam.myaarlibrary.widgets.calendardaterangepicker.customviews;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.calendardaterangepicker.models.CalendarStyleAttributes;


class AdapterEventCalendarMonths extends PagerAdapter {

    private final Context mContext;
    private final List<Calendar> dataList;
    private final CalendarStyleAttributes calendarStyleAttr;
    private CalendarListener calendarListener;
    private final DateRangeCalendarManager dateRangeCalendarManager;
    private final Handler mHandler;

    AdapterEventCalendarMonths(@NonNull final Context mContext, @NonNull final List<Calendar> list,
                               @NonNull final CalendarStyleAttributes calendarStyleAttr) {
        this.mContext = mContext;
        dataList = list;
        this.calendarStyleAttr = calendarStyleAttr;
        // Get start month and set start date of that month
        final Calendar startSelectableDate = (Calendar) list.get(0).clone();
        startSelectableDate.set(Calendar.DAY_OF_MONTH, 1);
        // Get end month and set end date of that month
        final Calendar endSelectableDate = (Calendar) list.get(list.size() - 1).clone();
        endSelectableDate.set(Calendar.DAY_OF_MONTH, endSelectableDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        dateRangeCalendarManager = new DateRangeCalendarManager(startSelectableDate, endSelectableDate);
        mHandler = new Handler();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        final Calendar modelObject = dataList.get(position);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_pager_month, container, false);

        final DateRangeMonthView dateRangeMonthView = layout.findViewById(R.id.cvEventCalendarView);
        dateRangeMonthView.drawCalendarForMonth(calendarStyleAttr, getCurrentMonth(modelObject), dateRangeCalendarManager);
        dateRangeMonthView.setCalendarListener(calendarAdapterListener);

        container.addView(layout);
        return layout;
    }

    /**
     * To clone calendar obj and get current month calendar starting from 1st date.
     *
     * @param calendar - Calendar
     * @return - Modified calendar obj of month of 1st date.
     */
    private Calendar getCurrentMonth(final @NonNull Calendar calendar) {
        final Calendar current = (Calendar) calendar.clone();
        current.set(Calendar.DAY_OF_MONTH, 1);
        return current;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup collection, final int position, @NonNull final Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getItemPosition(@NonNull final Object object) {
        return POSITION_NONE;
    }


    private final CalendarListener calendarAdapterListener = new CalendarListener() {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 50);

            if (calendarListener != null) {
                calendarListener.onFirstDateSelected(startDate);
            }
        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 50);
            if (calendarListener != null) {
                calendarListener.onDateRangeSelected(startDate, endDate);
            }
        }
    };

    void setCalendarListener(final CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    /**
     * To redraw calendar.
     */
    void invalidateCalendar() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 50);
    }

    /**
     * To remove all selection and redraw current calendar
     */
    void resetAllSelectedViews() {
        dateRangeCalendarManager.setMinSelectedDate(null);
        dateRangeCalendarManager.setMaxSelectedDate(null);
        notifyDataSetChanged();
    }

    void setSelectedDate(final Calendar minSelectedDate, final Calendar maxSelectedDate) {
        dateRangeCalendarManager.setMinSelectedDate(minSelectedDate);
        dateRangeCalendarManager.setMaxSelectedDate(maxSelectedDate);
        notifyDataSetChanged();
    }

    Calendar getMinSelectedDate() {
        return dateRangeCalendarManager.getMinSelectedDate();
    }

    Calendar getMaxSelectedDate() {
        return dateRangeCalendarManager.getMaxSelectedDate();
    }

    /**
     * To set editable mode. Set true if you want user to select date range else false. Default value will be true.
     */
    void setEditable(final boolean isEditable) {
        calendarStyleAttr.setEditable(isEditable);
        notifyDataSetChanged();
    }

    /**
     * To get editable mode.
     */
    boolean isEditable() {
        return calendarStyleAttr.isEditable();
    }

    void setSelectableDateRange(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
        dateRangeCalendarManager.setSelectableDateRange(startDate, endDate);
        notifyDataSetChanged();
    }
}
