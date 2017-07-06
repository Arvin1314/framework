package cn.arvin.framework.core.image.impl;

import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.arvin.framework.core.image.base.BaseImageLoader;
import cn.arvin.framework.core.image.listener.ImageLoadingListener;

/**
 * Created by youkang-bd on 2017-04-20.
 */

public class DefaultImageLoader extends BaseImageLoader {

    static class InstanceHolder {
        private static final DefaultImageLoader INSTANCE = new DefaultImageLoader();
    }

    public static DefaultImageLoader getInstance() {
        return DefaultImageLoader.InstanceHolder.INSTANCE;
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView) {

    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, int defaultImage) {

    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, int width, int height, int defaultImage) {

    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig) {

    }

    @Override
    public void loadImage(String imageUrl, ImageView imageView, ImageLoadingListener listener) {

    }

    @Override
    public void loadImage(String imageUrl, ImageLoadingListener listener) {

    }
}
