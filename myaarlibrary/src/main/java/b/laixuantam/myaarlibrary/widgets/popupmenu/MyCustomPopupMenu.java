package b.laixuantam.myaarlibrary.widgets.popupmenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.R;

public class MyCustomPopupMenu extends PopupWindows implements OnDismissListener {
    private View mRootView;
    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private LayoutInflater mInflater;
    private ViewGroup mTrack;

    private Animation mTrackAnim;

    private ScrollView mScroller;
    private OnActionItemClickListener mItemClickListener;
    private OnDismissListener mDismissListener;

    private List<ActionItem> actionItems = new ArrayList<ActionItem>();

    private boolean mDidAction;
    private boolean mAnimateTrack;

    private int mChildPos;
    private int mInsertPos;
    private int mAnimStyle;
    private int rootWidth = 0;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_AUTO = 4;

    public static final int ANIM_ITEM_TYPE_FLOAT = 1;
    public static final int ANIM_ITEM_TYPE_SLIDE_IN_RIGHT = 2;
    public static final int ANIM_ITEM_TYPE_FADE_IN = 3;

    /**
     * Constructor allowing orientation override
     *
     * @param context Context
     */
    public MyCustomPopupMenu(Context context) {
        super(context);

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setAnmItemType(ANIM_ITEM_TYPE_FADE_IN);

        setRootViewId(R.layout.popup_vertical);

        mAnimStyle = ANIM_AUTO;
        mAnimateTrack = true;
        mChildPos = 0;
    }

    private int mCustomBackground;
    private int mCustomArrowColor;

    public MyCustomPopupMenu(Context context, int background, int arrow_color) {
        super(context);
        this.mCustomBackground = background;
        this.mCustomArrowColor = arrow_color;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setAnmItemType(ANIM_ITEM_TYPE_FADE_IN);


        setRootViewId(R.layout.popup_vertical);

        mAnimStyle = ANIM_AUTO;
        mAnimateTrack = true;
        mChildPos = 0;
    }

    /**
     * LayoutItemAnimation
     */
    public void setAnmItemType(int anmItemType) {
        switch (anmItemType) {
            case ANIM_ITEM_TYPE_FLOAT:
                mTrackAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 6f, Animation.RELATIVE_TO_SELF, 0);
                mTrackAnim.setInterpolator(new DecelerateInterpolator());
                mTrackAnim.setDuration(350);
                mTrackAnim.setStartOffset(150);

                break;

            case ANIM_ITEM_TYPE_SLIDE_IN_RIGHT:
                mTrackAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0);
                mTrackAnim.setInterpolator(new DecelerateInterpolator());
                mTrackAnim.setDuration(550);

