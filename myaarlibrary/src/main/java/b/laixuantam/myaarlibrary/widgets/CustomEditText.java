/*
 * (c) Copyright GoTechCom 2017
 */

package b.laixuantam.myaarlibrary.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText
{
    public CustomEditText(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (!isInEditMode())
        {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoMono-Regular.ttf");
            setTypeface(typeface);
        }
    }
}
