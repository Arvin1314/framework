package cn.arvin.framework.core.net.callback;

public abstract class ProgressCallback<ResultType> extends CommonCallback<ResultType> {

    /**
     * 网络请求进度回调
     * @param total
     * @param current
     * @param isDownloading
     */
    public abstract void onLoading(long total, long current, boolean isDownloading);

}