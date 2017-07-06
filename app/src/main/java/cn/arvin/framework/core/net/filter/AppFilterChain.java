package cn.arvin.framework.core.net.filter;


import java.util.LinkedList;

import cn.arvin.framework.application.FilterManager;
import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.base.CommonNetwork;

/**
 * Created by youkang-bd on 2017-04-14.
 */

public class AppFilterChain implements FilterChain {

    //执行网络请求
    private CommonNetwork mNetwork;

    //当前执行的Filter下标
    private int mPosition = 0;

    //Filter数量
    private int mCount;

    private LinkedList<Filter> mFilters;

    //获取符合的所有的Filter实例
    public AppFilterChain(CommonNetwork network) {
        mFilters = FilterManager.getFilters();
        mNetwork = network;
        mCount = mFilters.size();
    }

    @Override
    public void doFilter(RequestModel request) {
        //所有请求Filter已经执行完
        if (mPosition >= mCount) {
            mNetwork.requestService(request);
            mPosition = 0;
        } else {
            Filter filter = mFilters.get(mPosition++);
            if (filter != null) {
                filter.doFilter(request, this);
            }
        }
    }
}
