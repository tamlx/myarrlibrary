package b.laixuantam.myaarlibrary.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.helper.BusHelper;
import b.laixuantam.myaarlibrary.widgets.dialog.ProgressDialog;
import b.laixuantam.myaarlibrary.widgets.progresswindow.ProgressWindow;
import b.laixuantam.myaarlibrary.widgets.progresswindow.ProgressWindowConfiguration;
import b.laixuantam.myaarlibrary.widgets.progresswindow.kprogresshud.KProgressHUD;


public abstract class BaseFragment<V extends BaseViewInterface, P extends BaseParameters> extends Fragment {
    private ProgressDialog mProgressDialog;
    protected V view;
    protected P parameters;
    private Activity activity;
    private Toast toast;
    protected Handler handler = new Handler();

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = getViewInstance();

        this.parameters = getParametersContainer();

        if (this.parameters != null) {
            this.parameters.bind(this);
        }

        return this.view.inflate(inflater, container);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // no call for super(). Bug on API Level > 11.
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();

        BusHelper.register(this);

    }

    protected abstract void initialize();

    protected abstract V getViewInstance();

    protected abstract P getParametersContainer();

    protected void finish() {
        getActivity().finish();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusHelper.unregister(this);
    }

    @Override
    public Context getContext() {
        return activity;
    }

    private boolean hasParameter(String key) {
        return ((getArguments() != null) && getArguments().containsKey(key));
    }

    public void setParameters(P param) {
        Bundle args = param.bundle();
        setArguments(args);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, T defaultValue) {
        if (hasParameter(key)) {
            return (T) getArguments().get(key);
        } else {
            return defaultValue;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T getParent(Class<T> parentClass) {
        Fragment parentFragment = getParentFragment();

        if (parentClass.isInstance(parentFragment)) {
            return (T) parentFragment;
        } else if (parentClass.isInstance(getActivity())) {
            return (T) getActivity();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected void addFragment(BaseFragment<?, ?> fragment, boolean addToBackStack) {
        BaseFragmentActivity<?, ?, ?> baseFragmentActivity = getParent(BaseFragmentActivity.class);

        if (baseFragmentActivity != null) {
            baseFragmentActivity.addFragment(fragment, addToBackStack);
        }
    }

    private KProgressHUD hud;

    public void showProgress() {
        showProgress("");
    }

    public void showProgress(@StringRes int resId) {
        showProgress(getString(resId));
    }

    public void showProgress(String message) {
        showProgress(message, true);
    }

    public synchronized void showProgress(String message, boolean timeout) {
        if (hud != null && hud.isShowing()) {

            hud.dismiss();
            hud = null;
        }

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (!TextUtils.isEmpty(message)) {
            hud.setLabel(message);
            hud.setCancellable(false);
        }

        hud.show();

        if (timeout) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (hud != null && hud.isShowing()) {

                        hud.dismiss();
                        hud = null;
                    }
                }
            }, 10000);
        }
    }

    public synchronized void dismissProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (hud != null)
                    hud.dismiss();
            }
        });
    }


    public void showSnackbar(@StringRes int resId) {
        showSnackbar(getString(resId));
    }

    public void showSnackbar(String message) {
        showSnackbar(message, false);
    }

    public void showSnackbar(String message, boolean warning) {
        showSnackbar(message, warning, null, null);
    }

    public void showSnackbar(ErrorApiResponse error) {
        showSnackbar(error.message, true);
    }

    public void showSnackbar(ErrorApiResponse error, OnClickListener actionCallback) {
        showSnackbar(error.message, true, getString(R.string.try_againt), actionCallback);
    }

    public void showSnackbar(ApiRequest.RequestError requestError) {
        showSnackbar(getErrorString(requestError), true);
    }

    public void showSnackbar(ApiRequest.RequestError requestError, OnClickListener actionCallback) {
        showSnackbar(getErrorString(requestError), true, getString(R.string.try_againt), actionCallback);
    }

    public void showSnackbar(@StringRes int resId, boolean warning) {
        showSnackbar(getString(resId), warning);
    }

    public void showSnackbar(String message, boolean warning, String action, OnClickListener actionCallback) {
        Snackbar snackbar = Snackbar.make(view.getView(), message, Snackbar.LENGTH_SHORT);
        if (warning) {
            if (!TextUtils.isEmpty(action) && actionCallback != null) {
                snackbar.setAction(action, actionCallback);
                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
            }

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
        }
        snackbar.show();
    }

    public void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        if (toast.getView().getWindowVisibility() == View.VISIBLE) {
            toast.cancel();
        }
        toast.show();
    }

    protected String getErrorString(ApiRequest.RequestError requestError) {
        switch (requestError) {
            case ERROR_NETWORK_CANCELLED:
            case ERROR_NETWORK_NO_CONNECTION:
                return getString(R.string.error_connect_internet);
            case ERROR_NETWORK_TIMEOUT:
                return getString(R.string.error_connect_timeout);
            default:
                return getString(R.string.error_other);
        }
    }
}