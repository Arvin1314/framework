package cn.arvin.framework.core.image.base;

import android.graphics.Bitmap;
import android.view.View;


public abstract class SimpleImageLoadingListener implements ImageLoadingListener {
    @Override
    public void onLoadingStarted(String url, View view) {
        System.out.println("onLoadingStarted");
    }

    @Override
    public void onLoadingFailed(String url, View view, String reason) {
        System.out.println("onLoadingFailed reason---"+reason);
    }

    @Override
    public abstract void onLoadingComplete(String url, View view, Bitmap bitmap);

    @Override
    public void onLoadingCancelled(String url, View view) {
        System.out.println("onLoadingCancelled");
    }
}
