package cn.arvin.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.arvin.framework.application.SoftApplication;
import cn.arvin.framework.constants.Config;


/**
 * @author WWN 操作SD卡上当前应用指定的目录下的文件
 */
public class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 删除制定路径下的文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
        }

        return file.delete();
    }

    /**
     * 清空指定文件夹下的所有文件不包含本身
     * @param dir
     */
    public static void emptyFolder(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFile(file);
                }
            }
        }
    }

    /**
     * 删除指定文件
     * @param file
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        deleteFile(f);
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 拷贝一个文件到指定文件
     *
     * @param fromFile
     * @param toFile
     * @return true 拷贝成功 false 拷贝失败
     */
    public boolean CopyFileToFile(File fromFile, File toFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(fromFile);
            os = new FileOutputStream(toFile);
            byte[] bt = new byte[8 * 1024];
            int len;
            while ((len = is.read(bt)) > 0) {
                os.write(bt, 0, len);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将内部存储中的文件拷贝到SD卡上
     *
     * @param romFile
     * @return
     */
    public File CopyFileRomToSD(String romFile) {
        if (isSDCardAvailable()) {
            File rom = new File(romFile);
            File sd = new File(Environment.getExternalStorageDirectory() + Config.BASE_DIR + "/" + romFile);

            if (CopyFileToFile(rom, sd)) {
                return sd;
            }
        }

        return null;
    }

    /**
     * 获取SD卡上的缓存文件夹路径
     *
     * @param fileName
     * @return
     */
    public static String getCachePhotoPath(String fileName) {
        File dir = SoftApplication.get().getExternalCacheDir();
        File file = new File(dir, fileName);
        return file.getPath();
    }

    public static Uri getCachePhotoUri(String fileName) {
        File dir = SoftApplication.get().getExternalCacheDir();
        File file = new File(dir, fileName);

        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }

    //----------------------------------------------------------------------------------------

    /**
     * 创建一个新的图片名称
     *
     * @return
     */
    public static String createNewImageName() {
        Random rand = new Random();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + rand.nextInt(1000);
        return "IMG_" + timeStamp + ".jpg";
    }

    /*
    *   getExternalCacheDir获取到的缓存路径:/sdcard/Android/data/<application package>/cache
    *   getCacheDir获取到的缓存路径:/data/data/<application package>/cache
    */
    public static String getDiskCacheDir(Context context) {
        File cache = null;
        if (isSDCardAvailable()) {//&& PermissionUtil.hasStorage(mContext)
            cache = context.getExternalCacheDir();
        }

        if (cache == null || TextUtils.isEmpty(cache.getAbsolutePath())) {
            cache = context.getCacheDir();
        }

        if (cache != null) {
            if (!cache.exists()) {
                cache.mkdirs();
            }
            return cache.getAbsolutePath();
        }

        return "";
    }

    /**
     * SD卡是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());//|| !Environment.isExternalStorageRemovable();
    }

    /**
     * 根据uri获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static final String getPathByUri(Context context, Uri uri) {
        if (uri == null) {
            return "";
        }
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri, projection, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToNext();
            if (cursor.getCount() == 0) {
                cursor.close();
                return null;
            } else {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
                return imagePath;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 添加到图库
     */
    public static void addPicToGallery(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


}
