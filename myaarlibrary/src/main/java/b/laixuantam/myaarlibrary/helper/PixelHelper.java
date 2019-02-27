/*
 * (c) Copyright GoTechCom 2017
 */

package b.laixuantam.myaarlibrary.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class PixelHelper
{
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      a value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context context to get resources and device specific display metrics
     * @return a float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      a value in px (pixels) unit. Which we need to convert into db
     * @param context context to get resources and device specific display metrics
     * @return a float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        return px / (metrics.densityDpi / 160f);
    }
}
