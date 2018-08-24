/*
 * (c) Copyright GoTechCom 2017
 */

package b.laixuantam.myaarlibrary.widgets.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;

import b.laixuantam.myaarlibrary.R;


public class AlertDialog extends AppDialog<AlertDialog.AlertDialogListener>
{
    public AlertDialog()
    {
        setCancelable(true);
    }

    private static boolean POPUP_OPEN = false;

    public static AlertDialog newInstance(String message, String title, AlertDialogListener listener)
    {
        return newInstance(message, title, false, R.string.dongy, listener);
    }

    public static AlertDialog newInstance(String message, String title, boolean cancelable, int buttonTextResId, AlertDialogListener listener)
    {
        AlertDialog dialog = new AlertDialog();
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
    protected boolean isListenerOptional()
    {
        return true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        String message = null, title = null;
        boolean cancelable = false;
        int buttonText = R.string.dongy;
        if (bundle != null)
        {
            buttonText = bundle.getInt(EXTRA_BUTTON_TEXT);
            message = bundle.getString(EXTRA_MESSAGE);
            title = bundle.getString(EXTRA_TITLE);
            cancelable = bundle.getBoolean(EXTRA_CANCELABLE);
        }
        setCancelable(cancelable);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity()).setMessage(message).setPositiveButton(buttonText, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int whichButton)
            {
                if (getListener() != null)
                {
                    getListener().onOk(AlertDialog.this);
                }
            }
        });

        if (!TextUtils.isEmpty(title))
        {
            builder.setTitle(title);
        }

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        POPUP_OPEN = false;
    }

    public interface AlertDialogListener
    {
        void onOk(AppDialog<?> f);
    }
}
