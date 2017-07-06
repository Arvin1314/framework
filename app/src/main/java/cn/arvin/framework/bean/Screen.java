package cn.arvin.framework.bean;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import cn.arvin.framework.application.SoftApplication;


public class Screen {
  public static final int WIDTH = getWidth(SoftApplication.get());

  public static final int HEIGHT = getHeight(SoftApplication.get());

  public static final int STATUS_BAR_HEIGHT = getStatusBarHeight(SoftApplication.get());

  public static final float SCALE = getScale(SoftApplication.get());

  /**
   * 获取屏幕宽度
   */
  public static int getWidth(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.widthPixels;
  }

  /**
   * 获取屏幕高度
   */
  public static int getHeight(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.heightPixels;
  }

  /**
   * 获取屏幕的缩放比例
   */
  public static float getScale(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.density;
  }

  /**
   * 获取屏幕的状态栏高度
   */
  public static int getStatusBarHeight(Context context) {
    Resources resources = context.getResources();
    int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
    int height = resources.getDimensionPixelSize(resourceId);
    return height;
  }
}

