package b.laixuantam.myaarlibrary.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import b.laixuantam.myaarlibrary.R;
import b.laixuantam.myaarlibrary.widgets.glide.CropCircleTransformation;

public class ImageHelper {
    private final Context context;

    public ImageHelper(Context context) {
        this.context = context;
    }

    @SuppressLint("ResourceType")
    public void displayImage(String imageLink, final ImageView imageView, final View loadingView, @DrawableRes int image_error_loading) {
        if (!TextUtils.isEmpty(imageLink)) {
            Glide.with(context)
                    .load(imageLink)
                    .placeholder(image_error_loading)
                    .error(image_error_loading)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (loadingView != null) {
                                loadingView.setVisibility(View.GONE);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (loadingView != null) {
                                loadingView.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    })
                    .priority(Priority.NORMAL)
                    .into(imageView);
        } else {
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
            if (imageView != null) {
                if (image_error_loading > 0) {
                    imageView.setBackgroundResource(image_error_loading);
                } else {
                    imageView.setBackgroundResource(R.color.white);
                }

            }
        }
    }

    @SuppressLint("ResourceType")
    public void displayImage(String imageLink, final ImageView imageView, final View loadingView, @DrawableRes int image_error_loading, boolean cropCircleTransformation) {
        if (!TextUtils.isEmpty(imageLink)) {
            if (loadingView != null) {
                loadingView.setVisibility(View.VISIBLE);
            }
            if (cropCircleTransformation) {
                Glide.with(context)
                        .load(imageLink)
                        .transform(new CropCircleTransformation(context))
                        .placeholder(image_error_loading).error(image_error_loading)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (loadingView != null) {
                                    loadingView.setVisibility(View.GONE);
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (loadingView != null) {
                                    loadingView.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .priority(Priority.NORMAL)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(imageLink)
                        .placeholder(image_error_loading).error(image_error_loading)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (loadingView != null) {
                                    loadingView.setVisibility(View.GONE);
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (loadingView != null) {
                                    loadingView.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .priority(Priority.NORMAL)
                        .into(imageView);
            }
        } else {
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
            if (imageView != null) {
                if (image_error_loading > 0) {
                    imageView.setImageResource(image_error_loading);
                } else {
                    imageView.setImageResource(R.drawable.no_image_full);
                }

            }
        }
    }


    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeholder)
                        .priority(Priority.NORMAL)
                        .centerCrop())
                .into(imageView);
    }

    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
                                 Uri uri) {
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeholder)
                        .priority(Priority.NORMAL)
                        .centerCrop())
                .into(imageView);
    }

    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.NORMAL)
                        .fitCenter())
                .into(imageView);
    }

    public void loadImage(Context context, int placeholder, ImageView imageView, String urlLink) {
        Glide.with(context)
                .load(urlLink)
                .apply(new RequestOptions()
                        .priority(Priority.NORMAL)
                        .placeholder(placeholder)
                        .fitCenter())
                .into(imageView);
    }

    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.NORMAL)
                        .fitCenter())
                .into(imageView);
    }
}