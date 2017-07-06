package cn.arvin.framework.core.net.base;


import cn.arvin.framework.core.net.RequestModel;

public abstract class BaseNetwork {
    /**
     * 执行网络请求，由子类去重写，子类中可以使用任何第三方的开源框架去实现网络访问，并把相应的结果通过callback回调
     *
     * @param request
     */
    protected abstract void performRequest(RequestModel request);

    /**
     * 将框架中的HttpMethod转化成第三方框架中所需要的格式
     * @param request
     * @return
     */
    protected abstract Object convertHttpMethod(RequestModel request);

    /**
     * 将框架中的Headers转化成第三方框架中所需要的格式
     * @param request
     * @return
     */
    protected abstract Object convertHeaders(RequestModel request);

    /**
     * 将框架中的Headers转化成第三方框架中所需要的格式
     * @param request
     * @return
     */
    protected abstract Object convertParams(RequestModel request);
}
