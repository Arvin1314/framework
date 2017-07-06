package cn.arvin.framework.core.net;

import java.util.Map;

/**
 * Created by youkang-bd on 2017-04-14.
 */

public class ResponseModel {
    private int statusCode;

    private Map<String, String> headers;

    private String data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
