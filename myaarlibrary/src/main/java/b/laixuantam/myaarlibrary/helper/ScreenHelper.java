package b.laixuantam.myaarlibrary.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenHelper
{
    /**
     * Returns screen size.
     *
     * @param context the context
     * @return the screen size
     */
    public static ScreenSize getScreenSize(Context context)
    {
        int width = 0;
        int height = 0;

        if (context != null)
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        }

        return new ScreenSize(width, height);
    }

    public static class ScreenSize
    {
        public final int width;
        public final int height;

        public ScreenSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        @SuppressWarnings("SuspiciousNameCombination")
        public ScreenSize swap()
        {
            return new ScreenSize(height, width);
        }
    }
}
