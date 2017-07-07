package cn.arvin.framework.core.net;


import java.lang.reflect.Type;

import cn.arvin.framework.core.net.base.CommonNetwork;
import cn.arvin.framework.core.net.callback.CommonCallback;
import cn.arvin.framework.core.net.impl.DefaultNetwork;


public class RequestSender {
    public static boolean shouldCache = false;

    private static CommonNetwork sDefaultNetwork = DefaultNetwork.getInstance();

    private RequestSender() {
        throw new RuntimeException("RequestSender cannot be instantiation");
    }

    public static void init(CommonNetwork network) {
        sDefaultNetwork = network;
    }

    //此种请求方式好处就是将当前请求的相关参数封装起来了，一旦该次请求失败，需要重新请求时更方便一些
    public static void sendRequest(RequestModel request) {
        sDefaultNetwork.sendRequest(request);
    }

    //下面网络请求方式更直观一些
    public static void stringGetRequest(String url, CommonCallback<String> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.GET, callback));
    }

    public static void stringGetRequest(String url, RequestParams requestParams, CommonCallback<String> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.GET, requestParams, callback));
    }

    public static void objectGetRequest(String url, Class<?> clazz, CommonCallback<?> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.GET, clazz, callback));
    }

    public static void objectGetRequest(String url, RequestParams requestParams, Class<?> clazz, CommonCallback<?> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.GET, requestParams, clazz, callback));
    }

    public static void stringPostRequest(String url, RequestParams paramMap, CommonCallback<String> callback) {

        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.POST, paramMap, callback));
    }

    public static void objectPostRequest(String url, RequestParams paramMap, Class<?> clazz, CommonCallback<?> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.POST, paramMap, clazz, callback));
    }

    public static void objectDeleteRequest(String url, Class<?> clazz, CommonCallback<?> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.DELETE, clazz, callback));
    }

    //----------------------------根据TypeToken解析--------------------------//
    public static void objectGetRequest(String url, Type type, CommonCallback<?> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.GET, type, callback));
    }

    public static void objectGetRequest(String url, RequestParams requestParams, Type type, CommonCallback<?> callback) {
        sDefaultNetwork.sendRequest(new RequestModel(url, RequestModel.Method.GET, requestParams, type, callback));
    }
}
