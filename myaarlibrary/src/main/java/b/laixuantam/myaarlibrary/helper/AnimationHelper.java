package b.laixuantam.myaarlibrary.helper;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;

public class AnimationHelper
{
    private AnimationHelper()
    {
    }

    public static synchronized void fade(final View view, final boolean fadeIn)
    {
        if (fadeIn)
        {
            view.setAlpha(0);
        }
        else
        {
            view.setAlpha(1);
        }

        Animation animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation)
            {
                super.applyTransformation(interpolatedTime, transformation);

                if (fadeIn)
                {
                    view.setAlpha(interpolatedTime);
                }
                else
                {
                    view.setAlpha(1 - interpolatedTime);
                }

                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        animation.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                if (fadeIn)
                {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (!fadeIn)
                {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });

        animation.setDuration(300);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(animation);
    }

    public static synchronized void expand(final View view, final float height)
    {
        if (view.getVisibility() != View.VISIBLE)
        {
            view.getLayoutParams().height = 0;
            view.setVisibility(View.VISIBLE);

            Animation animation = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation transformation)
                {
                    super.applyTransformation(interpolatedTime, transformation);

                    view.getLayoutParams().height = (int) (height * interpolatedTime);
                    view.requestLayout();
                }

                @Override
                public boolean willChangeBounds()
                {
                    return true;
                }
            };

            animation.setDuration(300);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            view.startAnimation(animation);
        }
    }

    public static synchronized void collapse(final View view, final float height)
    {
        if (view.getVisibility() == View.VISIBLE)
        {
            Animation animation = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation transformation)
                {
                    super.applyTransformation(interpolatedTime, transformation);

                    view.getLayoutParams().height = (int) (height - (height * interpolatedTime));
                    view.requestLayout();

                    if (interpolatedTime == 1)
                    {
                        view.getLayoutParams().height = 0;
                        view.setVisibility(View.GONE);
                    }
                }

                @Override
                public boolean willChangeBounds()
                {
                    return true;
                }
            };

            animation.setDuration(300);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            view.startAnimation(animation);
        }
    }
}