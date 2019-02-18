package b.laixuantam.myaarlibrary.widgets.button;

import android.support.v7.widget.AppCompatButton;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import b.laixuantam.myaarlibrary.R;

/**

 -----------------XML-------------------
     <laixuantam.myaarlibrary.widgets.button.CustomButton
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     android:layout_margin="10dp"
     android:text="Hello World!"
     android:textColor="@android:color/white"
     app:qb_backgroundColor="@color/btnGreen"
     app:qb_radius="100"
     app:qb_strokeColor="@color/colorPrimary"
     app:qb_strokeDashWidth="90"
     app:qb_strokeWidth="5" />

 -----------------CODE------------------------
     CustomButton cButton = (CustomButton) findViewById(R.id.btn);

     cButton.setCornerRadious(5);
     cButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
     cButton.setStrokeWidth(5);
     cButton.setStrokeDashGap(5);
     cButton.setCornerRadious(40);
     cButton.setStrokeDashWidth(90);
     cButton.setStrokeColor(getResources().getColor(R.color.colorPrimaryDark));

 */


public class CustomButton extends AppCompatButton {
    private int mRadius;
    private int mStrokeDashWidth, mStrokeDashGap;
    private int mStrokeColor = 0, mStrokeWidth, mBackgroundColor;

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        mBackgroundColor = a.getColor(R.styleable.CustomButton_cb_backgroundColor, getResources().getColor(R.color.colorPrimary));
        mRadius = a.getInt(R.styleable.CustomButton_cb_radius, 100);
        mStrokeDashGap = a.getInt(R.styleable.CustomButton_cb_strokeDashGap, 0);
        mStrokeDashWidth = a.getInt(R.styleable.CustomButton_cb_strokeDashWidth, 0);
        mStrokeWidth = a.getInt(R.styleable.CustomButton_cb_strokeWidth, 0);
        mStrokeColor = a.getColor(R.styleable.CustomButton_cb_strokeColor, mBackgroundColor);
        a.recycle();

        notifyChanges();
    }

    private void notifyChanges() {

        if (mStrokeColor == 0) {
            mStrokeColor = manipulateColor(mBackgroundColor, 0.9f);
        }

        Drawable pressed = getDrawable1(manipulateColor(mBackgroundColor, 0.8f), mRadius);
        Drawable normal = getDrawable1(mBackgroundColor, mRadius);

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);
        setBackground(states);

    }

    public GradientDrawable getDrawable1(int backgroundColor, float radius) {

        int colors[] = {backgroundColor,
                manipulateColor(backgroundColor, 0.8f)};

        GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius);

        shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        shape.setStroke(mStrokeWidth, mStrokeColor);
        if (mStrokeDashGap > 0 || mStrokeDashWidth > 0) {
            shape.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap);
        }

        return shape;
    }

    private int manipulateColor(int color, float factor) {
        //factor = 0.8f
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        notifyChanges();
    }

    public void setCornerRadious(int cornerRadious) {
        mRadius = cornerRadious;
        notifyChanges();
    }

    public void setStrokeDashGap(int strokeDashGap) {
        mStrokeDashGap = strokeDashGap;
        notifyChanges();
    }

    public void setStrokeDashWidth(int strokeDashWidth) {
        mStrokeDashWidth = strokeDashWidth;
        notifyChanges();
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        notifyChanges();
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        notifyChanges();
    }

}
