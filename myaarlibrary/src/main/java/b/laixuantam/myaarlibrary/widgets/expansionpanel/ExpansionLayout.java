package b.laixuantam.myaarlibrary.widgets.expansionpanel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.R;

public class ExpansionLayout extends NestedScrollView {

    private final List<IndicatorListener> indicatorListeners = new ArrayList<>();
    private final List<Listener> listeners = new ArrayList<>();
    private boolean expanded = false;
    private Animator animator;

    public ExpansionLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ExpansionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpansionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        requestDisallowInterceptTouchEvent(true);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpansionLayout);
            if (a != null) {
                expanded = a.getBoolean(R.styleable.ExpansionLayout_expansion_expanded, expanded);
                a.recycle();
            }
        }
    }

    public void addListener(Listener listener) {
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void addIndicatorListener(IndicatorListener listener) {
        if (listener != null && !indicatorListeners.contains(listener))
            indicatorListeners.add(listener);
    }

    public void removeIndicatorListener(IndicatorListener listener) {
        if (listener != null) {
            indicatorListeners.remove(listener);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!expanded) {
            setHeight(0f);
        }
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ExpansionLayout can host only one direct child");
        }

        super.addView(child);
        onViewAdded();
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ExpansionLayout can host only one direct child");
        }

        super.addView(child, index);
        onViewAdded();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ExpansionLayout can host only one direct child");
        }

        super.addView(child, params);
        onViewAdded();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ExpansionLayout can host only one direct child");
        }

        super.addView(child, index, params);
        onViewAdded();
    }

    private void onViewAdded(){
        if (getChildCount() != 0) {
            final View childView = getChildAt(0);
            childView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    childView.getViewTreeObserver().removeOnPreDrawListener(this);

                    //now we have a size
                    if (expanded) {
                        expand(false);
                    }

                    childView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (expanded && animator == null) {
                                final int height = bottom - top;
                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setHeight(height);

                                    }
                                });
                            }
                        }
                    });

                    return true;
                }
            });
        }
    }

    public void collapse(boolean animated) {
        if (!isEnabled() || !expanded) {
            return;
        }
        pingIndicatorListeners(false);
        if (animated) {
            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f * getHeight(), 0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setHeight((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator = null;
                    pingListeners();
                }
            });
            expanded = false;
            animator = valueAnimator;
            valueAnimator.start();
        } else {
            setHeight(0f);
            expanded = false;
            pingListeners();
        }
    }

    private void pingIndicatorListeners(boolean willBeExpanded) {
        for (IndicatorListener indicatorListener : indicatorListeners) {
            if (indicatorListener != null) {
                indicatorListener.onStartedExpand(this, willBeExpanded);
            }
        }
    }

    private void pingListeners() {
        for (Listener listener : listeners) {
            if (listener != null) {
                listener.onExpansionChanged(this, expanded);
            }
        }
    }

    public void expand(boolean animated) {
        if (!isEnabled() || expanded) {
            return;
        }

        pingIndicatorListeners(true);
        if (animated) {
            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, getChildAt(0).getHeight());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setHeight((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator = null;
                    pingListeners();
                }
            });
            expanded = true;
            animator = valueAnimator;
            valueAnimator.start();
        } else {
            setHeight(getChildAt(0).getHeight());
            expanded = true;
            pingListeners();
        }
    }

    private void setHeight(float height) {
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = (int) height;
            setLayoutParams(layoutParams);
        }
    }

    public void toggle(boolean animated) {
        if (expanded) {
            collapse(animated);
        } else {
            expand(animated);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle savedInstance = new Bundle();
        savedInstance.putParcelable("super", super.onSaveInstanceState());
        savedInstance.putBoolean("expanded", expanded);
        return savedInstance;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            final Bundle savedInstance = (Bundle) state;
            boolean expanded = savedInstance.getBoolean("expanded");
            if(expanded){
               expand(false);
            } else {
                collapse(false);
            }
            super.onRestoreInstanceState(savedInstance.getParcelable("super"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public interface Listener {
        void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded);
    }

    public interface IndicatorListener {
        void onStartedExpand(ExpansionLayout expansionLayout, boolean willExpand);
    }
}

/**

 ====================== expansion_panel_sample_panel ===============================
 <?xml version="1.0" encoding="utf-8"?>

 <LinearLayout android:layout_height="wrap_content"
 android:layout_width="match_parent"
 android:orientation="vertical"
 xmlns:app="http://schemas.android.com/apk/res-auto"
 xmlns:android="http://schemas.android.com/apk/res/android">

 <com.github.florent37.expansionpanel.ExpansionHeader
 android:layout_height="wrap_content"
 android:layout_width="match_parent"
 android:id="@+id/sampleHeader"
 app:expansion_headerIndicator="@id/headerIndicator"
 app:expansion_layout="@id/expansionLayout"
 app:expansion_toggleOnClick="true">

 <RelativeLayout
 android:background="@android:color/white"
 android:layout_height="48dp"
 android:layout_width="match_parent"
 android:paddingLeft="16dp"
 android:paddingRight="16dp">

 <TextView
 android:layout_centerVertical="true"
 android:layout_height="wrap_content"
 android:layout_width="wrap_content"
 android:text="Trip name"
 android:textColor="#3E3E3E" />

 <TextView
 android:layout_centerVertical="true"
 android:layout_height="wrap_content"
 android:layout_marginLeft="120dp"
 android:layout_width="wrap_content"
 android:text="Carribean cruise"
 android:textColor="#AAA" />

 <android.support.v7.widget.AppCompatImageView
 android:adjustViewBounds="true"
 android:id="@+id/headerIndicator"
 android:layout_alignParentRight="true"
 android:layout_centerVertical="true"
 android:layout_height="wrap_content"
 android:layout_marginLeft="16dp"
 android:layout_width="wrap_content"
 app:srcCompat="@drawable/ic_expansion_header_indicator_grey_24dp" />

 </RelativeLayout>

 </com.github.florent37.expansionpanel.ExpansionHeader>

 <com.github.florent37.expansionpanel.ExpansionLayout
 android:id="@+id/expansionLayout"
 app:expansion_expanded="false"
 android:layout_height="wrap_content"
 android:layout_width="match_parent">

 <LinearLayout
 android:id="@+id/container"
 android:layout_height="wrap_content"
 android:layout_width="match_parent"
 android:orientation="vertical">

 <TextView
 android:background="#EEE"
 android:gravity="center"
 android:layout_height="100dp"
 android:layout_width="match_parent"
 android:text="content" />

 </LinearLayout>

 </com.github.florent37.expansionpanel.ExpansionLayout>
 </LinearLayout>

 ====================== expansion_panel_sample_main_viewgroup ===============================
 <ExpansionsViewGroupLinearLayout
 android:layout_width="match_parent"
 android:layout_height="wrap_content"
 app:expansion_openOnlyOne="true"
 android:orientation="vertical">

 </ExpansionsViewGroupLinearLayout>

 */