package b.laixuantam.myaarlibrary.widgets.progresswindow.kprogresshud;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import b.laixuantam.myaarlibrary.R;


class SpinView extends ImageView implements Indeterminate {

    private float mRotateDegrees;
    private int mFrameTime;
    private boolean mNeedToUpdateView;
    private Runnable mUpdateViewRunnable;
    private int id_color = -1;

    public SpinView(Context context) {
        super(context);
        init();
    }

    public SpinView(Context context, @ColorRes int id_color) {
        super(context);
        this.id_color = id_color;
        init();
    }

    public SpinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImageResource(R.drawable.kprogresshud_spinner);
        if (id_color != -1) {
            setColorFilter(ContextCompat.getColor(getContext(), id_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        mFrameTime = 1000 / 12;
        mUpdateViewRunnable = new Runnable() {
            @Override
            public void run() {
                mRotateDegrees += 30;
                mRotateDegrees = mRotateDegrees < 360 ? mRotateDegrees : mRotateDegrees - 360;
                invalidate();
                if (mNeedToUpdateView) {
                    postDelayed(this, mFrameTime);
                }
            }
        };
    }

    @Override
    public void setAnimationSpeed(float scale) {
        mFrameTime = (int) (1000 / 12 / scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mRotateDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedToUpdateView = true;
        post(mUpdateViewRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        mNeedToUpdateView = false;
        super.onDetachedFromWindow();
    }
}
