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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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


public abstract class BaseFragmentActivity<V extends BaseViewInterface, A extends BaseActionbarView, P extends BaseParameters> extends AppCompatActivity {

    public enum Animation {
        TRANSLATE_Y,
        SLIDE_IN_OUT
    }

    protected V view;
    protected A actionbar;
    protected P parameters;
    private ProgressDialog mProgressDialog;
    private Toast exitToast;
    private Toast toast;
    private boolean isInitBranch = false;
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

        // TODO Analytics

        AppProvider.getLanguageHelper().setup(getResources());

        setupView();

        if (sideTransitionEnabled()) {
            applySideTransitionOpen();
        }

        initialize(savedInstanceState);
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
        if (progressWindow == null){
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

        if (!TextUtils.isEmpty(title)) {
            progressWindowConfiguration.title = title;
        }
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

    protected void setupActionbar(ViewGroup container) {
        ActionBar actionBar = getSupportActionBar();
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
    }

    protected abstract V getViewInstance();

    protected abstract A getActionbarInstance();

    protected abstract P getParametersContainer();

    protected abstract int getFragmentContainerId();

    protected abstract void initialize(Bundle savedInstanceState);

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

        if (isFinishing()) {
            return;
        }

        if (!confirmOnBack()) {
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

    protected void setTitle(String subtitle) {
        if (actionbar != null) {
            actionbar.setTitle(subtitle);
        }
    }

    protected void setSubtitle(String subtitle) {
        if (actionbar != null) {
            actionbar.setSubtitle(subtitle);
        }
    }

    protected BaseFragment<?, ?> getCurrentFragment() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(getFragmentContainerId());

        if ((fragment != null) && fragment.isVisible()) {
            return fragment;
        }

        return null;
    }

    public void addFragment(BaseFragment<?, ?> fragment, boolean addToBackStack) {
        if (fragment != null && !fragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(getFragmentContainerId(), fragment);

            // skip add fragment to back stack if it is first fragment
            if (addToBackStack) {
                transaction.addToBackStack(null);
            } else {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            transaction.commitAllowingStateLoss();
        }
    }

    public void addFragment(BaseFragment<?, ?> fragment, boolean addToBackStack, Animation anim) {
        if (fragment != null && !fragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (anim == Animation.TRANSLATE_Y)
                transaction.setCustomAnimations(R.anim.translate_y_enter, R.anim.translate_y_exit, R.anim.translate_y_pop_enter, R.anim.translate_y_pop_exit);

            else
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);

            transaction.replace(getFragmentContainerId(), fragment);

            // skip add fragment to back stack if it is first fragment
            if (addToBackStack) {
                transaction.addToBackStack(null);
            } else {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            transaction.commitAllowingStateLoss();
        }
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

    public void showProgress(@StringRes int resId) {
        showProgress(getString(resId));
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
            if (!mProgressDialog.isAdded())
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

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    private void removeTagFragment(String tag) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

    }

    public synchronized void dismissProgress() {
        if (mProgressDialog != null && !isFinishing()) {
            mProgressDialog.dismissAllowingStateLoss();
            mProgressDialog = null;
            removeTagFragment("ProgressDialog");

        }
        if (progressRunnable != null) {
            handler.removeCallbacks(progressRunnable);
            progressRunnable = null;
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