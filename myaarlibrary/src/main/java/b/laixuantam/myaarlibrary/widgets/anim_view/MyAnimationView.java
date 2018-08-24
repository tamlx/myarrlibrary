package b.laixuantam.myaarlibrary.widgets.anim_view;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by laixuantam on 1/13/18.
 */

public class MyAnimationView
{
    private View views;

    public MyAnimationView(View views)
    {
        this.views = views;
    }

    public void translationY(int from, int to, int duration)
    {
        ObjectAnimator anim = ObjectAnimator.ofFloat(views, "translationY", from, to);
        anim.setDuration(duration);
        anim.start();
    }
}
