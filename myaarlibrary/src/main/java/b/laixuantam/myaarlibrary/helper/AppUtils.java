package b.laixuantam.myaarlibrary.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.AlarmClock;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

import b.laixuantam.myaarlibrary.R;

public class AppUtils {
    public static void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void toggleKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }


    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(v.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);

    }

    public static void showKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * set alarm
     *
     * @param targetCal
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setAlarm(Context context, Calendar targetCal) {
        Toast.makeText(context, "\n\n***\n" + "Alarm is set " + targetCal.getTime() + "\n" + "***\n", Toast.LENGTH_LONG).show();
        Intent openNewAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        openNewAlarm.putExtra(AlarmClock.EXTRA_HOUR, targetCal.get(Calendar.HOUR_OF_DAY));
        openNewAlarm.putExtra(AlarmClock.EXTRA_MINUTES, targetCal.get(Calendar.MINUTE));
        openNewAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(openNewAlarm);
    }

    public static Spanned underlineText(String text) {
        return Html.fromHtml("<u>" + text + "</u>");
    }

    /**
     * Random and return uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static boolean checkRequiredEditText(Context context, EditText... editTexts) {
        boolean r = true;
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError(context.getString(R.string.error_required));
                r = false;
            } else {
                editText.setError("");
            }
        }
        return r;
    }

    public static void shakeView(View view) {
        view.animate()
                .translationX(-15).translationX(15)
                .setDuration(30)
                .setInterpolator(new CycleInterpolator(150 / 30))
                .setDuration(150)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                    }
                })
                .start();
    }

    public static void hightLightLayout(Context c, View view) {
        view.setBackgroundColor(ContextCompat.getColor(c, R.color.light_gray));
    }

    public static void setNomarlBackgroundLayout(Context c, View view) {
        view.setBackgroundColor(ContextCompat.getColor(c, R.color.white));
    }

    public static void hightlight(TextView textView, String filterString) {
        String text = textView.getText().toString().toLowerCase();
        text = AccentRemove.removeAccent(text);
        int startPos = text.indexOf(filterString);
        if (startPos > -1) {
            int endPos = startPos + filterString.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(textView.getText());
            spanText.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(spanText, TextView.BufferType.SPANNABLE);
        }
    }

    //    private boolean checkEmail() {
    //        String email = loginInputEmail.getText().toString().trim();
    //        if (email.isEmpty() || !isEmailValid(email)) {
    //
    //            loginInputLayoutEmail.setErrorEnabled(true);
    //            loginInputLayoutEmail.setError(getString(R.string.err_msg_email));
    //            loginInputEmail.setError(getString(R.string.err_msg_required));
    //            requestFocus(loginInputEmail);
    //            return false;
    //        }
    //        loginInputLayoutEmail.setErrorEnabled(false);
    //        return true;
    //    }
    //
    //    private boolean checkPassword() {
    //
    //        String password = loginInputPassword.getText().toString().trim();
    //        if (password.isEmpty() || !isPasswordValid(password)) {
    //
    //            loginInputLayoutPassword.setError(getString(R.string.err_msg_password));
    //            loginInputPassword.setError(getString(R.string.err_msg_required));
    //            requestFocus(loginInputPassword);
    //            return false;
    //        }
    //        loginInputLayoutPassword.setErrorEnabled(false);
    //        return true;
    //    }

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return (password.length() >= 6);
    }

    public void requestFocus(Activity context, View view) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}