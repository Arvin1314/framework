package cn.arvin.framework.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import cn.arvin.framework.constants.Config;

/**
 * 程序中文件存储位置管理
 * Created by youkang-bd on 2017-04-21.
 */

public class StorageUtil {
    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取项目中用于存储图片的目录
     *
     * @return
     */
    public static String getStoreImagesDirectoryPath(Context context) {
        File dir = createDirectoryByName(context, Config.IMAGE_CACHE_DIR);
        return dir.getAbsolutePath();
    }

    /**
     * 获取项目中用于存储日志的目录
     *
     * @return
     */
    public static String getStoreLogDirectoryPath(Context context) {
        File dir = createDirectoryByName(context, Config.LOG_CACHE_DIR);
        return dir.getAbsolutePath();
    }


    /**
     * 获取项目中用于存储视频的目录
     *
     * @return
     */
    public static String getStoreVideosDirectoryPath(Context context) {
        File dir = createDirectoryByName(context, Config.VIDEO_CACHE_DIR);
        return dir.getAbsolutePath();
    }

    /**
     * 获取项目中用于存储视频的目录
     *
     * @return
     */
    @NonNull
    public static String getStoreDownloadDirectoryPath(Context context) {
        File dir = createDirectoryByName(context, Config.DOWNLOAD_CACHE_DIR);
        return dir.getAbsolutePath();
    }

    /**
     * 在SD卡本应用指定的文件夹下上创建文件夹
     */
    public static File createDirectoryByName(Context context, String directoryName) {
        String path = "";
        if (FileUtil.isSDCardAvailable()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + directoryName;
        } else {
            path = context.getCacheDir().getAbsolutePath() + "/" + directoryName;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 在SD卡本应用指定的文件夹下上创建文件
     */
    public static File createFileByName(Context context, String fileName) throws IOException {
        String path = createDirectoryByName(context, Config.BASE_DIR).getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }
}
