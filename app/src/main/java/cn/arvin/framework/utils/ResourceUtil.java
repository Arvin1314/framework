package cn.arvin.framework.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import cn.arvin.framework.application.SoftApplication;


/**
 * Created by youkang-bd on 2017-04-24.
 */

public class ResourceUtil {
    private static Context context = SoftApplication.get();

    /**
     * 获取字符串资源
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 获取字符串资源
     *
     * @param resId
     * @return
     */
    public static float getDimen(int resId) {
        return context.getResources().getDimension(resId);
    }

    /**
     * 根据id获取颜色值
     *
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, resId);
        } else {
            return context.getResources().getColor(resId);
        }
    }
}
