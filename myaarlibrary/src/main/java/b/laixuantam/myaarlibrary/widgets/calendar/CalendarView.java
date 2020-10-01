package b.laixuantam.myaarlibrary.widgets.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.calendar.adapter.CalendarAdapter;
import b.laixuantam.myaarlibrary.widgets.calendar.adapter.VagueAdapter;
import b.laixuantam.myaarlibrary.widgets.calendar.entity.Date;
import b.laixuantam.myaarlibrary.widgets.calendar.listener.OnDateClickedListener;
import b.laixuantam.myaarlibrary.widgets.calendar.listener.OnMonthChangedListener;
import b.laixuantam.myaarlibrary.widgets.calendar.recyclerview.PageRecyclerView;
import b.laixuantam.myaarlibrary.widgets.calendar.recyclerview.SpeedScrollLinearLayoutManager;
import b.laixuantam.myaarlibrary.widgets.calendar.util.DateUtil;

//app demo booking vé máy bay
public class CalendarView extends LinearLayout
{

    private final int LANGUAGE_CHINA = 1;
    private final int LANGUAGE_ENGLISH = 2;
    private final int LANGUAGE_VI = 3;

    // params for layout
    private int titleColor;
    private int titleLayoutRes;
    private int weekColor;
    private Drawable imgLastMonth;
    private Drawable imgNextMonth;
    private Drawable weekBackground;
    private Drawable monthBackground;
    private int dividerColor;
    private float dividerSize;
    private int language = LANGUAGE_CHINA;
    private LayoutInflater layoutInflater;

    private String monthValue;

    private View vTitleLayout;
    private ImageButton ibtnLastMonth; // last month
    private ImageButton ibtnNextMonth; // next month
    private TextView tvTitle; // title
    private PageRecyclerView rcvMonth; // RecyclerView for data
    private LinearSnapHelper snapHelper;

    private CalendarAdapter calendarAdapter;
    private VagueAdapter vagueAdapter;

    private OnMonthChangedListener onMonthChangedListener;
    private boolean scaleEnable = false; // if true, the layout will wrap content.

    private int currentPosition = 0;

    private SimpleDateFormat dateFormat;
    private Date currentDate;

    /**
     * Limited value to prevent the view's visible.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @interface Visibility
    {
    }

    public CalendarView(Context context)
    {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        readAttrs(attrs);
        init();
    }

    // Read attribute for layout
    private void readAttrs(AttributeSet attrs)
    {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
        titleColor = array.getColor(R.styleable.CalendarView_titleColor, Color.BLACK);
        titleLayoutRes = array.getResourceId(R.styleable.CalendarView_titleLayout, R.layout.layout_calendar_title);
        weekColor = array.getColor(R.styleable.CalendarView_weekColor, Color.BLACK);
        imgLastMonth = array.getDrawable(R.styleable.CalendarView_imgLastMonth);
        imgNextMonth = array.getDrawable(R.styleable.CalendarView_imgNextMonth);
        weekBackground = array.getDrawable(R.styleable.CalendarView_weekBackground);
        monthBackground = array.getDrawable(R.styleable.CalendarView_monthBackground);
        dividerColor = array.getColor(R.styleable.CalendarView_dateDividerColor, Color.LTGRAY);
        dividerSize = array.getDimension(R.styleable.CalendarView_dateDividerSize, getResources().getDimension(R.dimen.dateDividerSize));
        language = array.getInt(R.styleable.CalendarView_language, LANGUAGE_CHINA);
        array.recycle();
    }

    private void init()
    {
        setOrientation(VERTICAL);

        layoutInflater = LayoutInflater.from(getContext());

        initTitleBar();

        initWeekBar();

        //        initRCVMonth();

        setTitleFormat("MM-yyyy", Locale.UK);


    }

    // initialize title bar
    private void initTitleBar()
    {

        vTitleLayout = layoutInflater.inflate(titleLayoutRes, this, true);

        // view of title
        tvTitle = (TextView) vTitleLayout.findViewById(R.id.tv_title);
        if (tvTitle != null)
        {
            tvTitle.setTextColor(titleColor);
        }

        ChangeMonthEvent changeMonthEvent = new ChangeMonthEvent();
        // view of last month
        ibtnLastMonth = (ImageButton) vTitleLayout.findViewById(R.id.btn_last_month);
        if (imgLastMonth != null)
        {
            ibtnLastMonth.setImageDrawable(imgLastMonth);
        }
        if (ibtnLastMonth != null)
        {
            ibtnLastMonth.setOnClickListener(changeMonthEvent);
        }
        // view of next month
        ibtnNextMonth = (ImageButton) vTitleLayout.findViewById(R.id.btn_next_month);
        if (imgNextMonth != null)
        {
            ibtnNextMonth.setImageDrawable(imgNextMonth);
        }
        if (ibtnNextMonth != null)
        {
            ibtnNextMonth.setOnClickListener(changeMonthEvent);
        }
    }

    // initialize week bar
    private void initWeekBar()
    {
        String[] weekArr = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        if (language == LANGUAGE_ENGLISH)
        {
            weekArr = new String[] {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        }
        else if (language == LANGUAGE_VI)
        {
            weekArr = new String[] {"CN", "Th 2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7"};
        }
        LinearLayout weekLayout = new LinearLayout(getContext());
        weekLayout.setBackgroundDrawable(weekBackground);
        weekLayout.setPadding(0, (int) getResources().getDimension(R.dimen.dp5), 0, (int) getResources().getDimension(R.dimen.dp5));
        addView(weekLayout);

        TextView tvWeek;
        for (int i = 0; i < 7; i++)
        {
            tvWeek = new TextView(getContext());
            tvWeek.setGravity(Gravity.CENTER);
            tvWeek.setText(weekArr[i]);
            tvWeek.setTextColor(weekColor);
            weekLayout.addView(tvWeek, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        }
    }

    // initialize RecyclerView for date.
    public void initRCVMonth(String dateString)
    {

        if (!TextUtils.isEmpty(dateString))
        {
            calendarAdapter = new CalendarAdapter(getContext(), DateUtil.getMonthSelected(dateString));
        }
        else
        {
            return;
        }
        calendarAdapter.setDateDividerColor(dividerColor);
        calendarAdapter.setDateDividerSize(dividerSize);

        rcvMonth = new PageRecyclerView(getContext());
        rcvMonth.setBackgroundDrawable(monthBackground);
        rcvMonth.addOnScrollListener(new RCVMonthScrollListener());
        SpeedScrollLinearLayoutManager manager = new SpeedScrollLinearLayoutManager(getContext(), HORIZONTAL, false);
        manager.slowScroll(); // Scrolling slow
        rcvMonth.setLayoutManager(manager);

        // center item when scroll idle
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcvMonth);

        addView(rcvMonth, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // scroll to current date
        rcvMonth.scrollToPosition(currentPosition);
        showTitle(calendarAdapter.getDateList().get(currentPosition));
    }

    /**
     * If true, show whole calendar.
     * e.g. showing date is April, if show whole calendar, 03/30 and 05/01 will show.
     */
    public void setShowOverflowDate(boolean showOverflowDate)
    {
        calendarAdapter.setShowOverflowDate(showOverflowDate);
    }

