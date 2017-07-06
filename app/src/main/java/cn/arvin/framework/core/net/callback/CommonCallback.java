package cn.arvin.framework.core.net.callback;

import android.text.TextUtils;

import com.google.gson.Gson;

import cn.arvin.framework.R;
import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.base.BaseCallback;
import cn.arvin.framework.core.net.bean.BaseErrorBean;
import cn.arvin.framework.utils.ResourceUtil;
import cn.arvin.framework.utils.ToastUtil;


public abstract class CommonCallback<ResultType> implements BaseCallback {

    private RequestModel mRequest;

    @SuppressWarnings("unchecked")
    @Override
    public final void onResponse(Object response) {
        try {
            if (!mRequest.isCanceled()) {
                onSuccess((ResultType) response);
            } else {
                onCanceled();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onFinished();
        }
    }

    @Override
    public final void onErrorResponse(int statusCode, String errorResult) {
        //异常处理，防止出现异常，onFinish不执行
        try {
            if (!mRequest.isCanceled()) {
                BaseErrorBean errorBean = new Gson().fromJson(errorResult, BaseErrorBean.class);
                if (TextUtils.isEmpty(errorBean.getDetail())) {
                    ToastUtil.show(ResourceUtil.getString(R.string.network_server_error));
                } else {
                    ToastUtil.show(String.valueOf(errorBean.getDetail()));
                }
                onServerError(statusCode, errorBean.getDetail());
            } else {
                onCanceled();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(ResourceUtil.getString(R.string.network_server_error));
            onServerError(statusCode, errorResult);
        } finally {
            onFinished();
        }
    }

    @Override
    public final void onNetworkFailure(String msg) {
        //异常处理，防止出现异常，onFinish不执行
        try {
            if (!mRequest.isCanceled()) {
                onNetworkError(String.valueOf(msg));
            } else {
                onCanceled();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onFinished();
        }
    }

    @Override
    public void onCanceled() {

    }

    public abstract void onSuccess(ResultType response);

    public void onServerError(int statusCode, String msg) {

    }

    public void onNetworkError(String msg) {
        ToastUtil.show(String.valueOf(msg));
    }

    public RequestModel getRequest() {
        return mRequest;
    }

    public void setRequest(RequestModel request) {
        mRequest = request;
    }
}