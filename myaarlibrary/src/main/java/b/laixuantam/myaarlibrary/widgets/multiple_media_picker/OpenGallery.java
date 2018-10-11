package b.laixuantam.myaarlibrary.widgets.multiple_media_picker;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.multiple_media_picker.Adapters.MediaAdapter;
import b.laixuantam.myaarlibrary.widgets.multiple_media_picker.Fragments.OneFragment;
import b.laixuantam.myaarlibrary.widgets.multiple_media_picker.Fragments.TwoFragment;

public class OpenGallery extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MediaAdapter mAdapter;
    private Toolbar toolbar;
    private List<String> mediaList = new ArrayList<>();
    public static List<Boolean> selected = new ArrayList<>();
    public static ArrayList<String> imagesSelected = new ArrayList<>();
    public static String parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open_gallery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.fillColorNavigation), PorterDuff.Mode.MULTIPLY);
        toolbar.setTitle(Gallery.title);
        if (imagesSelected == null) {
            imagesSelected = new ArrayList<>();
        }
        if (selected == null) {
            selected = new ArrayList<>();
        }
        if (imagesSelected.size() > 0) {
            if (Gallery.typeMedia == 1) {
                toolbar.setTitle(String.valueOf(imagesSelected.size()) + " [Hình ảnh] đã chọn");
            } else {
                toolbar.setTitle(String.valueOf(imagesSelected.size()) + " [Video] đã chọn");
            }
        } else {
            toolbar.setTitle(Gallery.title);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        parent = getIntent().getExtras().getString("FROM");
        mediaList.clear();
        selected.clear();
        if (parent.equals("Images")) {
            mediaList.addAll(OneFragment.imagesList);
            selected.addAll(OneFragment.selected);

        } else {
            mediaList.addAll(TwoFragment.videosList);
            selected.addAll(TwoFragment.selected);

        }

        toolbar.setTitleTextColor(getResources().getColor(R.color.fillColorNavigation));

        populateRecyclerView();
    }


    private void populateRecyclerView() {
        for (int i = 0; i < selected.size(); i++) {
            if (imagesSelected.contains(mediaList.get(i))) {
                selected.set(i, true);
            } else {
                selected.set(i, false);
            }
        }
        mAdapter = new MediaAdapter(mediaList, selected, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (!selected.get(position).equals(true) && imagesSelected.size() < Gallery.maxSelection) {
                    imagesSelected.add(mediaList.get(position));
                    selected.set(position, !selected.get(position));
                    mAdapter.notifyItemChanged(position);
                    if (parent.equals("Images")) {
                        toolbar.setTitle(String.valueOf(imagesSelected.size()) + " [Hình ảnh] đã chọn");
                        Gallery.typeMedia = 1;
                    } else {
                        toolbar.setTitle(String.valueOf(imagesSelected.size()) + " [Video] đã chọn");
                        Gallery.typeMedia = 2;
                    }
                } else if (selected.get(position).equals(true)) {
                    if (imagesSelected.indexOf(mediaList.get(position)) != -1) {
                        imagesSelected.remove(imagesSelected.indexOf(mediaList.get(position)));
                        selected.set(position, !selected.get(position));
                        mAdapter.notifyItemChanged(position);
                    }
                } else {
                    //todo truong hợp cho chọn muilti thì không xử lý function dưới đây
//                    Toast.makeText(getApplicationContext(), "Bạn phải bỏ chọn file trước khi thay đổi.", Toast.LENGTH_SHORT).show();

                    if (Gallery.maxSelection == 1) {

                        imagesSelected.clear();

                        for (int i = 0; i < selected.size(); i++) {
                            selected.set(i, false);
                        }

                        imagesSelected.add(mediaList.get(position));
                        selected.set(position, !selected.get(position));
//                        mAdapter.notifyItemChanged(position);
                        mAdapter.notifyDataSetChanged();
                        if (parent.equals("Images")) {
                            toolbar.setTitle(String.valueOf(imagesSelected.size()) + " [Hình ảnh] đã chọn");
                            Gallery.typeMedia = 1;
                        } else {
                            toolbar.setTitle(String.valueOf(imagesSelected.size()) + " [Video] đã chọn");
                            Gallery.typeMedia = 2;
                        }

                    } else {

                        if (parent.equals("Images")) {

                            String messAleart = String.valueOf(imagesSelected.size()) + " [Hình ảnh] đã chọn";
                            Toast.makeText(getApplicationContext(), messAleart, Toast.LENGTH_SHORT).show();
                        } else {
                            String messAleart = String.valueOf(imagesSelected.size()) + " [Video] đã chọn";
                            Toast.makeText(getApplicationContext(), messAleart, Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                Gallery.selectionTitle = imagesSelected.size();
                if (imagesSelected.size() != 0) {

                } else {
                    toolbar.setTitle(Gallery.title);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private OpenGallery.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OpenGallery.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}

