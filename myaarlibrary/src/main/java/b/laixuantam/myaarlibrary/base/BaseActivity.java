package b.laixuantam.myaarlibrary.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.dependency.AppProvider;
import b.laixuantam.myaarlibrary.helper.BusHelper;
import b.laixuantam.myaarlibrary.widgets.dialog.ProgressDialog;
import b.laixuantam.myaarlibrary.widgets.progresswindow.ProgressWindow;
import b.laixuantam.myaarlibrary.widgets.progresswindow.ProgressWindowConfiguration;


public abstract class BaseActivity<V extends BaseViewInterface, A extends BaseActionbarView, P extends BaseParameters> extends AppCompatActivity {
    protected V view;
    protected A actionbar;
    protected P parameters;
    private ProgressDialog mProgressDialog;
    private Toast exitToast;
    private Toast toast;
    protected Handler handler = new Handler();
    private Runnable progressRunnable = null;

    private ProgressWindow progressWindow;
    private ProgressWindowConfiguration progressWindowConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.parameters = getParametersContainer();

        if (this.parameters != null) {
            this.parameters.bind(this);
        }

        AppProvider.getLanguageHelper().setup(getResources());

        setupView();

        if (sideTransitionEnabled()) {
            applySideTransitionOpen();
        }

        initialize();
        BusHelper.register(this);

        progressConfigurations();
    }

    /**
     * Function to set main configuration
     */
    private void progressConfigurations() {
        progressWindow = ProgressWindow.getInstance(this);
        progressWindowConfiguration = new ProgressWindowConfiguration();

    }

    /**
     * Function to show progress
     */
    public void showProgress2(ProgressWindowConfiguration.TYPE progressType) {
        if (progressWindow == null) {
            progressConfigurations();
        }
        showProgress2("", progressType);
    }

    public void showProgress2(String title, ProgressWindowConfiguration.TYPE progressType) {
        if (progressWindow == null) {
            progressConfigurations();
        }
        showProgress2(title, 0, progressType, 0);
    }

    @SuppressLint("ResourceType")
    public void showProgress2(String title, @ColorRes int titleColor, ProgressWindowConfiguration.TYPE progressType, @ColorRes int progressColor) {
        if (progressWindow == null) {
            progressConfigurations();
        }

        progressWindowConfiguration.title = title;
        if (titleColor > 0) {
            progressWindowConfiguration.titleColor = titleColor;
        }
        progressWindowConfiguration.type = progressType;

        if (progressColor > 0) {
            progressWindowConfiguration.progressColor = progressColor;
        }
        progressWindow.setConfiguration(progressWindowConfiguration);
        progressWindow.showProgress();
    }

    /**
     * Function to hide progress
     */
    public void hideProgress2() {
        if (progressWindow != null)
            progressWindow.hideProgress();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // no call for super(). Bug on API Level > 11.
    }

    private void applySideTransitionOpen() {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    private void applySideTransitionClose() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    protected boolean sideTransitionEnabled() {
        return false;
    }

    private void setupView() {
        this.view = getViewInstance();
        ViewGroup container = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        View layout = this.view.inflate(getLayoutInflater(), container);

        setContentView(layout);
        setupActionbar(container);
    }

    private void setupActionbar(ViewGroup container) {
        ActionBar actionBar = getSupportActionBar();

        if (enableActionbar()) {
            this.actionbar = getActionbarInstance();

            if ((actionBar != null) && (this.actionbar != null)) {
                View actionbarView = this.actionbar.inflate(getLayoutInflater(), container);
                actionBar.setCustomView(actionbarView);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                Toolbar parent = (Toolbar) actionbarView.getParent();
                parent.setContentInsetsAbsolute(0, 0);
                parent.setPadding(0, 0, 0, 0);
            }
        } else {
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    protected void setTitle(String subtitle) {
        if (actionbar != null) {
            actionbar.setTitle(subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract V getViewInstance();

    protected abstract A getActionbarInstance();

    protected abstract P getParametersContainer();

    protected void initialize() {
    }

    protected boolean enableActionbar() {
        return true;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getParameter(String key, T defaultValue) {
        return getParameter(getIntent(), key, defaultValue);
    }

    protected boolean confirmOnBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (sideTransitionEnabled()) {
            applySideTransitionClose();
        }

        if (confirmOnBack()) {
            if (exitToast == null) {
                exitToast = Toast.makeText(this, "Nhấn Back một lần nữa để thoát", Toast.LENGTH_SHORT);
            }
            if (exitToast.getView().getWindowVisibility() != View.VISIBLE) {
                exitToast.show();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusHelper.unregister(this);
    }


    private boolean hasParameters(Intent intent) {
        return (intent != null) && (intent.getExtras() != null);
    }

    private boolean hasParameter(Intent intent, String key) {
        return (hasParameters(intent) && intent.getExtras().containsKey(key));
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(Intent intent, String key, T defaultValue) {
        if (hasParameter(intent, key)) {
            return (T) intent.getExtras().get(key);
        } else {
            return defaultValue;
        }
    }

    public boolean isConnectInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void showProgress() {
        showProgress("");
    }

    public void showProgress(String message) {
        showProgress(message, false);
    }

    public synchronized void showProgress(String message, boolean timeout) {
        // Show progress dialog if it is null or not showing.
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.newInstance(message);
        }
        if (!mProgressDialog.isShowing() && !this.isFinishing()) {
            mProgressDialog.show(getSupportFragmentManager(), "ProgressDialog");
        }
        if (timeout) {
            if (progressRunnable != null) {
                handler.removeCallbacks(progressRunnable);
                progressRunnable = null;
            }
            progressRunnable = new Runnable() {
                @Override
                public void run() {
                    showSnackbar(ApiRequest.RequestError.ERROR_NETWORK_OTHER);
                    dismissProgress();
                }
            };
            handler.postDelayed(progressRunnable, 10000);
        }
    }

    public synchronized void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (progressRunnable != null) {
            handler.removeCallbacks(progressRunnable);
            progressRunnable = null;
        }
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
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
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