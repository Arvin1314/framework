package cn.arvin.framework.core.image;


import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.arvin.framework.core.image.base.ImageHandle;
import cn.arvin.framework.core.image.base.ImageLoadingListener;


public class ImageLoader {

    private static ImageHandle sImageHandle;

    public static void setImageHandle(ImageHandle imageHandle) {
        sImageHandle = imageHandle;
    }

    public static void showImage(String imageUrl, ImageView imageView) {
        if (sImageHandle != null) {
            System.out.println("showImage---" + imageUrl);
            sImageHandle.showImage(imageUrl, imageView);
        }
    }

    public static void showImage(String imageUrl, ImageView imageView, int defaultImage) {
        if (sImageHandle != null) {
            sImageHandle.showImage(imageUrl, imageView, defaultImage);
        }
    }

    public static void showImage(String imageUrl, ImageView imageView, int width, int height, int defaultImage) {
        if (sImageHandle != null) {
            sImageHandle.showImage(imageUrl, imageView, width, height, defaultImage);
        }
    }

    public static void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig) {
        if (sImageHandle != null) {
            sImageHandle.showImage(imageUrl, imageView, bitmapConfig);
        }
    }

    public static void showImage(String imageUrl, ImageLoadingListener listener) {
        if (sImageHandle != null) {
            sImageHandle.showImage(imageUrl, listener);
        }
    }

    public static void showImage(String imageUrl, ImageView imageView, ImageLoadingListener listener) {
        if (sImageHandle != null) {
            sImageHandle.showImage(imageUrl, imageView, listener);
        }
    }


}
