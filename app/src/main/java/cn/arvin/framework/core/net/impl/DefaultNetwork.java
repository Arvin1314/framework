package cn.arvin.framework.core.net.impl;


import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.base.CommonNetwork;

/**
 * Created by youkang-bd on 2017-04-18.
 */

public class DefaultNetwork extends CommonNetwork {
    private static final String TAG = "NetworkDefault";

    static class InstanceHolder {
        private static final DefaultNetwork INSTANCE = new DefaultNetwork();
    }

    public static DefaultNetwork getInstance() {
        return DefaultNetwork.InstanceHolder.INSTANCE;
    }


    @Override
    protected void performRequest(RequestModel request) {

    }

    @Override
    protected Object convertHttpMethod(RequestModel request) {
        return null;
    }

    @Override
    protected Object convertHeaders(RequestModel request) {
        return null;
    }

    @Override
    protected Object convertParams(RequestModel request) {
        return null;
    }
}
