package b.laixuantam.myaarlibrary.widgets.viewpage;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by laixuantam on 5/22/18.
 */

public class ViewPageTransformationHelper implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        CubeInDepthTransformation(page, position);

//        DepthTransformation(page,position);
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

}
