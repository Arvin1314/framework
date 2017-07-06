package cn.arvin.framework.core.image.base;

import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.arvin.framework.core.image.listener.ImageLoadingListener;


public abstract class BaseImageLoader {
    public abstract void showImage(String imageUrl, ImageView imageView);

    public abstract void showImage(String imageUrl, ImageView imageView, int defaultImage);

    public abstract void showImage(String imageUrl, ImageView imageView, int width, int height, int defaultImage);

    public abstract void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig);

    public abstract void loadImage(String imageUrl, ImageView imageView, ImageLoadingListener listener);

    public abstract void loadImage(String imageUrl, ImageLoadingListener listener);

    protected Object convertListener(ImageLoadingListener listener) {
        return null;
    }
}
