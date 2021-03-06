package cn.arvin.framework.application;


import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.net.CookieHandler;
import java.util.List;

import cn.arvin.framework.constants.Config;
import cn.arvin.framework.core.image.ImageLoader;
import cn.arvin.framework.core.image.impl.ImageLoaderImpl;
import cn.arvin.framework.exception.AppUncaughtExceptionHandler;
import cn.arvin.framework.utils.CollectionsUtil;


public class FrameApplication extends Application {

    private static FrameApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

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
     * 获取当前应用的Context对象，可以用于工具类中
     *
     * @return Application
     */
    @SuppressWarnings("unchecked")
    public static <T extends FrameApplication> T get() {
        return (T) sApplication;
    }

    /**
     * 获取当前系统的版本号
     *
     * @return SDK_INT
     */
    public int getSystemVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 得到应用的版本号
     *
     * @return versionCode
     */
    public int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        int versionCode = 0;
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 得到应用的版本名称
     *
     * @return versionName
     */
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        String versionName = "";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 应用是否处于运行状态
     *
     * @return Running foreground status
     */
    public static boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) get().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if (!CollectionsUtil.isEmpty(runningTasks)) {
            ComponentName cn = runningTasks.get(0).topActivity;
            String currentPackageName = cn.getPackageName();
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(get().getPackageName())) {
                return true;
            }
        }
        return false;
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
