package b.laixuantam.myaarlibrary.widgets.multiple_media_picker;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.multiple_media_picker.Fragments.OneFragment;
import b.laixuantam.myaarlibrary.widgets.multiple_media_picker.Fragments.TwoFragment;

public class Gallery extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    public static int selectionTitle;
    public static String title;
    public static int maxSelection;
    public static int mode;
    public static int typeMedia; //type = 1 is Selected Image, 2-Video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fillColorNavigation), PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnResult();
            }
        });

        title = getIntent().getExtras().getString("title");
        maxSelection = getIntent().getExtras().getInt("maxSelection");
        if (maxSelection == 0) maxSelection = Integer.MAX_VALUE;
        mode = getIntent().getExtras().getInt("mode");

        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.fillColorNavigation));
        selectionTitle = 0;

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (mode == 2 || mode == 3) {
            tabLayout.setVisibility(View.GONE);
        }

        if (OpenGallery.selected != null)
            OpenGallery.selected.clear();
        if (OpenGallery.imagesSelected != null)
            OpenGallery.imagesSelected.clear();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (selectionTitle > 0) {
            if (typeMedia == 1) {

                toolbar.setTitle(String.valueOf(selectionTitle) + " [Hình ảnh] đã chọn");
            } else {
                toolbar.setTitle(String.valueOf(selectionTitle) + " [Video] đã chọn");

            }
        } else {
            toolbar.setTitle(title);
        }
    }

    //This method set up the tab view for images and videos
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (mode == 1 || mode == 2) {
            adapter.addFragment(new OneFragment(), "Images");
        }
        if (mode == 1 || mode == 3)
            adapter.addFragment(new TwoFragment(), "Videos");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void returnResult() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra("result", OpenGallery.imagesSelected);

        setResult(RESULT_OK, returnIntent);
        finish();
    }


    @Override
    protected void onDestroy() {

        title = null;
        OpenGallery.selected = null;
        OpenGallery.imagesSelected = null;
        OpenGallery.parent = null;

        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
