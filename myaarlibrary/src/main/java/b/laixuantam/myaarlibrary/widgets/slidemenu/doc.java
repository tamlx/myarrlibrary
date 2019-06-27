package b.laixuantam.myaarlibrary.widgets.slidemenu;

public class doc {
    /**
     <?xml version="1.0" encoding="utf-8"?>
     <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:app="http://schemas.android.com/apk/res-auto"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:orientation="vertical">

     <ir.smartdevelop.eram.slidemenulayout.slidemenu.SlideMenuLayout
     android:id="@+id/mainSlideMenu"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:background="@color/white"
     app:allowDragging="false"
     app:contentAlpha="0.5"
     app:contentToggle="true"
     app:parallax="true"
     app:slideMode="both">

     <include layout="@layout/content_menu_left" />

     <include layout="@layout/content_menu_right" />

     <LinearLayout
     android:id="@+id/fm_slide_content"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:background="@color/white"
     android:orientation="vertical">

     </LinearLayout>

     </ir.smartdevelop.eram.slidemenulayout.slidemenu.SlideMenuLayout>

     </RelativeLayout>

     <activity
     android:name="ir.smartdevelop.eram.slidemenulayout.MainActivity2"
     android:theme="@style/AppThemeSlideMenuLayout"
     android:windowSoftInputMode="stateHidden">
     </activity>



     public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

     private String TAG = this.getClass().getSimpleName();
     //ui
     private SlideMenuLayout slideMenuLayout;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main2);
     //        initStatusBar();
     initView();
     }

     private void initView() {
     slideMenuLayout = (SlideMenuLayout) findViewById(R.id.mainSlideMenu);

     findViewById(R.id.fm_leftMenu).setOnClickListener(this);
     findViewById(R.id.fm_rightMenu).setOnClickListener(this);
     findViewById(R.id.btnRight).setOnClickListener(this);
     findViewById(R.id.btnLeft).setOnClickListener(this);
     slideMenuLayout.addOnSlideChangedListener(new OnSlideChangedListener() {
     @Override
     public void onSlideChanged(SlideMenuLayout slideMenu, boolean isLeftSlideOpen, boolean isRightSlideOpen) {
     Log.d(TAG, "onSlideChanged:isLeftSlideOpen=" + isLeftSlideOpen + ":isRightSlideOpen=" + isRightSlideOpen);
     }
     });
     }

     private void initStatusBar() {
     if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
     int flag_translucent_status = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
     getWindow().setFlags(flag_translucent_status, flag_translucent_status);
     }

     @Override
     public void onClick(View v) {
     switch (v.getId()) {

     case R.id.btnLeft:
     Toast.makeText(this, "click button Left", Toast.LENGTH_LONG).show();
     slideMenuLayout.closeLeftSlide();
     break;
     case R.id.btnRight:
     Toast.makeText(this, "click button right", Toast.LENGTH_LONG).show();
     slideMenuLayout.closeRightSlide();
     break;

     case R.id.fm_leftMenu:
     slideMenuLayout.toggleLeftSlide();
     break;
     case R.id.fm_rightMenu:
     slideMenuLayout.toggleRightSlide();
     break;
     }
     }

     @Override
     public void onBackPressed() {
     if (slideMenuLayout.isLeftSlideOpen() || slideMenuLayout.isRightSlideOpen()) {
     slideMenuLayout.closeLeftSlide();
     slideMenuLayout.closeRightSlide();
     } else {
     super.onBackPressed();
     }
     }
     }

     */

}
