package cn.arvin.framework.utils;

import android.content.Context;

import cn.arvin.framework.application.SoftApplication;
import cn.arvin.framework.bean.Screen;


public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int)(dp * Screen.SCALE + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / Screen.SCALE + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     * @param sp
     * @return
     */
    public static float sp2px(float sp) {
        Context context = SoftApplication.get();
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
