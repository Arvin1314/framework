package cn.arvin.framework.core.net.base;

import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.arvin.framework.utils.CollectionsUtil;

/**
 * Created by youkang-bd on 2016-08-17.
 */
public class BaseParams {
    /**
     * Default encoding for POST or PUT parameters. See {@link #getParamsEncoding()}.
     */
    protected static final String CHARSET = "UTF-8";

    /**
     * Content type for request.
     */
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=" + CHARSET;

    private static final String PROTOCOL_CONTENT_TYPE = "application/json; charset=" + CHARSET;

    private String mContentType = CONTENT_TYPE;

    /**
     * 请求头信息
     */
    private final Map<String, String> mHeaders = new HashMap<>();
    /**
     * 链接中附带的参数
     */
    private final Map<String, String> mQueryStringParams = new HashMap<>();
    /**
     * 请求体重的参数
     */
    private final Map<String, String> mBodyParams = new HashMap<>();
    /**
     * 请求体中的文件参数
     */
    private final Map<String, File> mFileParams = new HashMap<>();

    /**
     * 请求体中的内容//如果有内容，则mBodyParams中的参数不会被放到请求体重发送出去
     */
    private String bodyContent;

    /**
     * 添加请求头信息
     *
     * @param headers
     */
    public void setHeaders(Map<String, String> headers) {
        if (CollectionsUtil.isEmpty(headers)) {
            mHeaders.putAll(headers);
        }
    }

    /**
     * 添加请求头信息
     *
     * @param name
     * @param value
     */
    public void setHeader(String name, String value) {
        if (!TextUtils.isEmpty(name)) {
            mHeaders.put(name, value);
        }
    }

    /**
     * 添加请求头信息
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        if (!TextUtils.isEmpty(name)) {
            mHeaders.put(name, value);
        }
    }

    /**
     * 添加参数至Query String
     *
     * @param name
     * @param value
     */
    public void addQueryStringParameter(String name, String value) {
        if (!TextUtils.isEmpty(name)) {
            mQueryStringParams.put(name, value);
        }
    }

    /**
     * 添加body参数
     *
     * @param name
     * @param value
     */
    public void addBodyParameter(String name, String value) {
        if (!TextUtils.isEmpty(name)) {
            mBodyParams.put(name, value);
        } else {
            bodyContent = value;
            mContentType = PROTOCOL_CONTENT_TYPE;
        }
    }

    /**
     * 添加body参数
     *
     * @param name
     * @param value
     */
    public void addBodyParameter(String name, File value) {
        if (!TextUtils.isEmpty(name)) {
            mFileParams.put(name, value);
        }
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Map<String, String> getQueryStringParams() {
        return mQueryStringParams;
    }

    public Map<String, String> getBodyParams() {
        return mBodyParams;
    }

    public Map<String, File> getFileParams() {
        return mFileParams;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String jsonStr, String contentType) {
        this.bodyContent = jsonStr;
        mContentType = contentType;
    }

    public void setBodyContent(String jsonStr) {
        bodyContent = jsonStr;
        mContentType = PROTOCOL_CONTENT_TYPE;
    }

    /*
     * Returns which encoding should be used when converting POST or PUT parameters returned by
     * {@link #getBodyParams()} into a raw POST or PUT body.
     * */
    public String getParamsEncoding() {
        return CHARSET;
    }

    /**
     * Returns the content type of the POST or PUT body.
     */
    public String getBodyContentType() {
        return mContentType;
    }
}
