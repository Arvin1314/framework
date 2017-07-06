package cn.arvin.framework.core.image.base;

import android.graphics.Bitmap;
import android.view.View;


public interface ImageLoadingListener {
    void onLoadingStarted(String url, View view);

    void onLoadingFailed(String url, View view, String reason);

    void onLoadingComplete(String url, View view, Bitmap bitmap);

    void onLoadingCancelled(String url, View view);
}