                break;
            case ANIM_ITEM_TYPE_FADE_IN:
                mTrackAnim = new AlphaAnimation(0, 1);
                mTrackAnim.setInterpolator(new DecelerateInterpolator()); //add this
                mTrackAnim.setDuration(700);
                mTrackAnim.setStartOffset(350);
                break;

        }
    }

    /**
     * Get action item at an index
     *
     * @param index Index of item (position from callback)
     * @return Action Item at the position
     */
    public ActionItem getActionItem(int index) {
        return actionItems.get(index);
    }

    /**
     * Set root view.
     *
     * @param id Layout resource id
     */
    public void setRootViewId(int id) {
        mRootView = (ViewGroup) mInflater.inflate(id, null);
        mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);

        mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
        mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

        mScroller = (ScrollView) mRootView.findViewById(R.id.scroller);

        // This was previously defined on show() method, moved here to prevent
        // force close that occured
        // when tapping fastly on a view to show quickaction dialog.
        // Thanx to zammbi (github.com/zammbi)
        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        if (Build.VERSION.SDK_INT >= 21) {
            if (mCustomBackground != 0) {
                int[][] states = new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_pressed, -android.R.attr.state_focused},
                        new int[]{}
                };
                int[] colors = new int[]{
                        mCustomBackground,
                        mCustomBackground,
                        mCustomBackground
                };

                ColorStateList list = new ColorStateList(states, colors);
                mScroller.setBackgroundTintList(list);

            }

            if (mCustomArrowColor != 0) {
                int[][] states = new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_pressed, -android.R.attr.state_focused},
                        new int[]{}
                };
                int[] colors = new int[]{
                        mCustomArrowColor,
                        mCustomArrowColor,
                        mCustomArrowColor
                };

                ColorStateList list = new ColorStateList(states, colors);
                mArrowDown.setImageTintList(list);
                mArrowUp.setImageTintList(list);
            }
        }

        setContentView(mRootView);
    }

    /**
     * Set animation style
     *
     * @param mAnimStyle animation style, default is set to ANIM_AUTO
     */
    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    /**
     * Animate track.
     *
     * @param mAnimateTrack flag to animate track
     */
    public void mAnimateTrack(boolean mAnimateTrack) {
        this.mAnimateTrack = mAnimateTrack;
    }

    /**
     * Set listener for action item clicked.
     *
     * @param listener Listener
     */
    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * Add action item
     *
     * @param action {@link ActionItem}
     */
    public void addActionItem(ActionItem action) {
        actionItems.add(action);

        String title = action.getTitle();
        Drawable icon = action.getIcon();

        View container;

        container = mInflater.inflate(R.layout.action_item_vertical, null);

        // Button btn_lang_vi = (Button)
        // container.findViewById(R.id.button_set_language_vi);
        // btn_lang_vi.setBackgroundResource(R.drawable.btn_language_vi_selector);
        ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
        TextView text = (TextView) container.findViewById(R.id.tv_title);

        if (icon != null) {
            img.setImageDrawable(icon);
        } else {
            img.setVisibility(View.GONE);
        }

        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(View.GONE);
        }

        final int pos = mChildPos;
        final int actionId = action.getActionId();

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(MyCustomPopupMenu.this, pos,
                            actionId);
                }

                if (!getActionItem(pos).isSticky()) {
                    mDidAction = true;

                    dismiss();
                }
            }
        });

        container.setFocusable(true);
        container.setClickable(true);

        mTrack.addView(container, mInsertPos);

        mChildPos++;
        mInsertPos++;
    }

    public void addActionItem(ActionItem action, @LayoutRes int layout_item_custom) {
        actionItems.add(action);

        String title = action.getTitle();
        Drawable icon = action.getIcon();

        View container;

        if (layout_item_custom != 0) {
            container = mInflater.inflate(layout_item_custom, null);
        } else
            container = mInflater.inflate(R.layout.action_item_vertical, null);

        // Button btn_lang_vi = (Button)
        // container.findViewById(R.id.button_set_language_vi);
        // btn_lang_vi.setBackgroundResource(R.drawable.btn_language_vi_selector);
        ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
        TextView text = (TextView) container.findViewById(R.id.tv_title);

        if (icon != null) {
            img.setImageDrawable(icon);
        } else {
            img.setVisibility(View.GONE);
        }

        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(View.GONE);
        }

        final int pos = mChildPos;
        final int actionId = action.getActionId();

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(MyCustomPopupMenu.this, pos,
                            actionId);
                }

                if (!getActionItem(pos).isSticky()) {
                    mDidAction = true;

                    dismiss();
                }
            }
        });

        container.setFocusable(true);
        container.setClickable(true);

        mTrack.addView(container, mInsertPos);

        mChildPos++;
        mInsertPos++;
    }

    /**
     * Show quickaction popup. Popup is automatically positioned, on top or
     * bottom of anchor view.
     */
    public void show(View anchor) {
        preShow();

        int xPos, yPos, arrowPos;

        mDidAction = false;

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + anchor.getWidth(), location[1] + anchor.getHeight());

        // mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));

        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootHeight = mRootView.getMeasuredHeight();

        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

        // automatically get X coord of popup (top left)
        if ((anchorRect.left + rootWidth) > screenWidth) {
            xPos = anchorRect.left - (rootWidth - anchor.getWidth());
            xPos = (xPos < 0) ? 0 : xPos;

            arrowPos = anchorRect.centerX() - xPos;

        } else {
            if (anchor.getWidth() > rootWidth) {
                xPos = anchorRect.centerX() - (rootWidth / 2);
            } else {
                xPos = anchorRect.left;
            }

            arrowPos = anchorRect.centerX() - xPos;
        }

        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom) ? true : false;

        if (onTop) {
            if (rootHeight > dyTop) {
                yPos = 15;
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyTop - anchor.getHeight();
            } else {
                yPos = anchorRect.top - rootHeight;
            }
        } else {
            yPos = anchorRect.bottom;

            if (rootHeight > dyBottom) {
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyBottom;
            }
        }

        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);

        if (mAnimateTrack)
            mTrack.startAnimation(mTrackAnim);

    }

    /**
     * Set animation style
     *
     * @param screenWidth screen width
     * @param requestedX  distance from left edge
     * @param onTop       flag to indicate where the popup should be displayed. Set TRUE
     *                    if displayed on top of anchor view and vice versa
     */
    private void setAnimationStyle(int screenWidth, int requestedX, boolean
            onTop) {
        int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;

        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left :
                        R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right :
                        R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center :
                        R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_AUTO:
                if (arrowPos <= screenWidth / 4) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left :
                            R.style.Animations_PopDownMenu_Left);
                } else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center :
                            R.style.Animations_PopDownMenu_Center);
                } else {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right :
                            R.style.Animations_PopDownMenu_Right);
                }

                break;
        }
    }

    /**
     * Show arrow
     *
     * @param whichArrow arrow type resource id
     * @param requestedX distance from left screen
     */
    private void showArrow(int whichArrow, int requestedX) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
                : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
                : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
                .getLayoutParams();

        param.leftMargin = requestedX - arrowWidth / 2;

        hideArrow.setVisibility(View.INVISIBLE);
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if
     * the quicakction dialog is dismissed by clicking outside the dialog or
     * clicking on sticky item.
     */
    public void setOnDismissListener(OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    /**
     * Listener for item click
     */
    public interface OnActionItemClickListener {
        public abstract void onItemClick(MyCustomPopupMenu source, int pos,
                                         int actionId);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnDismissListener {
        public abstract void onDismiss();
    }
}

/*
    //intit
    ActionItem menu_setting = new ActionItem(0, getString(R.string.menu_setting), getContext().getDrawable(R.drawable.ic_setting_light));
        ActionItem menu_rate = new ActionItem(1, getString(R.string.menu_rate), null);
        ActionItem menu_share_fb = new ActionItem(2, getString(R.string.menu_share_fb), null);

        ActionItem menu_share_google = new ActionItem(3, getString(R.string.menu_share_google), null);

        ActionItem menu_remove_ads = new ActionItem(4, getString(R.string.menu_remove_ads), null);

        MyCustomPopupMenu quickAction = new MyCustomPopupMenu(getActivity());

        quickAction.addActionItem(menu_setting);
        quickAction.addActionItem(menu_rate);
        quickAction.addActionItem(menu_share_fb);
        quickAction.addActionItem(menu_share_google);
        quickAction.addActionItem(menu_remove_ads);

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
            switch (actionId) {
            case 0:
            break;
            }
            }
       });

       //call
        quickAction.show(v);

*/