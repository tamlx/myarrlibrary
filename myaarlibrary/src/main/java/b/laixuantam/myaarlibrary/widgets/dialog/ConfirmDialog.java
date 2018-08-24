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
        return newInstance(message, title, true, R.string.dongy, true, R.string.huybo, listener);
    }

    public static ConfirmDialog newInstance(String message, String title, boolean cancelable, int buttonTextResId, boolean isShowNegativeButton, int buttonTextNegativeButton, ConfirmDialogListener listener) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setListener(listener);
        Bundle args = new Bundle();
        args.putInt(EXTRA_BUTTON_TEXT, buttonTextResId);
        args.putInt(EXTRA_BUTTON_TEXT_NEGATIVE, buttonTextNegativeButton);
        args.putString(EXTRA_MESSAGE, message);
        args.putString(EXTRA_TITLE, title);
        args.putBoolean(EXTRA_CANCELABLE, cancelable);
        args.putBoolean(EXTRA_SHOW_NEGATIVE, isShowNegativeButton);
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
        boolean isShowNegativeButton = true;
        int buttonText = R.string.dongy;
        int buttonNegative = R.string.huybo;
        isOk = false;
        if (bundle != null) {
            buttonText = bundle.getInt(EXTRA_BUTTON_TEXT);
            buttonNegative = bundle.getInt(EXTRA_BUTTON_TEXT_NEGATIVE);
            message = bundle.getString(EXTRA_MESSAGE);
            title = bundle.getString(EXTRA_TITLE);
            cancelable = bundle.getBoolean(EXTRA_CANCELABLE);
            isShowNegativeButton = bundle.getBoolean(EXTRA_SHOW_NEGATIVE);
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
        });
        if (isShowNegativeButton) {
            builder.setNegativeButton(buttonNegative, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        builder.setCancelable(cancelable);

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
