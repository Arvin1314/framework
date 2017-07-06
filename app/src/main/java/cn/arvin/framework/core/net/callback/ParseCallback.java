package cn.arvin.framework.core.net.callback;


import cn.arvin.framework.core.net.NetworkHeader;

public abstract class ParseCallback<ResultType> extends CommonCallback<ResultType> {
    /**
     * 解析相应头信息
     *
     * @param headers
     */
    public abstract void parseHeaders(NetworkHeader headers);

    /**
     * 解析状态码
     *
     * @param statusCode
     */
    public void parseStatusCode(int statusCode) {
        if (statusCode >= 500) {
            //服务器异常

        } else if (statusCode >= 400) {
            //身份失效，重新登录

        } else if (statusCode >= 300) {

        } else if ((statusCode >= 200)) {

        } else {

        }
    }
}