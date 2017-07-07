package cn.arvin.framework.utils;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import cn.arvin.framework.application.FrameApplication;


/**
 * 显示土司通知的工具类
 */
public class ToastUtil {

    private static long mLastShowTime;

    private static CharSequence mLastShowContent;

    private ToastUtil() {
    }

    /**
     * 显示Toast通知
     *
     * @param text
     * @param duration
     */
    public static void show(CharSequence text, int duration) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (!mLastShowContent.equals(text) || System.currentTimeMillis() - mLastShowTime > 2000) {

            mLastShowContent = text;

            mLastShowTime = System.currentTimeMillis();

            Toast.makeText(FrameApplication.get(), text, duration).show();
        }

    }

    /**
     * 显示Toast通知
     *
     * @param resId
     * @param duration
     */
    public static void show(int resId, int duration) {
        CharSequence text = FrameApplication.get().getResources().getText(resId);
        show(text, duration);
    }

    /**
     * 显示Toast通知
     *
     * @param resId
     */
    public static void show(int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast通知
     *
     * @param text
     */
    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }


    /**
     * 显示自定义样式的Toast
     *
     * @param context
     * @param text
     */
    public static void showTop(Context context, String text) {
       /* Toast toast = new Toast(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.layout_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.message);
        textView.setText(text);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, DensityUtil.dp2px(60));
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();*/
    }

    /**
     * 显示自定义样式的Toast
     *
     * @param context
     * @param text
     */
    public static void showCenter(Context context, String text) {
        /*Toast toast = new Toast(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.layout_toast_checkin, null);
        TextView textView = (TextView) v.findViewById(R.id.message);
        textView.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();*/
    }
}
