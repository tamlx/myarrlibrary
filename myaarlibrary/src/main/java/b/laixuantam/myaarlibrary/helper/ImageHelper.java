package b.laixuantam.myaarlibrary.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
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
            if (loadingView != null) {
                loadingView.setVisibility(View.VISIBLE);
            }
            Glide.with(context).load(imageLink).asBitmap().transform(new CropCircleTransformation(context)).placeholder(R.color.white).error(R.color.white).listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    if (loadingView != null) {
                        loadingView.setVisibility(View.GONE);
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    if (loadingView != null) {
                        loadingView.setVisibility(View.GONE);
                    }
                    return false;
                }
            }).into(imageView);
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
                Glide.with(context).load(imageLink).asBitmap().transform(new CropCircleTransformation(context)).placeholder(image_error_loading).error(image_error_loading).listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(imageView);
            } else {
                Glide.with(context).load(imageLink).asBitmap().placeholder(image_error_loading).error(image_error_loading).listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(imageView);
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

}