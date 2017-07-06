package cn.arvin.framework.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;


/**
 * Created by youkang-bd on 2016-08-03.
 */
public class PermissionUtil {

    private PermissionUtil() {
    }

    /**
     * 是否具有存储卡权限
     *
     * @param context
     * @return
     */
    public static Boolean hasStorage(Context context) {
        return (PackageManager.PERMISSION_GRANTED ==
                context.getPackageManager().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()));
    }

    /**
     * 是否具有拍照和访问相册权限
     *
     * @param context
     * @return
     */
    public static Boolean hasCamera(Context context) {
        return (PackageManager.PERMISSION_GRANTED ==
                context.getPackageManager().checkPermission("android.permission.CAMERA", context.getPackageName()));
    }

    /**
     * 是否具有存储卡和拍照权限
     *
     * @param context
     * @return
     */
    public static boolean checkStorageAndCameraPermission(final Context context) {
        if (!hasCamera(context) || !hasStorage(context)) {
            /*new CommonDialog(mContext).buildMessageDialog()
                    .setTitle("获取权限")
                    .setMessage("我们需要获取您的照相机和存储卡权限，否则您将无法正常使用医学影像\n\n设置路径：设置->应用->医学影像->权限")
                    .setConfirmBtnText("去设置")
                    .setConfirmBtnOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            mContext.startActivity(intent);
                        }
                    });

            return false;*/
        }

        return true;
    }


    /**
     * 检查是否具有某权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();

            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
