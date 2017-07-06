package cn.arvin.framework.core.image.impl;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.Collection;

import cn.arvin.framework.application.SoftApplication;
import cn.arvin.framework.core.image.base.BaseImageLoader;
import cn.arvin.framework.core.image.listener.ImageLoadingListener;
import cn.arvin.framework.utils.LogUtil;

/**
 * 对UniversalImageLoader加载图片进行封装
 */
public class UniversalImageLoader extends BaseImageLoader {

    private ImageLoader mUniversalImageLoader;

    static class InstanceHolder {
        private static final UniversalImageLoader INSTANCE = new UniversalImageLoader();
    }

    public static UniversalImageLoader getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private UniversalImageLoader() {
        mUniversalImageLoader = initUniversalImageLoader();
    }

    public ImageLoader getUniversalImageLoader() {
        return mUniversalImageLoader;
    }

    public MemoryCache getMemoryCache() {
        return mUniversalImageLoader.getMemoryCache();
    }

    /**
     * 使用默认的设置进行图片加载
     *
     * @param imageUrl
     * @param imageView
     */
    @Override
    public void showImage(String imageUrl, ImageView imageView) {
        mUniversalImageLoader.displayImage(imageUrl, imageView);
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, int defaultImage) {
        DisplayImageOptions.Builder builder = createDefaultDisplayImageOptionsBuilder();
        builder.showImageOnLoading(defaultImage);

        DisplayImageOptions options = builder.build();

        mUniversalImageLoader.displayImage(imageUrl, imageView, options);
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, int width, int height, int defaultImage) {

    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, Bitmap.Config bitmapConfig) {
        DisplayImageOptions.Builder builder = createDefaultDisplayImageOptionsBuilder();
        builder.bitmapConfig(bitmapConfig);

        DisplayImageOptions options = builder.build();

        mUniversalImageLoader.displayImage(imageUrl, imageView, options);
    }

    @Override
    public void loadImage(String imageUrl, ImageView imageView, final ImageLoadingListener listener) {
        mUniversalImageLoader.displayImage(imageUrl, imageView, convertListener(listener));
    }

    @Override
    public void loadImage(String imageUrl, ImageLoadingListener listener) {
        mUniversalImageLoader.loadImage(imageUrl, convertListener(listener));
    }

    @Override
    protected com.nostra13.universalimageloader.core.listener.ImageLoadingListener convertListener(final ImageLoadingListener listener) {
        super.convertListener(listener);

        com.nostra13.universalimageloader.core.listener.ImageLoadingListener realListener = new com.nostra13.universalimageloader.core.listener.ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                listener.onLoadingStarted(s, view);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                listener.onLoadingFailed(s, view, failReason.getCause().getMessage());
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                listener.onLoadingComplete(s, view, bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                listener.onLoadingCancelled(s, view);
            }
        };
        return realListener;
    }

    private ImageLoader initUniversalImageLoader() {
        //mDiskCacheDirPath = FileUtil.getDiskCacheDir(SoftApplication.get());

        DisplayImageOptions.Builder builder = createDefaultDisplayImageOptionsBuilder();
        DisplayImageOptions options = builder.build();

        ImageLoaderConfiguration config = createImageLoaderConfiguration(options);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        return imageLoader;
    }

    private DisplayImageOptions.Builder createDefaultDisplayImageOptionsBuilder() {
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

    private ImageLoaderConfiguration createImageLoaderConfiguration(DisplayImageOptions options) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                SoftApplication.get())
                .defaultDisplayImageOptions(options)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(4)//线程池线程数量
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
                // .memoryCacheSize(cacheSize)//注意是字节值
                // .memoryCache(new BitmapCache())
                .memoryCache(new LruMemoryCache(cacheSize))//内存缓存模式，包含上面的缓存大小设置
                .diskCacheSize(100 * 1024 * 1024)//磁盘缓存
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(500);
        //.denyCacheImageMultipleSizesInMemory();

        /*if (!TextUtils.isEmpty(mDiskCacheDirPath)) {
            File cacheDir = new File(mDiskCacheDirPath + "/imageLoader");
            builder.diskCache(new LruDiscCache(cacheDir, new Md5FileNameGenerator(), 100 * 1024 * 1024, 500));// 磁盘缓存模式，包含上面的路径，命名，缓存大小，数量
        }*/

        return builder.build();
    }


    class BitmapCache implements MemoryCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
            // LruCache通过构造函数传入缓存值，以KB为单位。
            int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            LogUtil.d("max available memory is " + maxMemory / 1024 + " MB");
            // 使用最大可用内存值的1/8作为缓存的大小。
            int cacheSize = maxMemory / 8;
            mCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                    return bitmap.getByteCount() / 1024;
                }
            };
        }

        @Override
        public boolean put(String key, Bitmap bitmap) {
            mCache.put(key, bitmap);
            if (mCache.size() / mCache.maxSize() > 0.75) {
                LogUtil.e("总缓存大小：" + mCache.maxSize() + "KB，使用了：" + mCache.size() + "KB");
            }
            return true;
        }

        @Override
        public Bitmap get(String key) {
            return mCache.get(key);
        }

        @Override
        public Bitmap remove(String key) {
            return mCache.remove(key);
        }

        @Override
        public Collection<String> keys() {
            return mCache.snapshot().keySet();
        }

        @Override
        public void clear() {

        }
    }
}
