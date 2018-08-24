package b.laixuantam.myaarlibrary.helper;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by laixuantam on 6/22/16.
 */
public class MyLog {
    public static boolean isEnableLog = true;

    /**
     * Lấy default tag
     *
     * @return the default tag
     */
    private static String getDefaultTag() {
        try {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String className = stackTrace[3].getClassName();

            return className.substring(className.lastIndexOf('.') + 1);
        } catch (Exception e) {
            return MyLog.class.getName();
        }
    }

    public static void e(String tag, String message) {
        if (isEnableLog) {
            if (!TextUtils.isEmpty(message)) {
                if (!TextUtils.isEmpty(tag)) {
                    Log.e(tag, message);
                } else {
                    Log.e(getDefaultTag(), message);
                }
            }
        }
    }


    public static void e(Throwable exception) {
        if (isEnableLog) {
            try {
                String tag = MyLog.getDefaultTag();

                {
                    Log.e(tag, exception.getMessage(), exception);
                }
            } catch (Exception e) {
            }
        }
    }

    public static void d(String tag, String message) {
        if (isEnableLog) {
            try {
                {
                    Log.d(tag, message);
                }
            } catch (Exception e) {
            }
        }
    }


    public static void d(String message) {
        if (isEnableLog) {
            try {
                String tag = MyLog.getDefaultTag();

                {
                    Log.d(tag, message);
                }
            } catch (Exception e) {
            }
        }
    }
}
