package cn.arvin.framework.core.net.filter;


import cn.arvin.framework.core.net.RequestModel;

/**
 * Filter有如下几个用处。
 * <p>
 * 在HttpServletRequest到达Servlet之前，拦截客户的HttpServletRequest。
 * 根据需要检查HttpServletRequest，也可以修改HttpServletRequest头和数据。
 * 在HttpServletResponse到达客户端之前，拦截HttpServletResponse。
 * 根据需要检查HttpServletResponse，也可以修改HttpServletResponse头和数据。
 * Filter有如下几个种类。
 * <p>
 * 用户授权的Filter：Filter负责检查用户请求，根据请求过滤用户非法请求。
 * 日志Filter：详细记录某些特殊的用户请求。
 * 负责解码的Filter：包括对非标准编码的请求解码。
 * 能改变XML内容的XSLT Filter等。
 * Filter可负责拦截多个请求或响应；一个请求或响应也可被多个请求拦截。
 * <p>
 * Created by youkang-bd on 2016-06-30.
 */
public interface Filter<T> {

    /**
     * 创建Filter时执行初始化操作
     *
     * @param config
     */
    void init(FilterConfig config);

    /**
     * 请求具体的过滤逻辑
     *
     * @param request
     * @param filterChain
     */
    void doFilter(RequestModel request, FilterChain filterChain);

    /**
     * Filter使用完之后做一些资源释放操作
     */
    void destroy();
}
