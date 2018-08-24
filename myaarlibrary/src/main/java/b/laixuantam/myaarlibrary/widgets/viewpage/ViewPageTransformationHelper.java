package b.laixuantam.myaarlibrary.widgets.viewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by laixuantam on 5/22/18.
 */

public class ViewPageTransformationHelper implements ViewPager.PageTransformer {

    public enum ViewPageTransformType {
        CubeInDepthTransformer,
        DepthTransformer,
        ShadowTransformer

    }

    private int maxTranslateOffsetX;
    private ViewPager viewPager;
    private ViewPageTransformType pageTransformType;

    private boolean isSacleItem;

    public ViewPageTransformationHelper(Context context, ViewPageTransformType type) {
        this.maxTranslateOffsetX = dp2px(context, 180);
        this.pageTransformType = type;
        isSacleItem = true;
    }

    @Override
    public void transformPage(View page, float position) {

        switch (pageTransformType) {
            case CubeInDepthTransformer:
                CubeInDepthTransformation(page, position);
                break;

            case DepthTransformer:
                DepthTransformation(page, position);
                break;

            case ShadowTransformer:
                ShadowTransformeation(page, position);

                break;
        }


    }

    private void ShadowTransformeation(View view, float position) {

        float scaleValue = 0.25f;

        if (!isSacleItem) {
            scaleValue = 0;
        }

        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }

        int leftInScreen = view.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
        float offsetRate = (float) offsetX * scaleValue / viewPager.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);
        if (scaleFactor > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }
    }

    private void CubeInDepthTransformation(View page, float position) {
        page.setCameraDistance(20000);

        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 0) {
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(90 * Math.abs(position));
        } else if (position <= 1) {
            page.setAlpha(1);
            page.setPivotX(0);
            page.setRotationY(-90 * Math.abs(position));
        } else {
            page.setAlpha(0);
        }


        if (Math.abs(position) <= 0.5) {
            page.setScaleY(Math.max(.4f, 1 - Math.abs(position)));
        } else if (Math.abs(position) <= 1) {
            page.setScaleY(Math.max(.4f, 1 - Math.abs(position)));

        }
    }

    private void DepthTransformation(View page, float position) {
        if (position < -1) {    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);

        } else if (position <= 1) {    // (0,1]
            page.setTranslationX(-position * page.getWidth());
            page.setAlpha(1 - Math.abs(position));
            page.setScaleX(1 - Math.abs(position));
            page.setScaleY(1 - Math.abs(position));

        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }
    }

    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    public boolean isSacleItem() {
        return isSacleItem;
    }

    public void setSacleItem(boolean sacleItem) {
        isSacleItem = sacleItem;
    }
}
