package cn.arvin.framework.core.net.base;


import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.callback.CommonCallback;
import cn.arvin.framework.core.net.filter.AppFilterChain;
import cn.arvin.framework.core.net.filter.FilterChain;

public abstract class CommonNetwork extends BaseNetwork {
    public void sendRequest(RequestModel request) {
        FilterChain filterChain = new AppFilterChain(this);
        filterChain.doFilter(request);
    }

    public void requestService(RequestModel request) {
        if (request != null) {
            CommonCallback<?> callback = request.getCallback();
            if (!request.isCanceled()) {
                callback.onStarted();
                //执行网络请求
                performRequest(request);
            } else {
                callback.onCanceled();
                callback.onFinished();
            }
        }
    }
}
