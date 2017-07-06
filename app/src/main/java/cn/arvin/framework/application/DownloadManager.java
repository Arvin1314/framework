package cn.arvin.framework.application;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadManager {

    /**
     * 下载路径
     */
    private String downloadUrl;
    /**
     * 下载保存路径
     */
    private String savePath;
    /**
     * 文件保存名称
     */
    private String fileName;
    /**
     * 获取文件大小
     */
    private long fileSize = 0;
    /**
     * 已下载的文件大小
     */
    private long downloadSize = 0;
    /**
     * 取消下载
     */
    private boolean cancelState = false;
    /**
     * 下载是否完成
     */
    private boolean doneState = false;
    /**
     * 文件是否下载完成的回掉接口
     */
    private OnFinishedListener finishedListener;

    /**
     * 文件下载管理类
     *
     * @param url  下载路径
     * @param path 文件保存路径
     * @param path 文件保存名称
     */
    public DownloadManager(String url, String path, String name) {
        this.downloadUrl = url;
        this.savePath = path;
        this.fileName = name;
        // 启动新线程下载软件
        new downloadThread().start();
    }

    /**
     * 是否已经取消文件的下载
     *
     * @return boolean
     */
    public boolean isCancelState() {
        return cancelState;
    }

    /**
     * 是否取消下载
     *
     * @param cancelState
     */
    public void setCancelState(boolean cancelState) {
        this.cancelState = cancelState;
    }

    /**
     * 下载是否已完成
     *
     * @return doneState
     */
    public boolean isDoneState() {
        return doneState;
    }

    /**
     * 该文件的总大小
     *
     * @return long
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * 该文件已下载的大小
     *
     * @return long
     */
    public long getDownloadSize() {
        return downloadSize;
    }

    /**
     * 文件下载完成的回调
     *
     * @param finishedListener
     */
    public void setFinishedListener(OnFinishedListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    /**
     * 下载文件线程
     */
    private class downloadThread extends Thread {

        @Override
        public void run() {
            // 判断SD卡是否存在，并且是否具有读写权限
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }

            InputStream is = null;
            RandomAccessFile randomFile = null;
            try {

                URL url = new URL(downloadUrl);

                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 获取文件大小
                fileSize = conn.getContentLength();

                File file = new File(savePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(savePath, fileName);
                long startSize = apkFile.length();
                downloadSize = startSize;
                if (startSize == fileSize) {//文件已下载
                    doneState = true;
                    if (finishedListener != null) {
                        finishedListener.onFinished(downloadUrl, fileSize, savePath, fileName);
                    }
                    downloadSize = fileSize;
                    return;
                } else if (startSize > fileSize) {//文件异常，重新下载
                    apkFile.delete();
                    startSize = 0;
                    downloadSize = 0;
                }
                conn.disconnect();

                conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setRequestProperty("RANGE", "bytes=" + startSize + "-"); // 设置断点续传的开始位置
                conn.connect();
                // 创建输入流
                is = conn.getInputStream();
                // 输出流
                randomFile = new RandomAccessFile(apkFile, "rw");
                // 文件长度，字节数
                long fileLength = randomFile.length();
                //将写文件指针移到文件尾。
                randomFile.seek(fileLength);
                // 缓存
                byte buf[] = new byte[8 * 1024];
                // 写入到文件中
                while (!cancelState)// 点击取消就停止下载.
                {
                    // 读到的字节数
                    int numread = is.read(buf);
                    if (numread <= 0) {
                        doneState = true;
                        if (finishedListener != null) {
                            finishedListener.onFinished(downloadUrl, fileSize, savePath, fileName);
                        }
                        break;
                    }

                    downloadSize += numread;
                    // 写入文件
                    randomFile.write(buf, 0, numread);
                }

            } catch (Exception e) {
                doneState = true;
                e.printStackTrace();
                if (finishedListener != null) {
                    finishedListener.onFailure();
                }

            } finally {
                try {
                    if (randomFile != null) {
                        randomFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 文件是否下载完成的回调接口
     */
    public interface OnFinishedListener {
        void onFinished(String url, long fileSize, String savePath, String fileName);

        void onFailure();
    }
}
