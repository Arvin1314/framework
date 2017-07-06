package cn.arvin.framework.core.net.filter;


import cn.arvin.framework.core.net.RequestModel;

/**
 * Created by youkang-bd on 2017-04-14.
 */

public interface FilterChain {
    void doFilter(RequestModel request);
}
