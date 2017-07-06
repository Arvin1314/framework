package cn.arvin.framework.core.net;


import javax.net.ssl.SSLSocketFactory;

import cn.arvin.framework.core.net.base.BaseParams;

/**
 * Created by youkang-bd on 2017-04-14.
 */

public class RequestParams extends BaseParams {
    private SSLSocketFactory sslSocketFactory;
    private int connectTimeout = 1000 * 5; // 连接超时时间
    private int readTimeout = 1000 * 5; // 读取超时时间
    private boolean autoResume = true; // 是否在下载是自动断点续传
    private boolean autoRename = false; // 是否根据头信息自动命名文件
    private int maxRetryCount = 1; // 最大请求错误重试次数//至少是一次，否则重定向的url不能被访问
    private String saveFilePath; // 下载文件时文件保存的路径和文件名
    private boolean cancelFast = false; // 是否可以被立即停止, true: 为请求创建新的线程, 取消时请求线程被立即中断.
    private int loadingUpdateMaxTimeSpan = 300; // 进度刷新最大间隔时间(ms)

    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * 设置连接超时时间
     *
     * @param connectTimeout
     */
    public void setConnectTimeout(int connectTimeout) {
        if (connectTimeout > 0) {
            this.connectTimeout = connectTimeout;
        }
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * 设置读取超时时间
     *
     * @param readTimeout
     */
    public void setReadTimeout(int readTimeout) {
        if (readTimeout > 0) {
            this.readTimeout = readTimeout;
        }
    }

    /**
     * 是否在下载是自动断点续传
     */
    public boolean isAutoResume() {
        return autoResume;
    }

    /**
     * 设置是否在下载是自动断点续传
     *
     * @param autoResume
     */
    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    /**
     * 是否根据头信息自动命名文件
     */
    public boolean isAutoRename() {
        return autoRename;
    }

    /**
     * 设置是否根据头信息自动命名文件
     *
     * @param autoRename
     */
    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    /**
     * 获取下载文件时文件保存的路径和文件名
     */
    public String getSaveFilePath() {
        return saveFilePath;
    }

    /**
     * 设置下载文件时文件保存的路径和文件名
     *
     * @param saveFilePath
     */
    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * 是否可以被立即停止.
     *
     * @return true: 为请求创建新的线程, 取消时请求线程被立即中断; false: 请求建立过程可能不被立即终止.
     */
    public boolean isCancelFast() {
        return cancelFast;
    }

    /**
     * 是否可以被立即停止.
     *
     * @param cancelFast true: 为请求创建新的线程, 取消时请求线程被立即中断; false: 请求建立过程可能不被立即终止.
     */
    public void setCancelFast(boolean cancelFast) {
        this.cancelFast = cancelFast;
    }

    public int getLoadingUpdateMaxTimeSpan() {
        return loadingUpdateMaxTimeSpan;
    }

    /**
     * 进度刷新最大间隔时间(默认300毫秒)
     *
     * @param loadingUpdateMaxTimeSpan
     */
    public void setLoadingUpdateMaxTimeSpan(int loadingUpdateMaxTimeSpan) {
        this.loadingUpdateMaxTimeSpan = loadingUpdateMaxTimeSpan;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }
}
