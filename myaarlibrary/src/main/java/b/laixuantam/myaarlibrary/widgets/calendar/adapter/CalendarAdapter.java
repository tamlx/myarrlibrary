package b.laixuantam.myaarlibrary.widgets.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.widgets.calendar.MonthView;
import b.laixuantam.myaarlibrary.widgets.calendar.annotation.Month;
import b.laixuantam.myaarlibrary.widgets.calendar.entity.Date;
import b.laixuantam.myaarlibrary.widgets.calendar.listener.OnDateClickedListener;
import b.laixuantam.myaarlibrary.widgets.calendar.listener.OnVagueDataSetChangeListener;
import b.laixuantam.myaarlibrary.widgets.calendar.util.DateUtil;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder>
{

    private final Context context;

    private int dateDividerColor;
    private float dateDividerSize;
    private boolean showOverflowDate = true;

    private VagueAdapter vagueAdapter;
    private OnDateClickedListener onDateClickedListener;

    private List<Date> dateList;

    /**
     * initialize adapter by {@link Date}
     *
     * @param context Activity context
     * @param date    first display date
     */
    public CalendarAdapter(Context context, Date date)
    {
        this.context = context;

        dateList = new ArrayList<>();
        //        dateList.add(DateUtil.lastMonth(date));
        dateList.add(date);
        //        dateList.add(DateUtil.nextMonth(date));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MonthView monthView = new MonthView(context);
        monthView.setDateDividerColor(dateDividerColor);
        monthView.setDateDividerSize(dateDividerSize);
        monthView.setVagueAdapter(vagueAdapter);
        monthView.setOnDateClickedListener(onDateClickedListener);
        monthView.setShowOverflowDate(showOverflowDate);
        return new MyViewHolder(monthView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Date date = dateList.get(0);
        ((MonthView) holder.itemView).setMonth(date.getYear(), date.getMonth());
    }

    @Override
    public int getItemCount()
    {
        return dateList.size();
    }

    public List<Date> getDateList()
    {
        return dateList;
    }

    /**
     * Set {@link VagueAdapter} to handle custom data.
     *
     * @param vagueAdapter adapter
     */
    public void setVagueAdapter(VagueAdapter vagueAdapter)
    {
        this.vagueAdapter = vagueAdapter;
        vagueAdapter.addOnVagueDataSetChangeListener(new OnVagueDataSetChangeListener()
        {
            @Override
            public void onVagueDataSetChange()
            {
                notifyDataSetChanged();
            }

            @Override
            public void onVagueDataSetChange(int year, @Month int month)
            {
                notifyItemChanged(dateList.indexOf(new Date(year, month, 1)));
            }
        });
    }

    /**
     * Set listener for date be clicked.
     *
     * @param onDateClickedListener the listener will be call when date be clicked.
     */
    public void setOnDateClickedListener(OnDateClickedListener onDateClickedListener)
    {
        this.onDateClickedListener = onDateClickedListener;
    }

    /**
     * Add last month of {@link #dateList}'s  first month.
     */
    public void addNewLastMonth()
    {
        dateList.add(0, DateUtil.lastMonth(dateList.get(0)));
        notifyItemInserted(0);
    }

    /**
     * Add next month of {@link #dateList}'s  last month.
     */
    public void addNewNextMonth()
    {
        dateList.add(DateUtil.nextMonth(dateList.get(dateList.size() - 1)));
        notifyItemInserted(dateList.size() - 1);
    }

    /**
     * Set divider color for date.
     *
     * @param dateDividerColor the color
     */
    public void setDateDividerColor(int dateDividerColor)
    {
        this.dateDividerColor = dateDividerColor;
    }

    /**
     * Set divider size for date.
     *
     * @param dateDividerSize the size
     */
    public void setDateDividerSize(float dateDividerSize)
    {
        this.dateDividerSize = dateDividerSize;
    }

    /**
     * If true, show whole calendar.
     * e.g. showing date is April, if show whole calendar, 03/30 and 05/01 will show.
     */
    public void setShowOverflowDate(boolean showOverflowDate)
    {
        this.showOverflowDate = showOverflowDate;
    }

    public boolean isShowOverflowDate()
    {
        return showOverflowDate;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        MyViewHolder(View itemView)
        {
            super(itemView);
        }

    }
}