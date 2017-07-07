package cn.arvin.framework.core.net.impl;


import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import cn.arvin.framework.R;
import cn.arvin.framework.application.FrameApplication;
import cn.arvin.framework.core.net.RequestSender;
import cn.arvin.framework.core.net.NetworkHeader;
import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.RestUrlBuilder;
import cn.arvin.framework.core.net.base.CommonNetwork;
import cn.arvin.framework.core.net.callback.CommonCallback;
import cn.arvin.framework.core.net.callback.ParseCallback;
import cn.arvin.framework.utils.LogUtil;
import cn.arvin.framework.utils.ResourceUtil;

/**
 * 使用Volley框架实现具体的网络请求操作
 */
public class VolleyNetwork extends CommonNetwork {
    private static final String TAG = "NetworkVolley";

    private RequestQueue mRequestQueue;

    static class InstanceHolder {
        private static final VolleyNetwork INSTANCE = new VolleyNetwork();
    }

    public static VolleyNetwork getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private VolleyNetwork() {
        // 这个用单例
        mRequestQueue = Volley.newRequestQueue(FrameApplication.get());//,new HurlStack(null, HTTPSTrustManager.buildSSLSocketFactory())
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    @Override
    protected void performRequest(RequestModel requestModel) {
        Response.Listener<String> listener = getListener(requestModel);
        Response.ErrorListener errorListener = getErrorListener(requestModel);
        Request<String> request = buildRequest(requestModel, listener, errorListener);

        if (!requestModel.isCanceled()) {
            request.setTag(requestModel.getTag());
            int timeOut = requestModel.getParams().getConnectTimeout();
            int retryCount = requestModel.getParams().getMaxRetryCount();
            RetryPolicy retryPolicy = new DefaultRetryPolicy(timeOut, retryCount, 0f);
            request.setRetryPolicy(retryPolicy);
            request.setShouldCache(RequestSender.shouldCache);
            mRequestQueue.add(request);
        }
    }

    private Request<String> buildRequest(final RequestModel requestModel, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = generateUrl(requestModel);
        int volley_method = convertHttpMethod(requestModel);
        final CommonCallback<?> callback = requestModel.getCallback();

        final Request<String> request = new StringRequest(volley_method, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return convertHeaders(requestModel);
            }

            //当请求方式为POST/PUT时会调用该方法，为GET时不调用
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return convertParams(requestModel);
            }

            //请求方式为POST/PUT，当有bodyContent内容时，发送bodyContent内容，否则发送getParams()返回的内容
            @Override
            public byte[] getBody() throws AuthFailureError {
                String bodyContent = requestModel.getParams().getBodyContent();
                if (!TextUtils.isEmpty(bodyContent)) {
                    try {
                        LogUtil.d(TAG, "bodyContent--->" + bodyContent);
                        return bodyContent.getBytes(requestModel.getParams().getParamsEncoding());
                    } catch (UnsupportedEncodingException uee) {
                        LogUtil.d(String.format("Unsupported Encoding while trying to get the bytes of %s using %s",
                                bodyContent, requestModel.getParams().getParamsEncoding()));
                        return null;
                    }
                }
                return super.getBody();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (callback instanceof ParseCallback) {
                    ((ParseCallback) callback).parseStatusCode(response.statusCode);
                    ((ParseCallback) callback).parseHeaders(new NetworkHeader(response.headers));
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return requestModel.getParams().getBodyContentType();
            }
        };

        return request;
    }

    private Response.Listener<String> getListener(final RequestModel requestModel) {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.d(TAG, "Response--->" + result);
                CommonCallback<?> callback = requestModel.getCallback();
                Object resultObject = requestModel.getResultObject();

                //rest有可能delete成功后返回结果是空，但同样属于操作成功
                if (resultObject == null || resultObject == String.class) {
                    callback.onResponse(result);
                    return;
                }
                //说明需要解析成指定类型对象
                Object object = null;
                if (!TextUtils.isEmpty(result)) {
                    try {
                        if (resultObject instanceof Type) {
                            object = new Gson().fromJson(result, (Type) resultObject);
                        } else if (resultObject instanceof Class) {
                            object = new Gson().fromJson(result, (Class) resultObject);
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        //处理JSON解析失败（某些字段返回值和预期不符合） //发送错误日志
                    }
                }

                //数据解析成功
                if (object != null) {
                    callback.onResponse(object);
                } else {
                    //2、是否需要提示？？
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("status_code", 0);
                        jsonObject.put("detail", ResourceUtil.getString(R.string.network_result_is_not_expected));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callback.onErrorResponse(0, jsonObject.toString());
                }
            }
        };

