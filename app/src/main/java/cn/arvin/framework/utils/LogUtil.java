package cn.arvin.framework.utils;


import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import cn.arvin.framework.application.SoftApplication;
import cn.arvin.framework.constants.Config;


/**
 * Log统一管理类
 *
 * @author WWN
 */
public class LogUtil {
    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final boolean DEBUG = Config.DEBUG;

    private static final String TAG = SoftApplication.get().getPackageName();

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        i("", msg);
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void e(String msg) {
        e("", msg);
    }

    public static void v(String msg) {
        v("", msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(TAG + ":" + tag, String.valueOf(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(TAG + ":" + tag, String.valueOf(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(TAG + ":" + tag, String.valueOf(msg));
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(TAG + ":" + tag, String.valueOf(msg));
        }
    }

    /**
     * 打印Bundle内容
     *
     * @param bundle
     */
    public static void bundle(Bundle bundle) {
        if (DEBUG) {
            Set<String> keySet = bundle.keySet();
            for (String key : keySet) {
                Object value = bundle.get(key);
                if (value != null) {
                    LogUtil.d(TAG + " key:" + key + "\nvalue:" + value.toString());
                }
            }
        }
    }

    /**
     * 向服务器端发送错误日志
     *
     * @param e
     */
    public static void sendLog(Throwable e) {
        sendLog(getExceptionString(e));
    }

    /**
     * 向服务器端发送错误日志
     *
     * @param stack
     */
    public static void sendLog(String stack) {

    }

    /**
     * 保存异常信息到本地
     *
     * @param exceptionString
     */
    public static void saveExceptionInfo(String exceptionString) {
        FileOutputStream fos = null;
        try {
            String date = DateTimeUtil.formatCurrentTime(DateTimeUtil.DF_YYYY_MM_DD);
            String path = StorageUtil.getStoreLogDirectoryPath(SoftApplication.get()) + "/" + "log_" + date + ".txt";
            File file = new File(path);
            fos = new FileOutputStream(file, true);

            String crashTime = DateTimeUtil.formatDateTime(System.currentTimeMillis()) + ":  ";

            String crashDetail = crashTime + exceptionString + "\n\r\n\r";

            byte[] bytes = crashDetail.getBytes();

            if (!DEBUG) {//正式版上日志加密
                bytes = Base64.encode(bytes, Base64.DEFAULT);
            }

            fos.write(bytes);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存异常信息到本地
     *
     * @param e
     */
    public static void saveExceptionInfo(Throwable e) {
        saveExceptionInfo(getExceptionString(e));
    }


    /**
     * 获取错误日志信息
     *
     * @param exception
     * @return
     */
    public static String getExceptionString(Throwable exception) {
        String error = "";
        if (exception != null) {
            ByteArrayOutputStream bos = null;
            PrintStream ps = null;
            try {
                bos = new ByteArrayOutputStream();
                ps = new PrintStream(bos);
                exception.printStackTrace(ps);
                error = bos.toString();
            } catch (Exception e1) {
                error = exception.toString();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        }
        return error;
    }
}