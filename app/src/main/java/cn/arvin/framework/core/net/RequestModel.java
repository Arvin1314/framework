package cn.arvin.framework.core.net;


import java.lang.reflect.Type;

import cn.arvin.framework.core.net.callback.CommonCallback;

public class RequestModel {
    /**
     * 网络访问的地址
     */
    private final String mUrl;

    /**
     * 网络访问的方式
     */
    private final Method mMethod;

    /**
     * 参数
     */
    private RequestParams mParams;

    /**
     * 网络访问结束的回调接口
     */
    private CommonCallback<?> mCallback;

    /**
     * 网络访问返回的结果对应的是实体类
     */
    private Object mResultObject;

    /**
     * 网络访问返回的结果对应的是实体类
     */
    private Class mClazz;

    /**
     * 网络访问返回的结果对应的是实体类
     */
    private Type mType;

    /**
     * 请求的标识
     */
    private Object mTag;

    /**
     * 请求是否取消
     */
    private boolean mCancel;

    public RequestModel(String url, Method method, CommonCallback<?> callback) {
        this(url, method, null, null, null, callback);
    }

    public RequestModel(String url, Method method, RequestParams params, CommonCallback<?> callback) {
        this(url, method, params, null, null, callback);
    }

    public RequestModel(String url, Method method, Class<?> clazz, CommonCallback<?> callback) {
        this(url, method, null, clazz, null, callback);
    }

    public RequestModel(String url, Method method, Type type, CommonCallback<?> callback) {
        this(url, method, null, null, type, callback);
    }

    public RequestModel(String url, Method method, RequestParams params, Class<?> clazz, CommonCallback<?> callback) {
        this(url, method, params, clazz, null, callback);
    }

    public RequestModel(String url, Method method, RequestParams params, Type type, CommonCallback<?> callback) {
        this(url, method, params, null, type, callback);
    }

    private RequestModel(String url, Method method, RequestParams params, Class<?> clazz, Type type, CommonCallback<?> callback) {
        mUrl = url;
        mMethod = method;
        mParams = params == null ? new RequestParams() : params;
        mCallback = callback;
        mClazz = clazz;
        mType = type;
        mResultObject = clazz == null ? type : clazz;
        mCallback.setRequest(this);
    }

    public Object getTag() {
        return mTag;
    }

    public RequestModel setTag(Object tag) {
        mTag = tag;
        return this;
    }

    public boolean isCanceled() {
        return mCancel;
    }

    public void cancel() {
        mCancel = true;
        if (mCallback != null) {
            mCallback.onCanceled();
        }
    }

    public Object getResultObject() {
        return mResultObject;
    }

    public RequestParams getParams() {
        return mParams;
    }


    public RequestModel setParams(RequestParams paramsMap) {
        mParams = paramsMap;
        return this;
    }

    public CommonCallback<?> getCallback() {
        return mCallback;
    }

    public RequestModel setCallback(CommonCallback<?> callback) {
        mCallback = callback;
        return this;
    }

    public Class<?> getClazz() {
        return mClazz;
    }

    public RequestModel setClazz(Class<?> clazz) {
        mClazz = clazz;
        return this;
    }

    public Type getType() {
        return mType;
    }

    public RequestModel setType(Type type) {
        mType = type;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public Method getMethod() {
        return mMethod;
    }

    /**
     * http请求方式
     */
    public enum Method {
        DEPRECATED_GET_OR_POST,
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE,
        CONNECT,
        PATCH
    }
}
