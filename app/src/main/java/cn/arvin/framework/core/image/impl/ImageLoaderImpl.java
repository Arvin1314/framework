package cn.arvin.framework.core.image.impl;


import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import cn.arvin.framework.application.SoftApplication;
import cn.arvin.framework.core.image.base.ImageHandle;
import cn.arvin.framework.core.image.base.ImageLoadingListener;


/**
 * 对UniversalImageLoader加载图片进行封装
 */
public class ImageLoaderImpl implements ImageHandle {
//    INSTANCE;

    private ImageLoader mUniversalImageLoader;

    public ImageLoaderImpl() {
        System.out.println("ImageLoaderImpl init");
        DisplayImageOptions options = createBuilder().build();

        ImageLoaderConfiguration config = createConfiguration(options);

        mUniversalImageLoader = ImageLoader.getInstance();
        mUniversalImageLoader.init(config);
    }


    @Override
    public void showImage(String imageUrl, ImageView imageView) {
        System.out.println("ImageLoaderImpl---" + imageUrl);
        mUniversalImageLoader.displayImage(imageUrl, imageView);
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, int defaultImage) {
        mUniversalImageLoader.displayImage(imageUrl, imageView, createDefaultImageOptions(defaultImage));
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, final int width, final int height, int defaultImage) {

        mUniversalImageLoader.displayImage(imageUrl, imageView, createDefaultImageOptions(defaultImage), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(loadedImage, width, height);
                ((ImageView) view).setImageBitmap(resizeBmp);
            }
        });
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig) {
        DisplayImageOptions options = createBuilder().bitmapConfig(bitmapConfig).build();
        mUniversalImageLoader.displayImage(imageUrl, imageView, options);
    }

    @Override
    public void showImage(String imageUrl, ImageLoadingListener listener) {
        mUniversalImageLoader.loadImage(imageUrl, varyImageLoadingListener(listener));
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, ImageLoadingListener listener) {
        mUniversalImageLoader.displayImage(imageUrl, imageView, varyImageLoadingListener(listener));
    }

    private DisplayImageOptions createDefaultImageOptions(int defaultImage) {
        return createBuilder().showImageOnLoading(defaultImage).build();
    }

    private DisplayImageOptions.Builder createBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showStubImage(0)
                .showImageOnLoading(0)// 设置图片下载期间显示的图片
                .showImageOnFail(0)// 设置图片加载或解码过程中发生错误显示的图
                .showImageForEmptyUri(0)// 设置图片Uri为空或是错误的时候显示的图
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .displayer(new SimpleBitmapDisplayer())
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true);// 保留Exif信息

        /*if (!TextUtils.isEmpty(mDiskCacheDirPath)) {
            builder.cacheOnDisk(true);
        }*/

        return builder;
    }

    private ImageLoaderConfiguration createConfiguration(DisplayImageOptions options) {
        // 使用最大可用内存值的1/8作为缓存的大小。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 8;

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(SoftApplication.get())
                .defaultDisplayImageOptions(options)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(4)//线程池线程数量
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
                 .memoryCacheSize(cacheSize)//注意是字节值
//                 .memoryCache(new BitmapCache())
                .memoryCache(new LruMemoryCache(cacheSize))//内存缓存模式，包含上面的缓存大小设置
                .diskCacheSize(100 * 1024 * 1024)//磁盘缓存
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(500)
                .denyCacheImageMultipleSizesInMemory();

        /*if (!TextUtils.isEmpty(mDiskCacheDirPath)) {
            File cacheDir = new File(mDiskCacheDirPath + "/imageLoader");
            builder.diskCache(new LruDiscCache(cacheDir, new Md5FileNameGenerator(), 100 * 1024 * 1024, 500));// 磁盘缓存模式，包含上面的路径，命名，缓存大小，数量
        }*/

        return builder.build();
    }

    @NonNull
    private com.nostra13.universalimageloader.core.listener.ImageLoadingListener varyImageLoadingListener(final ImageLoadingListener listener) {
        return new com.nostra13.universalimageloader.core.listener.ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String url, View view) {
                listener.onLoadingStarted(url, view);
            }

            @Override
            public void onLoadingFailed(String url, View view, FailReason failReason) {
                System.out.println("failReason=---" + failReason);
                listener.onLoadingFailed(url, view, failReason.getType().toString());
            }

            @Override
            public void onLoadingComplete(String url, View view, Bitmap bitmap) {
                listener.onLoadingComplete(url, view, bitmap);
            }

            @Override
            public void onLoadingCancelled(String url, View view) {
                listener.onLoadingCancelled(url, view);
            }
        };
    }

}
