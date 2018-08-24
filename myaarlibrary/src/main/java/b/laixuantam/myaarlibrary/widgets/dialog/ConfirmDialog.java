package b.laixuantam.myaarlibrary.widgets.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import b.laixuantam.myaarlibrary.R;

public class ConfirmDialog extends AppDialog<ConfirmDialog.ConfirmDialogListener> {
    private boolean isOk = false;

    public ConfirmDialog() {
        setCancelable(true);
    }

    public static void showLogoutDialog(final FragmentActivity activity, ConfirmDialogListener listener) {
        ConfirmDialog.newInstance(activity.getString(R.string.dialog_logout_message), activity.getString(R.string.dialog_logout_title), listener).show(activity.getSupportFragmentManager(), "LogoutDialog");
    }

    public static ConfirmDialog newInstance(String message, String title, ConfirmDialogListener listener) {
        return newInstance(message, title, true, R.string.dongy, listener);
    }

    public static ConfirmDialog newInstance(String message, String title, boolean cancelable, int buttonTextResId, ConfirmDialogListener listener) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setListener(listener);
        Bundle args = new Bundle();
        args.putInt(EXTRA_BUTTON_TEXT, buttonTextResId);
        args.putString(EXTRA_MESSAGE, message);
        args.putString(EXTRA_TITLE, title);
        args.putBoolean(EXTRA_CANCELABLE, cancelable);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    protected boolean isListenerOptional() {
        return true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String message = null, title = null;
        boolean cancelable = false;
        int buttonText = R.string.dongy;
        int buttonNegative = R.string.huybo;
        isOk = false;
        if (bundle != null) {
            buttonText = bundle.getInt(EXTRA_BUTTON_TEXT);
            message = bundle.getString(EXTRA_MESSAGE);
            title = bundle.getString(EXTRA_TITLE);
            cancelable = bundle.getBoolean(EXTRA_CANCELABLE);
        }
        setCancelable(cancelable);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage(message).setPositiveButton(buttonText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                isOk = true;
                if (getListener() != null) {
                    getListener().onOk(ConfirmDialog.this);
                }
            }
        }).setNegativeButton(buttonNegative, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(cancelable);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getListener() != null && !isOk) {
            getListener().onCancel(ConfirmDialog.this);
        }
    }

    public interface ConfirmDialogListener {
        void onOk(AppDialog<?> f);

        void onCancel(AppDialog<?> f);
    }
}
