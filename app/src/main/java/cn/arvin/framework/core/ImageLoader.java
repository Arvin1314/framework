package cn.arvin.framework.core;


import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.arvin.framework.core.image.base.BaseImageLoader;
import cn.arvin.framework.core.image.impl.DefaultImageLoader;
import cn.arvin.framework.core.image.listener.ImageLoadingListener;


public class ImageLoader {
    private ImageLoader() {
        throw new RuntimeException("ImageLoader cannot be instantiation");
    }

    private static BaseImageLoader sDefaultImageHandle = DefaultImageLoader.getInstance();

    public static void init(BaseImageLoader imageHandle) {
        sDefaultImageHandle = imageHandle;
    }

    public static void showImage(String imageUrl, ImageView imageView) {
        sDefaultImageHandle.showImage(imageUrl, imageView);
    }

    public static void showImage(String imageUrl, ImageView imageView, int defaultImage) {
        sDefaultImageHandle.showImage(imageUrl, imageView, defaultImage);
    }

    public static void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig) {
        sDefaultImageHandle.showImage(imageUrl, imageView, bitmapConfig);
    }

    public static void showImage(String imageUrl, ImageView imageView, int width, int height, int defaultImage) {
        sDefaultImageHandle.showImage(imageUrl, imageView, width, height, defaultImage);
    }

    public static void loadImage(String imageUrl, ImageView imageView, ImageLoadingListener listener) {
        sDefaultImageHandle.loadImage(imageUrl, imageView, listener);
    }

    public static void loadImage(String imageUrl, ImageLoadingListener listener) {
        sDefaultImageHandle.loadImage(imageUrl, listener);
    }
}
