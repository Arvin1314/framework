package cn.arvin.framework.core.net.base;

public interface BaseCallback {
    /**
     * 网络访问返回结果时回调,主线程中执行
     *
     * @param response
     */
    void onResponse(Object response);

    /**
     * 接口访问失败时回调,主线程中执行
     *
     * @param statusCode
     * @param errorResult
     */
    void onErrorResponse(int statusCode, String errorResult);

    /**
     * 网络访问失败时回调
     *
     * @param msg
     */
    void onNetworkFailure(String msg);

    /**
     * 当网络请求开始时回调,主线程中执行
     */
    void onStarted();

    /**
     * 网络请求取消,主线程中执行
     */
    void onCanceled();

    /**
     * 不管网络访问情况如何，网络访问结束之后回调，做一些后续统一处理,主线程中执行
     */
    void onFinished();
}