        return listener;
    }

    private Response.ErrorListener getErrorListener(final RequestModel requestModel) {
        final CommonCallback<?> callback = requestModel.getCallback();
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    error.printStackTrace();
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null) {
                        //String errorResult = getErrorContent(requestModel, error);
                        String errorResult = "";
                        try {
                            errorResult = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String logStr = "onError-->responseCode:" + networkResponse.statusCode + "  result:" + errorResult;
                        LogUtil.e(TAG, logStr);
                        LogUtil.saveExceptionInfo(logStr);

                        callback.onErrorResponse(networkResponse.statusCode, errorResult);
                    } else {
                        onNetworkResponseIsNullError(callback, error);
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("status_code", -1);
                        jsonObject.put("detail", ResourceUtil.getString(R.string.unknown_error));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callback.onErrorResponse(-1, jsonObject.toString());
                }
            }
        };

        return errorListener;
    }

    private String getErrorContent(RequestModel requestModel, VolleyError error) {
       /* 200 OK 201 Created 204 No Content
        304 Not Modified
        400 Bad Request 401 Unauthorized 403 Forbidden 404 Not Found 409 Conflict
        500 Internal Server Error*/
        NetworkResponse networkResponse = error.networkResponse;
        String errorResult = "";
        try {
            errorResult = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String logStr = "onError-->responseCode:" + networkResponse.statusCode + "  result:" + errorResult;
        LogUtil.e(TAG, logStr);
        LogUtil.saveExceptionInfo(logStr);

        //服务器异常时，返回错误堆栈信息没意义，需自定义错误信息
        if (networkResponse.statusCode >= 500) {
            errorResult = ResourceUtil.getString(R.string.network_server_error);
            //TODO 并且不是Crash接口报的错误
            String crash = "Unexpected response code " + networkResponse.statusCode + " for " + requestModel.getUrl() + "\n\r" + LogUtil.getExceptionString(error);
            LogUtil.sendLog(crash);
        }

        return errorResult;
    }

    /**
     * 返回值为空的情况
     *
     * @param callback
     * @param error
     */
    private void onNetworkResponseIsNullError(CommonCallback callback, VolleyError error) {
        String message = ResourceUtil.getString(R.string.cannot_connect_server);
        if (error instanceof NoConnectionError) {
            message = ResourceUtil.getString(R.string.network_cannot_connect_server);
        } else if (error instanceof NetworkError) {
            message = ResourceUtil.getString(R.string.network_not_available);
        } else if (error instanceof TimeoutError) {
            message = ResourceUtil.getString(R.string.network_connect_timeout);
        } else if (error instanceof Exception) {
            message = ResourceUtil.getString(R.string.cannot_connect_server);
        }
        callback.onNetworkFailure(message);
    }

    /**
     * 生成链接
     *
     * @param request
     * @return
     */
    private static String generateUrl(RequestModel request) {
        String baseUrl = request.getUrl();
        return new RestUrlBuilder()
                .base(baseUrl.trim())
                .addParams(request.getParams().getQueryStringParams())
                .build();
    }

    @Override
    protected Map<String, String> convertHeaders(RequestModel request) {
        LogUtil.d(TAG, "Request Headers--->" + request.getParams().getHeaders().toString());
        return request.getParams().getHeaders();
    }

    @Override
    protected Map<String, String> convertParams(RequestModel request) {
        LogUtil.d(TAG, "Request BodyParams--->" + request.getParams().getBodyParams());
        return request.getParams().getBodyParams();
    }

    /**
     * 将自定义Method转换成指定Method类型
     *
     * @param requestModel
     * @return
     */
    @Override
    public Integer convertHttpMethod(RequestModel requestModel) {
        String url = generateUrl(requestModel);
        String tag = "[" + requestModel.getTag() + "]";
        RequestModel.Method method = requestModel.getMethod();
        int volley_method = -1;
        String message;
        switch (method) {
            case GET:
                volley_method = Request.Method.GET;
                message = tag + "：<GET> url-->" + url;
                break;
            case POST:
                volley_method = Request.Method.POST;
                message = tag + "：<POST> url-->" + url;
                break;
            case DELETE:
                volley_method = Request.Method.DELETE;
                message = tag + "：<DELETE> url -->" + url;
                break;
            case PUT:
                volley_method = Request.Method.PUT;
                message = tag + "：<PUT> url-->" + url;
                break;
            case OPTIONS:
                volley_method = Request.Method.OPTIONS;
                message = tag + "：<OPTIONS> url-->" + url;
                break;
            case TRACE:
                volley_method = Request.Method.TRACE;
                message = tag + "：<TRACE> url-->" + url;
                break;
            case DEPRECATED_GET_OR_POST:
                volley_method = Request.Method.DEPRECATED_GET_OR_POST;
                message = tag + "：<DEPRECATED_GET_OR_POST> url-->" + url;
                break;
            case PATCH:
                volley_method = Request.Method.PATCH;
                message = tag + "：<PATCH> url-->" + url;
                break;
            default:
                message = tag + "：<Unknown> url-->" + url;
                break;
        }
        LogUtil.d(TAG, message);
        return volley_method;
    }
}
