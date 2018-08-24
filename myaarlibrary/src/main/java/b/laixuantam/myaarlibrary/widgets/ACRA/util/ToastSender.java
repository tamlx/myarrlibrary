package b.laixuantam.myaarlibrary.widgets.ACRA.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.widgets.ACRA.ACRA;

/**
 * Responsible for sending Toasts under all circumstances.
 * <p/>
 * @author William Ferguson
 * @since 4.3.0
 */
public final class ToastSender {

    /**
     * Sends a Toast and ensures that any Exception thrown during sending is handled.
     *
     * @param context           Application context.
     * @param toastResourceId   Id of the resource to send as the Toast message.
     * @param toastLength       Length of the Toast.
     */
    public static void sendToast(Context context, int toastResourceId, int toastLength) {
        try {
            Toast.makeText(context, toastResourceId, toastLength).show();
        } catch (RuntimeException e) {
            Log.e(ACRA.LOG_TAG, "Could not send crash Toast", e);
        }
    }
}
