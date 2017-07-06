package cn.arvin.framework.application;


import java.net.CookieHandler;

import cn.arvin.framework.constants.Config;
import cn.arvin.framework.core.image.ImageLoader;
import cn.arvin.framework.core.image.impl.ImageLoaderImpl;
import cn.arvin.framework.exception.AppUncaughtExceptionHandler;


/**
 * 类名称 ：SoftApplication 类描述 ：自定义Application
 */
public class SoftApplication extends AbsApp {

    @Override
    public void onCreate() {
        super.onCreate();

        //全局异常捕获
        Thread.setDefaultUncaughtExceptionHandler(AppUncaughtExceptionHandler.INSTANCE);

        //初始化Filter
        FilterManager.init(this);

        //Cookie管理
        CookieHandler.setDefault(new AppCookieManager(this));

        //设置图片加载器
        ImageLoader.setImageHandle(new ImageLoaderImpl());

    }


    /**
     * 设置应用是否可以调试，打印日志等
     *
     * @param debug
     */
    public static void setDebug(boolean debug) {
        Config.DEBUG = debug;
    }

    /**
     * 设置应用是否使用cookie
     *
     * @param useCookie
     */
    public static void setUseCookie(boolean useCookie) {
        Config.USE_COOKIE = useCookie;
    }


}
