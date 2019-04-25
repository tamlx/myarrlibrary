package b.laixuantam.myaarlibrary.widgets.tutorial;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.helper.ScreenHelper;

/**
 * ------------- set up xml ----------------------
  <.widgets.TutorialView
  android:id="@+id/tutorial"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:visibility="gone" />

 * ------------- set up view ----------------------
 *
  @UiElement(R.id.tutorial) public TutorialView tutorialView;

  public void showTutorial(TutorialModel tutorial, TutorialListener listener)
  {
  ui.tutorialView.setListener(listener);
  ui.tutorialView.showTutorialDelay(tutorial);
  }

 * -------------- function call --------------------
  private void showMainTutorial() {
  TutorialModel model = new TutorialModel(R.id.button_menu, R.string.tutorial_menu, R.layout.view_tutorial_main_menu)
  model.setArrowBottom(false);
  view.showTutorial(model, new TutorialListener() {
  @Override public void onClose() {
  showBranchTutorial();
  }
  @Override public void onAction() {
  }
  });
  }

 */


public class TutorialView extends FrameLayout {
    private static final int DELAY = 500;
    private View contentView;
    private TextView contentText;
    private FrameLayout fakeComponent, fakeBottomComponent;
    private View arrow, arrowBottom;
    private TutorialListener listener;
    private Handler handler = new Handler();

    public TutorialView(Context context) {
        super(context);
        initView();
    }

    public TutorialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TutorialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.view_tutorial, null);
        this.setVisibility(View.GONE);
        contentView = view.findViewById(R.id.tutorial_content);
        contentText = (TextView) view.findViewById(R.id.tutorial_text);
        fakeComponent = (FrameLayout) view.findViewById(R.id.tutorial_fake_component);
        fakeBottomComponent = (FrameLayout) view.findViewById(R.id.tutorial_fake_bottom_component);
        arrow = view.findViewById(R.id.tutorial_arrow);
        arrowBottom = view.findViewById(R.id.tutorial_arrow_bottom);

        view.findViewById(R.id.tutorial_skip).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTutorial();
            }
        });
        addView(view);
    }

    public void setListener(TutorialListener listener) {
        this.listener = listener;
    }

    /**
     * Delay and show tutorial attach to component with content
     *
     * @param tutorial the tutorial
     */
    public void showTutorialDelay(final TutorialModel tutorial) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showTutorial(tutorial);
            }
        }, DELAY);
    }

    /**
     * Show tutorial attach to component with content
     *
     * @param tutorial the tutorial
     */
    public void showTutorial(TutorialModel tutorial) {

        ViewGroup frameLayout = (ViewGroup) getParent();
        final View v = frameLayout.findViewById(tutorial.getComponentAttached());
        if (v != null) {
            createTutorialView(tutorial, v);
        }
    }

    private void createTutorialView(TutorialModel tutorial, final View componentAttached) {
        // create fake view
        View fake = LayoutInflater.from(getContext()).inflate(tutorial.getFakeView(), null);
        LayoutParams lp = new LayoutParams(componentAttached.getWidth(), componentAttached.getHeight());
        fake.setLayoutParams(lp);
        fake.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE);
                if (listener != null) {
                    componentAttached.performClick();
                    listener.onAction();
                }
            }
        });
        if (!tutorial.isArrowBottom()) {
            fakeComponent.removeAllViews();
            fakeComponent.addView(fake);
        } else {
            fakeBottomComponent.removeAllViews();
            fakeBottomComponent.addView(fake);
        }

        // set text for fake view if fake view and component attached view is TextView
        if (componentAttached instanceof TextView && fake instanceof TextView) {
            ((TextView) fake).setText(((TextView) componentAttached).getText());
        }

        // set tutorial content
        contentText.setText(tutorial.getContent());

        int statusHeight = getStatusBarHeight();

        // get location of component attached
        int location[] = new int[2];
        componentAttached.getLocationInWindow(location);

        int top = location[1] - statusHeight;

        if (tutorial.isActionBar()) {
            top -= getActionbarHeight();
        }

        // set layout params
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) fakeComponent.getLayoutParams();
        RelativeLayout.LayoutParams lp_01 = (RelativeLayout.LayoutParams) fakeBottomComponent.getLayoutParams();
        lp1.setMargins(location[0], top, 0, 0);
        if (tutorial.getMarginBottom() > 0) {
            lp_01.setMargins(location[0], 0, 0, tutorial.getMarginBottom());
        } else
            lp_01.setMargins(location[0], 0, 0, 20);
        fakeComponent.setLayoutParams(lp1);
        fakeBottomComponent.setLayoutParams(lp_01);

        if (tutorial.isArrowBottom()) {
            arrow.setVisibility(View.GONE);
            arrowBottom.setVisibility(View.VISIBLE);
            fakeBottomComponent.setVisibility(View.VISIBLE);
            fakeComponent.setVisibility(View.GONE);
        } else {
            arrow.setVisibility(View.VISIBLE);
            arrowBottom.setVisibility(View.GONE);
            fakeBottomComponent.setVisibility(View.GONE);
            fakeComponent.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        if (tutorial.isRlt()) {
            ScreenHelper.ScreenSize screenSize = ScreenHelper.getScreenSize(getContext());
            int marginRight = screenSize.width - location[0] - componentAttached.getWidth();
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);

            if (!tutorial.isArrowBottom()) {
                lp2.setMargins(0, 0, marginRight + componentAttached.getWidth() / 4, 0);
                ((LinearLayout.LayoutParams) arrow.getLayoutParams()).gravity = Gravity.RIGHT;
            } else {
                lp2.setMargins(0, 0, marginRight + componentAttached.getWidth() / 4, 20);
                ((LinearLayout.LayoutParams) arrowBottom.getLayoutParams()).gravity = Gravity.RIGHT;
            }
        } else {
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            if (!tutorial.isArrowBottom()) {
                lp2.setMargins(location[0] + componentAttached.getWidth() / 4, 0, 0, 0);
                ((LinearLayout.LayoutParams) arrow.getLayoutParams()).gravity = Gravity.LEFT;
            } else {
                lp2.setMargins(location[0] + componentAttached.getWidth() / 4, 0, 0, 20);
                ((LinearLayout.LayoutParams) arrowBottom.getLayoutParams()).gravity = Gravity.LEFT;
            }
        }
        contentView.setLayoutParams(lp2);

        this.setVisibility(View.VISIBLE);

    }

    public void closeTutorial() {
        this.setVisibility(View.GONE);
        if (listener != null) {
            listener.onClose();
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private int getActionbarHeight() {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public interface TutorialListener {
        void onClose();

        void onAction();
    }

}
