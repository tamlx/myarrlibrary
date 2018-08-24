/*
 * Copyright 2017 StoneHui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package b.laixuantam.myaarlibrary.widgets.calendar.recyclerview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by StoneHui on 17/2/24.
 * <p>
 * A linear layout manager for control scroll speed.
 */
public class SpeedScrollLinearLayoutManager extends LinearLayoutManager
{

    private final float MILLISECONDS_PER_INCH = 25f;
    private float millisecondsPerInch = MILLISECONDS_PER_INCH;

    public SpeedScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public SpeedScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SpeedScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return SpeedScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            //if returned value is 2 ms, it means scrolling 1000 pixels with LinearInterpolation should take 2 seconds.
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return millisecondsPerInch / displayMetrics.densityDpi;
            }

            //Calculates the time it should take to scroll the given distance (in pixels)
            @Override
            protected int calculateTimeForScrolling(int dx) {

                return super.calculateTimeForScrolling(dx);
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    /**
     * scrolling fast
     */
    public void fastScroll() {
        millisecondsPerInch = MILLISECONDS_PER_INCH / 5f;
    }

    /**
     * scrolling slow
     */
    public void slowScroll() {
        millisecondsPerInch = MILLISECONDS_PER_INCH * 5f;
    }

    /**
     * scrolling normal
     */
    public void normalScroll() {
        millisecondsPerInch = MILLISECONDS_PER_INCH;
    }

}