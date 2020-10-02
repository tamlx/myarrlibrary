package b.laixuantam.myaarlibrary.widgets.touch_view_anim.pushdownanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.lang.annotation.Retention;
import java.lang.ref.WeakReference;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class PushDownAnim implements PushDown{
    @Retention( SOURCE )
    @IntDef( {MODE_SCALE, MODE_STATIC_DP} )
    public @interface Mode{
    }

    public static final float DEFAULT_PUSH_SCALE = 0.97f;
    public static final float DEFAULT_PUSH_STATIC = 2;
    public static final long DEFAULT_PUSH_DURATION = 50;
    public static final long DEFAULT_RELEASE_DURATION = 125;
    public static final int MODE_SCALE = 0;
    public static final int MODE_STATIC_DP = 1;
    public static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR
            = new AccelerateDecelerateInterpolator();

    private final float defaultScale;
    private int mode = MODE_SCALE;
    private float pushScale = DEFAULT_PUSH_SCALE;
    private float pushStatic = DEFAULT_PUSH_STATIC;
    private long durationPush = DEFAULT_PUSH_DURATION;
    private long durationRelease = DEFAULT_RELEASE_DURATION;
    private AccelerateDecelerateInterpolator interpolatorPush = DEFAULT_INTERPOLATOR;
    private AccelerateDecelerateInterpolator interpolatorRelease = DEFAULT_INTERPOLATOR;
    private WeakReference<View> weakView;
    private AnimatorSet scaleAnimSet;

    private PushDownAnim( final View view ){
        this.weakView = new WeakReference<>(view) ;
        this.weakView.get().setClickable( true );
        defaultScale = view.getScaleX();
    }


    public static PushDownAnim setPushDownAnimTo( final View view ){
        PushDownAnim pushAnim = new PushDownAnim( view );
        pushAnim.setOnTouchEvent( null );
        return pushAnim;
    }

    public static PushDownAnimList setPushDownAnimTo( View... views ){
        return new PushDownAnimList( views );
    }

    @Override
    public PushDown setScale( float scale ){
        if( this.mode == MODE_SCALE ){
            this.pushScale = scale;
        }else if( this.mode == MODE_STATIC_DP ){
            this.pushStatic = scale;
        }
        return this;
    }

    @Override
    public PushDown setScale( @Mode int mode, float scale ){
        this.mode = mode;
        this.setScale( scale );
        return this;
    }

    @Override
    public PushDown setDurationPush( long duration ){
        this.durationPush = duration;
        return this;
    }

    @Override
    public PushDown setDurationRelease( long duration ){
        this.durationRelease = duration;
        return this;
    }

    @Override
    public PushDown setInterpolatorPush( AccelerateDecelerateInterpolator interpolatorPush ){
        this.interpolatorPush = interpolatorPush;
        return this;
    }

    @Override
    public PushDown setInterpolatorRelease( AccelerateDecelerateInterpolator interpolatorRelease ){
        this.interpolatorRelease = interpolatorRelease;
        return this;
    }

    @Override
    public PushDown setOnClickListener( View.OnClickListener clickListener ){
        if( weakView.get() != null ){
            weakView.get().setOnClickListener( clickListener );
        }
        return this;
    }

    @Override
    public PushDown setOnLongClickListener( View.OnLongClickListener clickListener ){
        if( weakView.get() != null ){
            weakView.get().setOnLongClickListener( clickListener );
        }
        return this;
    }

    @Override
    public PushDown setOnTouchEvent( final View.OnTouchListener eventListener ){
        if( weakView.get() != null ){
            if( eventListener == null ){
                weakView.get().setOnTouchListener( new View.OnTouchListener(){
                    boolean isOutSide;
                    Rect rect;

                    @Override
                    public boolean onTouch( View view, MotionEvent motionEvent ){
                        if( view.isClickable() ){
                            int i = motionEvent.getAction();
                            if( i == MotionEvent.ACTION_DOWN ){
                                isOutSide = false;
                                rect = new Rect(
                                        view.getLeft(),
                                        view.getTop(),
                                        view.getRight(),
                                        view.getBottom() );
                                makeDecisionAnimScale( view,
                                        mode,
                                        pushScale,
                                        pushStatic,
                                        durationPush,
                                        interpolatorPush,
                                        i );
                            }else if( i == MotionEvent.ACTION_MOVE ){
                                if( rect != null
                                        && !isOutSide
                                        && !rect.contains(
                                        view.getLeft() + (int) motionEvent.getX(),
                                        view.getTop() + (int) motionEvent.getY() ) ){
                                    isOutSide = true;
                                    makeDecisionAnimScale( view,
                                            mode,
                                            defaultScale,
                                            0,
                                            durationRelease,
                                            interpolatorRelease,
                                            i );
                                }
                            }else if( i == MotionEvent.ACTION_CANCEL
                                    || i == MotionEvent.ACTION_UP ){
                                makeDecisionAnimScale( view,
                                        mode,
                                        defaultScale,
                                        0,
                                        durationRelease,
                                        interpolatorRelease,
                                        i );
                            }
                        }
                        return false;
                    }
                } );

            }else{
                weakView.get().setOnTouchListener( new View.OnTouchListener(){

                    @Override
                    public boolean onTouch( View v, MotionEvent motionEvent ){
                        return eventListener.onTouch( weakView.get(), motionEvent );
                    }
                } );
            }
        }

        return this;
    }

    /* =========================== Private method =============================================== */
    private void makeDecisionAnimScale( final View view,
                                        @Mode int mode,
                                        float pushScale,
                                        float pushStatic,
                                        long duration,
                                        TimeInterpolator interpolator,
                                        int action ){
        float tmpScale = pushScale;
        if( mode == MODE_STATIC_DP ){
            tmpScale = getScaleFromStaticSize( pushStatic );
        }
        animScale( view, tmpScale, duration, interpolator );
    }

    private void animScale( final View view,
                            float scale,
                            long duration,
                            TimeInterpolator interpolator ){

        view.animate().cancel();
        if( scaleAnimSet != null ){
            scaleAnimSet.cancel();
        }

        ObjectAnimator scaleX = ObjectAnimator.ofFloat( view, "scaleX", scale );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat( view, "scaleY", scale );
        scaleX.setInterpolator( interpolator );
        scaleX.setDuration( duration );
        scaleY.setInterpolator( interpolator );
        scaleY.setDuration( duration );

        scaleAnimSet = new AnimatorSet();
        scaleAnimSet
                .play( scaleX )
                .with( scaleY );
        scaleX.addListener( new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart( Animator animation ){
                super.onAnimationStart( animation );
            }

            @Override
            public void onAnimationEnd( Animator animation ){
                super.onAnimationEnd( animation );
            }
        } );
        scaleX.addUpdateListener( new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate( ValueAnimator valueAnimator ){
                View p = (View) view.getParent();
                if( p != null ) p.invalidate();
            }
        } );
        scaleAnimSet.start();
    }

    private float getScaleFromStaticSize( float sizeStaticDp ){
        if( sizeStaticDp <= 0 ) return defaultScale;

        float sizePx = dpToPx( sizeStaticDp );
        if( getViewWidth() > getViewHeight() ){
            if( sizePx > getViewWidth() ) return 1.0f;
            float pushWidth = getViewWidth() - ( sizePx * 2 );
            return pushWidth / getViewWidth();
        }else{
            if( sizePx > getViewHeight() ) return 1.0f;
            float pushHeight = getViewHeight() - ( sizePx * 2 );
            return pushHeight / (float) getViewHeight();
        }
    }

    private int getViewHeight(){
        return weakView.get().getMeasuredHeight();
    }

    private int getViewWidth(){
        return weakView.get().getMeasuredWidth();
    }

    private float dpToPx( final float dp ){
        return TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, weakView.get().getResources().getDisplayMetrics() );
    }
}