    public boolean isShowOverflowDate()
    {
        return calendarAdapter.isShowOverflowDate();
    }

    /**
     * Return the layout of title.
     */
    public View getTitleLayout()
    {
        return vTitleLayout;
    }

    /**
     * Constructs a <code>SimpleDateFormat</code> using the given pattern and
     * the default date format symbols for the given locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     *
     * @param pattern the pattern describing the date and time format
     * @param locale  the locale whose date format symbols should be used
     */
    public void setTitleFormat(String pattern, Locale locale)
    {
        dateFormat = new SimpleDateFormat(pattern, locale);
        if (currentDate != null)
        {
            showTitle(currentDate);
        }
    }

    /**
     * Show title
     *
     * @param date date for title
     */
    private void showTitle(Date date)
    {
        currentDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDayOfMonth());
        tvTitle.setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Set listener for date be clicked.
     *
     * @param onDateClickedListener listener
     */
    public void setOnDateClickedListener(OnDateClickedListener onDateClickedListener)
    {
        calendarAdapter.setOnDateClickedListener(onDateClickedListener);
    }

    /**
     * Set listener for current showing month changed.
     *
     * @param onMonthChangedListener listener
     */
    public void setOnMonthChangedListener(final OnMonthChangedListener onMonthChangedListener)
    {
        this.onMonthChangedListener = onMonthChangedListener;
    }

    /**
     * Return adapter for custom data.
     */
    public VagueAdapter getVagueAdapter()
    {
        return vagueAdapter;
    }

    /**
     * Set adapter for custom data.
     *
     * @param vagueAdapter adapter
     */
    public void setVagueAdapter(VagueAdapter vagueAdapter)
    {
        this.vagueAdapter = vagueAdapter;
        calendarAdapter.setVagueAdapter(vagueAdapter);
        rcvMonth.setAdapter(calendarAdapter);
    }

    /**
     * Set drag enable for page.
     */
    public void setCanDrag(boolean canDrag)
    {
        rcvMonth.setCanDrag(canDrag);
    }

    /**
     * Return drag enable of page.
     */
    public boolean canDrag()
    {
        return rcvMonth.canDrag();
    }

    /**
     * Set fling enable for page.
     */
    public void setCanFling(boolean canFling)
    {
        rcvMonth.setCanFling(canFling);
    }

    /**
     * Return fling enable of page.
     */
    public boolean canFling()
    {
        return rcvMonth.canFling();
    }

