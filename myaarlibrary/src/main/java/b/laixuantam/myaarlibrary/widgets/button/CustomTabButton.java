package b.laixuantam.myaarlibrary.widgets.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.helper.PixelHelper;

/**
 ---------------- XML -------------------------
    <CustomTabButton
         android:id="@+id/status_tabs"
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         android:layout_centerInParent="true"
         app:tab_width="80dp" />

 ---------------- View -------------------------
     @UiElement(R.id.status_tabs)
     public CustomTabButton tabs;

 ---------------- init --------------------------
     String[] tabs = new String[2];
     tabs[0] = getString(R.string.order_product_status_waiting);
     tabs[1] = getString(R.string.order_product_status_done);

     ui.tabs.build(tabs, 0, new OnButtonSelected() {
         @Override
         public void onButtonSelected(int position) {

         }
     });

 */


public class CustomTabButton extends RelativeLayout
{
    private LinearLayout indicator;
    private int tabCount = 0;
    private int currentTab = -1;
    private OnButtonSelected listener;
    private final List<TextView> tabTitles = new ArrayList<>();
    private boolean fullWidth = false;
    private boolean isRendered = false;
    private int tabWidth = 0;

    public CustomTabButton(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomTabButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTabButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        // calc tab width and resize it
        if ((indicator != null) && changed)
        {
            if (fullWidth && tabCount > 0)
            {
                int containerWidth = indicator.getWidth();
                int tabWidth = containerWidth / tabCount;

                for (int i = 0; i < tabTitles.size(); i++)
                {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabTitles.get(i)
                            .getLayoutParams();
                    lp.width = tabWidth;
                    tabTitles.get(i).setLayoutParams(lp);
                }
            }
            else
            {
                calcTabSize();
            }
        }

        isRendered = true;
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (!isInEditMode())
        {
            if (attrs != null)
            {
                TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTabButton);
                fullWidth = styledAttributes.getBoolean(R.styleable.CustomTabButton_full_width, false);
                tabWidth = styledAttributes.getDimensionPixelSize(R.styleable.CustomTabButton_tab_width, 0);
                styledAttributes.recycle();
            }

            indicator = new LinearLayout(context);
            indicator.setOrientation(LinearLayout.HORIZONTAL);
            indicator.setGravity(Gravity.CENTER_HORIZONTAL);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            indicator.setLayoutParams(lp);
            addView(indicator);
        }
    }

    private void addTab(String text)
    {
        if (indicator != null)
        {
            int padding = (int) PixelHelper.convertDpToPixel(10, getContext()); // padding for 10dp
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(getResources().getColorStateList(R.color.view_tab_button_text_color));
            textView.setMaxLines(1);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(onClickListener);
            textView.setTag(tabCount);

            if (tabWidth > 0 && !fullWidth)
            {
                textView.setWidth(tabWidth);
            }

            tabCount++;
            indicator.addView(textView);
            tabTitles.add(textView);

            for (int i = 0; i < tabTitles.size(); i++)
            {
                if (i == 0 && tabTitles.size() > 1)
                {
                    // first tab, set first background
                    tabTitles.get(i)
                            .setBackgroundResource(R.drawable.view_tab_button_background_first);
                }
                else if (i == tabTitles.size() - 1 && tabTitles.size() > 1)
                {
                    // last tab, set last background
                    tabTitles.get(i)
                            .setBackgroundResource(R.drawable.view_tab_button_background_last);
                }
                else
                {
                    tabTitles.get(i)
                            .setBackgroundResource(R.drawable.view_tab_button_background_center);
                }
            }

            if (isRendered)
            {
                calcTabSize();
            }
        }
    }

    /**
     * Calculates the tab width size depending on tab text length
     */
    private void calcTabSize()
    {
        if (!fullWidth && (indicator != null) && (tabWidth == 0))
        {
            // get parent width contain view
            int parentWidth = indicator.getWidth();

            // find longest tab's width
            int width = 0;
            for (int i = 0; i < tabTitles.size(); i++)
            {
                tabTitles.get(i).measure(0, 0);

                if (tabTitles.get(i).getMeasuredWidth() > width)
                {
                    width = tabTitles.get(i).getMeasuredWidth();
                }
            }

            // check width is valid to parent width to ensure total tab width always small than parent width
            if (width > parentWidth / tabTitles.size())
            {
                width = parentWidth / tabTitles.size();
            }

            // apply width for all tabs
            for (int i = 0; i < tabTitles.size(); i++)
            {
                tabTitles.get(i).setWidth(width);
            }
        }
    }

    public void build(String[] tabTitles, int current, OnButtonSelected listener)
    {
        this.listener = listener;

        for (String tabTitle : tabTitles)
        {
            addTab(tabTitle);
        }

        setCurrent(current);
    }

    public void build(String[] tabTitles, Enum enumType, OnButtonSelected listener)
    {
        build(tabTitles, enumType.ordinal(), listener);
    }

    private void setCurrent(int current)
    {
        if (current < tabTitles.size())
        {
            // set unselected for previous view
            if (currentTab != -1)
            {
                tabTitles.get(currentTab).setSelected(false);
            }

            currentTab = current;
            // set selected for current view
            tabTitles.get(current).setSelected(true);
        }
    }

    public void setCurrent(Enum enumType)
    {
        setCurrent(enumType.ordinal());
    }

    private final OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int position = (int) view.getTag();

            setCurrent(position);

            if (listener != null)
            {
                listener.onButtonSelected(position);
            }
        }
    };

    public interface OnButtonSelected
    {
        void onButtonSelected(int position);
    }
}