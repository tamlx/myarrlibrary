package b.laixuantam.myaarlibrary.widgets.bottomnavigationbar;

public class example {
    /**

     =====================XML ===========================

     <BottomNavigationBar
     android:id="@+id/bnb_default"
     android:layout_width="match_parent"
     android:layout_height="0dp"
     android:layout_weight="1"
     android:background="#FFFFFF" />

    ====================== CLASS =======================

     private String mPackageName;

     mPackageName = getApplicationInfo().packageName;

     BottomNavigationBar mBnbDefault = (BottomNavigationBar) findViewById(R.id.bnb_default);

     =========================== initBnbDefault ==================
     BottomItem item1 = new BottomItem();
     item1.setText("Bag");
     //        item1.setIconResID(getResources().getIdentifier("home1x", "drawable", mPackageName));
     item1.setActiveBgResID(R.drawable.bg_bottom_navi_selected);
     item1.setInactiveBgResID(R.drawable.bg_bottom_navi_normal);
     mBnbDefault.addItem(item1);

    ============================ initBnbDrawableMode ===============
     BottomItem item1 = new BottomItem();
     item1.setMode(BottomItem.DRAWABLE_MODE);
     item1.setText("Camera");
     item1.setActiveIconResID(getResources().getIdentifier("ic_drawable_camera_fill", "drawable", mPackageName);
     item1.setInactiveIconResID(getResources().getIdentifier("ic_drawable_camera", "drawable", mPackageName);
     item1.setActiveTextColor(Color.parseColor("#E64B4E"));
     mBnbDefault.addItem(item1);

     ============================= initBnbMixMode ==================
     BottomItem item1 = new BottomItem();
     item1.setText("Bag");
     item1.setIconResID(getResources().getIdentifier("ic_tint_bag", "drawable", mPackageName));
     item1.setActiveIconColor(Color.parseColor("#E55D87"));
     item1.setInactiveIconColor(Color.parseColor("#5FC3E4"));
     item1.setActiveTextColor(Color.parseColor("#43CEA2"));
     item1.setInactiveTextColor(Color.parseColor("#D38312"));
     mBnbDefault.addItem(item1);


     ============================== config ===========================
     mBnbDefault.addOnSelectedListener(new BottomNavigationBar.OnSelectedListener() {
        @Override
        public void OnSelected(int oldPosition, int newPosition) {
            mBnbDefault.setText("Default Tint Mode : " + mBnbDefaultList.get(newPosition).getText());
        }
     });

     mBnbDefault.setSelectedPosition(0); //Set default selected item
     mBnbDefault.initialize();
     mBnbDefault.setBadgeNumber(0, 68);
     mBnbDefault.setBadgeText(0, "WOW");

     */

}