    /**
     * Set button visible for last month.
     *
     * @param visibility {@link Visibility}
     */
    public void setLastMonthButtonVisibility(@Visibility int visibility)
    {
        ibtnLastMonth.setVisibility(visibility);
    }

    /**
     * Set button visible for next month.
     *
     * @param visibility {@link Visibility}
     */
    public void setNextMonthButtonVisibility(@Visibility int visibility)
    {
        ibtnNextMonth.setVisibility(visibility);
    }

    /**
     * Return item view of today. If today not showing, return null。
     */
    public View getTodayItemView()
    {
        Date curMonth = calendarAdapter.getDateList().get(currentPosition);
        Date today = DateUtil.today();
        if (curMonth.getYear() != today.getYear() || curMonth.getMonth() != today.getMonth())
        {
            // today not showing
            return null;
        }
        MonthView monthView = (MonthView) rcvMonth.getChildAt(0);
        return monthView.getDayItemView(today.getDayOfMonth());
    }

    /**
     * Set the layout will wrap content or not.
     *
     * @param scaleEnable if true, the layout will wrap content.
     */
    public void setScaleEnable(boolean scaleEnable)
    {
        this.scaleEnable = scaleEnable;
        rcvMonth.post(new Runnable()
        {
            @Override
            public void run()
            {
                updateCalendarHeight(calendarAdapter.getDateList().get(currentPosition));
            }
        });
    }

    // Update view's height by date.
    private void updateCalendarHeight(Date date)
    {
        // get data by date
        int year = date.getYear();
        int month = date.getMonth();
        int firstWeekOfMonth = DateUtil.getFirstWeekOfMonth(year, month);
        int dayCountOfMonth = DateUtil.getDayCountOfMonth(year, month);
        int rowCount = MonthView.ROW_MAX_COUNT;
        if (scaleEnable)
        {
            // calculate valid count of rows
            int validRowCount = (int) Math.ceil((firstWeekOfMonth + dayCountOfMonth) / (double) MonthView.COLUMN_COUNT);
            validRowCount = validRowCount <= MonthView.ROW_MIN_COUNT ? MonthView.ROW_MIN_COUNT : MonthView.ROW_MAX_COUNT;
            rowCount = validRowCount;
        }
        // update height
        int offsetHeight = rowCount * rcvMonth.getWidth() / MonthView.COLUMN_COUNT - rcvMonth.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(0, offsetHeight);
        animator.setDuration(150L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            int width = rcvMonth.getWidth();
            int height = rcvMonth.getHeight();

            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int offsetHeight = (int) animation.getAnimatedValue();
                rcvMonth.getLayoutParams().width = width;
                rcvMonth.getLayoutParams().height = height + offsetHeight;
                rcvMonth.requestLayout();
            }
        });
        animator.start();

    }

    /**
     * Set the button be used to change month enable or not.
     *
     * @param enable button enable
     */
    private void setChangeMonthButtonEnable(boolean enable)
    {
        if (ibtnLastMonth != null)
        {
            ibtnLastMonth.setEnabled(enable);
        }
        if (ibtnNextMonth != null)
        {
            ibtnNextMonth.setEnabled(enable);
        }
    }

    // A listener, call back when month change.
    private class ChangeMonthEvent implements OnClickListener
    {

        @Override
        public void onClick(View view)
        {

            setChangeMonthButtonEnable(false);

            if (view == ibtnLastMonth)
            {
                rcvMonth.smoothScrollToPosition(currentPosition - 1);
            }
            else if (view == ibtnNextMonth)
            {
                rcvMonth.smoothScrollToPosition(currentPosition + 1);
            }
        }

    }

    // A listener for RecyclerView, listen to view's scroll.
    private class RCVMonthScrollListener extends RecyclerView.OnScrollListener
    {

        boolean isLocating;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {

            if (newState != SCROLL_STATE_IDLE || isLocating)
            {
                isLocating = false;
                return;
            }

            isLocating = true;

            int position = recyclerView.getChildLayoutPosition(snapHelper.findSnapView(recyclerView.getLayoutManager()));
            if (currentPosition == position)
            {
                return;
            }
            currentPosition = position;
            Date date = calendarAdapter.getDateList().get(currentPosition);
            showTitle(date);

            // add new month if position exceed limit
            if (currentPosition == 0)
            {
                calendarAdapter.addNewLastMonth();
                currentPosition = 1;
            }
            else if (currentPosition >= calendarAdapter.getItemCount() - 1)
            {
                calendarAdapter.addNewNextMonth();
            }

            if (onMonthChangedListener != null)
            {
                // call back
                onMonthChangedListener.onMonthChanged(date);
            }
            setChangeMonthButtonEnable(true);

            if (scaleEnable)
            {
                updateCalendarHeight(date);
            }

        }

    }

    public String getMonthValue()
    {
        return monthValue;
    }

    public void setMonthValue(String monthValue)
    {
        this.monthValue = monthValue;
    }
}