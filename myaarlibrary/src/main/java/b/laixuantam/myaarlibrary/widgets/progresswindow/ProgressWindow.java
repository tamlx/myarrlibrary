package b.laixuantam.myaarlibrary.widgets.progresswindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.progresswindow.mkloader.MKLoader;

public class ProgressWindow {
    private static ProgressWindow instance = null;
    private Context mContext;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View progressLayout;
    private boolean isAttached;

    /**
     * Private constructor to single-tone class
     *
     * @param context caller context
     */
    private ProgressWindow(Context context) {
        mContext = context;
        setupView();
    }

    /**
     * Static method to avoid multi instance from dialog
     *
     * @param context caller context
     * @return dialog reference
     */
    public static ProgressWindow getInstance(Context context) {

        synchronized (ProgressWindow.class) {

            if (instance == null) {
                instance = new ProgressWindow(context);
            }
        }

        return instance;
    }

    private ProgressBar mainProgress;
    private LinearLayout mainLayout;
    private MKLoader mkLoaderClassicSpinner;
    private MKLoader mkLoaderLineSpinner;
    private MKLoader mkLoaderFishSpinner;
    private TextView tvProgressTitle;


    /**
     * Function to setup component view
     */
    private void setupView() {

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        progressLayout = LayoutInflater.from(mContext).inflate(R.layout.view_progress_window, null);

        progressLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                isAttached = true;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                isAttached = false;
            }
        });

        mkLoaderClassicSpinner = progressLayout.findViewById(R.id.mkLoaderClassicSpinner);
        mkLoaderLineSpinner = progressLayout.findViewById(R.id.mkLoaderLineSpinner);
        mkLoaderFishSpinner = progressLayout.findViewById(R.id.mkLoaderFishSpinner);

        mainProgress = progressLayout.findViewById(R.id.pb_main_progress);

        tvProgressTitle = progressLayout.findViewById(R.id.tvLoadingTitle);
        mainProgress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.progressColor),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        tvProgressTitle.setTextColor(ContextCompat.getColor(mContext, R.color.progressColorTextTitle));

        mainLayout = progressLayout.findViewById(R.id.ll_main_layout);
        mainLayout.setBackgroundColor(Color.TRANSPARENT);

        mainProgress.setVisibility(View.VISIBLE);
        mkLoaderClassicSpinner.setVisibility(View.GONE);
        mkLoaderLineSpinner.setVisibility(View.GONE);
        mkLoaderFishSpinner.setVisibility(View.GONE);

        layoutParams = new WindowManager.LayoutParams(
                metrics.widthPixels, metrics.heightPixels,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.CENTER;
    }


    /**
     * Function to update component configuration
     *
     * @param progressWindowConfiguration progress configuration
     */
    public void setConfiguration(ProgressWindowConfiguration progressWindowConfiguration) {

        if (progressWindowConfiguration == null) {
            return;
        }

        if (progressWindowConfiguration.progressColor > 0) {
            mainProgress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, progressWindowConfiguration.progressColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY);

        }
        if (progressWindowConfiguration.backgroundColor > 0) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(mContext, progressWindowConfiguration.backgroundColor));
        }
        if (!TextUtils.isEmpty(progressWindowConfiguration.title)) {
            tvProgressTitle.setText(progressWindowConfiguration.title);
        }

        if (progressWindowConfiguration.titleColor > 0) {
            tvProgressTitle.setTextColor(ContextCompat.getColor(mContext, progressWindowConfiguration.titleColor));
        }

        switch (progressWindowConfiguration.type) {

            case ClassicSpinner:
                mkLoaderClassicSpinner.setVisibility(View.VISIBLE);
                mainProgress.setVisibility(View.GONE);
                mkLoaderLineSpinner.setVisibility(View.GONE);
                mkLoaderFishSpinner.setVisibility(View.GONE);
                break;

            case FishSpinner:
                mkLoaderClassicSpinner.setVisibility(View.GONE);
                mainProgress.setVisibility(View.GONE);
                mkLoaderLineSpinner.setVisibility(View.GONE);
                mkLoaderFishSpinner.setVisibility(View.VISIBLE);
                break;

            case LineSpinner:
                mkLoaderClassicSpinner.setVisibility(View.GONE);
                mainProgress.setVisibility(View.GONE);
                mkLoaderLineSpinner.setVisibility(View.VISIBLE);
                mkLoaderFishSpinner.setVisibility(View.GONE);
                break;

            default:
                mkLoaderClassicSpinner.setVisibility(View.GONE);
                mainProgress.setVisibility(View.VISIBLE);
                mkLoaderLineSpinner.setVisibility(View.GONE);
                mkLoaderFishSpinner.setVisibility(View.GONE);
                break;
        }


    }


    /**
     * Function to show progress
     */
    public void showProgress() {
        windowManager.addView(progressLayout, layoutParams);
    }

    public void showProgress(String title) {

        tvProgressTitle.setText(title);

        windowManager.addView(progressLayout, layoutParams);
    }

    /**
     * Function to hide progress
     */
    public void hideProgress() {
        if (isAttached) {
            windowManager.removeViewImmediate(progressLayout);
        }
    }


}
