/*
 * (c) Copyright GoTechCom 2017
 */

package b.laixuantam.myaarlibrary.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import b.laixuantam.myaarlibrary.R;

/**
 * Custom progressbar with red color loading
 */
public class CustomProgressBar extends ProgressBar
{
    public CustomProgressBar(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (!isInEditMode())
        {
            if (attrs != null)
            {
                TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
                int color = styledAttributes.getColor(R.styleable.CustomProgressBar_foreground_color, 0);
                styledAttributes.recycle();

                if (color != 0)
                {
                    getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.color_primary_CustomProgressBar), PorterDuff.Mode.MULTIPLY);
                }
            }
            else
            {
                getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.color_primary_CustomProgressBar), PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}