/**

 public class MainActivity extends AppCompatActivity{

 private Button button1;
 private Button button2;
 private View music;
 private ViewGroup container;

 @Override protected void onCreate( Bundle savedInstanceState ){
 super.onCreate( savedInstanceState );
 setContentView( R.layout.activity_main );

 container = findViewById(R.id.container);
 button1 = findViewById( R.id.button1 );
 button2 = findViewById( R.id.button2 );


 music = findViewById( R.id.music );

 // music.setEnabled( false ); // if you want to disable click
 //        PushDownAnim.setPushDownAnimTo( music )
 //                .setScale( PushDownAnim.MODE_STATIC_DP, 2 )
 //                .setOnClickListener( getClickListener() )
 //                .setOnLongClickListener( getLongClickListener() );
 //
 //        PushDownAnim.setPushDownAnimTo( button1 )
 //                .setOnClickListener( getClickListener() )
 //                .setOnLongClickListener( getLongClickListener() );
 //
 //        PushDownAnim.setPushDownAnimTo( button2 )
 //                .setOnClickListener( getClickListener() )
 //                .setOnLongClickListener( getLongClickListener() );

 //        [equal]
 //        PushDownAnim.setPushDownAnimTo( button )
 //                .setScale( PushDownAnim.MODE_SCALE , PushDownAnim.DEFAULT_PUSH_SCALE )
 //                .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
 //                .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
 //                .setInterpolatorPush( PushDownAnim.DEFAULT_INTERPOLATOR )
 //                .setInterpolatorRelease( PushDownAnim.DEFAULT_INTERPOLATOR
 //                .setOnClickListener( getClickListener() )
 //                .setOnLongClickListener( getLongClickListener() );
 //        [equal]
 PushDownAnim.setPushDownAnimTo( music, button1, button2 )
 .setScale( PushDownAnim.MODE_STATIC_DP , 2 )
 .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
 .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
 .setInterpolatorPush( PushDownAnim.DEFAULT_INTERPOLATOR )
 .setInterpolatorRelease( PushDownAnim.DEFAULT_INTERPOLATOR )
 .setOnClickListener( getClickListener() )
 .setOnLongClickListener( getLongClickListener() );


 // delay show view to check PushDownAnim.MODE_STATIC_DP not calculate size of view is zero.
 button2.setVisibility(View.GONE);
 final Handler handler = new Handler();
 handler.postDelayed(new Runnable() {
 @Override public void run() {
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
 TransitionSet set = new TransitionSet()
 .addTransition(new Scale())
 .addTransition(new Fade())
 .setDuration(200)
 .setInterpolator(new OvershootInterpolator(1.0f));
 TransitionManager.beginDelayedTransition(container, set);
 TransitionManager.beginDelayedTransition(container);
 button2.setVisibility(View.VISIBLE);
 }
 }
 }, 1000);


 }

 @NonNull private View.OnClickListener getClickListener(){
 return new View.OnClickListener(){
     @Override public void onClick( View view ){
         if( view == music ){
         Toast.makeText( MainActivity.this, "PUSH DOWN 1 !!", Toast.LENGTH_SHORT ).show();
         }else if( view == button1 ){
         Toast.makeText( MainActivity.this, "PUSH DOWN 2 !!", Toast.LENGTH_SHORT ).show();
         }else if( view == button2 ){
         Toast.makeText( MainActivity.this, "PUSH DOWN 3 !!", Toast.LENGTH_SHORT ).show();
         }
         }

         };
 }

 @NonNull private View.OnLongClickListener getLongClickListener(){
 return new View.OnLongClickListener(){
 @Override public boolean onLongClick( View view ){
 if( view == music ){
 Toast.makeText( MainActivity.this, "LONG PUSH DOWN 1 !!", Toast.LENGTH_SHORT ).show();
 }else if( view == button1 ){
 Toast.makeText( MainActivity.this, "LONG PUSH DOWN 2 !!", Toast.LENGTH_SHORT ).show();
 }else if( view == button2 ){
 Toast.makeText( MainActivity.this, "LONG PUSH DOWN 3 !!", Toast.LENGTH_SHORT ).show();
 }
 return true; // true: not effect to single click
 }
 };
 }

 }

 */
