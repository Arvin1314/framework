package cn.arvin.framework.core.image.base;

import android.graphics.Bitmap;
import android.widget.ImageView;


public interface ImageHandle {

     void showImage(String imageUrl, ImageView imageView);

     void showImage(String imageUrl, ImageView imageView, int defaultImage);

     void showImage(String imageUrl, ImageView imageView, int width, int height, int defaultImage);

     void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig);

     void showImage(String imageUrl, ImageLoadingListener listener);

     void showImage(String imageUrl, ImageView imageView, ImageLoadingListener listener);
}
