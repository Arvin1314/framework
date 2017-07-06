package cn.arvin.framework.core.net;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cn.arvin.framework.utils.CollectionsUtil;

/**
 * Created by youkang-bd on 2016-07-07.
 */
public class RestUrlBuilder {

    private StringBuilder mUrlBuilder;

    public RestUrlBuilder() {
        mUrlBuilder = new StringBuilder();
    }

    public RestUrlBuilder(String baseUrl) {
        mUrlBuilder = new StringBuilder(baseUrl);
    }

    /**
     * 设置基础链接地址
     *
     * @param baseUrl
     * @return
     */
    public RestUrlBuilder base(String baseUrl) {
        mUrlBuilder.append(baseUrl);
        return this;
    }

    /**
     * 在基础链接上的下一级路径
     *
     * @param path
     * @return
     */
    public RestUrlBuilder next(Object path) {
        checkHost();

        String url = mUrlBuilder.toString();

        //先去除掉最后一个"/"
        if (url.endsWith("/")) {
            mUrlBuilder.deleteCharAt(url.lastIndexOf("/"));
        }

        String next = String.valueOf(path);
        if (next.startsWith("/")) {
            mUrlBuilder.append(next);
        } else {
            mUrlBuilder.append("/");
            mUrlBuilder.append(next);
        }

        url = mUrlBuilder.toString();
        if (!url.endsWith("/")) {
            mUrlBuilder.append("/");
        }

        return this;
    }

    /**
     * 设置链接所带的参数
     *
     * @param params
     * @return
     */
    public RestUrlBuilder addParams(Map<String, String> params) {
        checkHost();

        if (!CollectionsUtil.isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                addParam(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public RestUrlBuilder addParam(String key, Object value) {
        if (TextUtils.isEmpty(key)) {
            return this;
        }
        String url = mUrlBuilder.toString();
        if (!url.contains("?")) {//不包含"?"
            mUrlBuilder.append("?");
        } else if (!url.endsWith("?")) {//包含"?"但不是以"?"结尾
            mUrlBuilder.append("&");
        }

        mUrlBuilder.append(key);
        mUrlBuilder.append("=");
        try {
            mUrlBuilder.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Encoding not supported!");
        }

        return this;
    }

    /**
     * 设置链接所带的参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String buildQueryUrl(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url.trim());

        if (!url.contains("?")) {
            sb.append("?");
        } else if (!url.endsWith("?")) {
            sb.append("&");
        }

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(String.valueOf(entry.getKey()));
                sb.append("=");
                //把链接转码，防止有空格或汉字

                try {
                    sb.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Encoding not supported!");
                }

                sb.append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }

        return sb.toString();
    }

    /**
     * 最终构成的链接
     *
     * @return
     */
    public String build() {
        return mUrlBuilder.toString();
    }

    private void checkHost() {
        if (TextUtils.isEmpty(mUrlBuilder.toString())) {
            throw new IllegalStateException("url is empty !");
        }
    }
}
