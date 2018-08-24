package b.laixuantam.myaarlibrary.widgets.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.helper.ScreenHelper;


public class ProgressDialog extends AppDialog<ConfirmDialog.ConfirmDialogListener>
{
    public static final String TAG = ProgressDialog.class.getSimpleName();
    private static final String EXTRA_CANCELABLE = "EXTRA_CANCELABLE";
    protected android.app.ProgressDialog mProgressDialog;

    public ProgressDialog()
    {
        setCancelable(false);
    }

    public static ProgressDialog newInstance(String message)
    {
        return newInstance(message, false);
    }

    public static ProgressDialog newInstance(String message, boolean cancelable)
    {
        ProgressDialog dialog = new ProgressDialog();
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
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
        String message = null;
        boolean cancelable = false;
        if (bundle != null)
        {
            message = bundle.getString(EXTRA_MESSAGE);
            cancelable = bundle.getBoolean(EXTRA_CANCELABLE);
        }
        setCancelable(cancelable);
        View dialoglayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading, null);
        TextView textMessage = (TextView) dialoglayout.findViewById(R.id.dialog_message);
        if (TextUtils.isEmpty(message))
        {
            textMessage.setText(R.string.loading);
        }
        else
        {
            textMessage.setText(message);
        }
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialoglayout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        int screenWidth = ScreenHelper.getScreenSize(getActivity()).width;
        lp.width = screenWidth / 2;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        return dialog;
    }
}
