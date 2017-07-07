package cn.arvin.framework.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.Snackbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Type;

import cn.arvin.framework.R;
import cn.arvin.framework.core.net.RequestSender;
import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.RequestParams;
import cn.arvin.framework.core.net.callback.CommonCallback;


public abstract class NormalActivity extends BaseActivity implements View.OnClickListener {
    private View mBaseLayout;
    private FrameLayout mHeadContainer;
    private FrameLayout mBodyContainer;
    private FrameLayout mBodyOverlayContainer;

    protected View mHeadView;
    protected View mBodyView;
    private SparseArray<View> mBodyOverlayViews = new SparseArray<>();

    private long mLastShowTime;
    private String mLastShowContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        mBaseLayout = View.inflate(this, R.layout.activity_layout_base, null);

        setContentView(mBaseLayout);

        dealLogicBeforeInitViews();

        // 界面部分
        mHeadContainer = getView(R.id.head_container);
        mBodyContainer = getView(R.id.body_container);
        mBodyOverlayContainer = getView(R.id.body_overlay_container);

        if (getHeadViewId() != 0) {
            mHeadView = layoutInflater.inflate(getHeadViewId(), mHeadContainer, true);

            initHeadViews(mHeadView);
        } else {
            mHeadContainer.setVisibility(View.GONE);
        }

        mBodyView = layoutInflater.inflate(getBodyViewId(), mBodyContainer, true);

        initBodyViews(mBodyView);

        dealLogicAfterInitViews();

        setListeners();
    }


    /**
     * first step: 在实例化布局之前处理的逻辑,不强制子类重写，需要时可重写
     */
    protected void dealLogicBeforeInitViews() {
    }

    /**
     * 获取界面Header要显示的内容布局文件的id,子类可以通过重写返回0可去掉头部Header
     *
     * @return 标题栏部分要显示的内容布局文件的id
     */
    public abstract int getHeadViewId();

    /**
     * second step:获取界面主体部分要显示的内容布局文件的id
     *
     * @return 标题栏以下部分要显示的内容布局文件的id
     */
    public abstract int getBodyViewId();

    /**
     * 初始化头部View的方法
     *
     * @param head
     */
    protected abstract void initHeadViews(View head);


    /**
     * 初始化标题栏下面主体的内容部分
     */
    public abstract void initBodyViews(View body);


    /**
     * 在实例化布局之后处理的逻辑，执行此方法时界面中的控件均已经实例化
     */
    protected void dealLogicAfterInitViews() {
    }

    /**
     * 可以在该方法中统一给控件设置监听事件
     */
    protected abstract void setListeners();

    /**
     * onClick方法的封装，在此方法中统一处理点击事件
     *
     * @param viewId
     * @param view
     */
    protected abstract void onClickEvent(int viewId, View view);

    @Override
    public void onClick(View view) {
        onClickEvent(view.getId(), view);
    }

    /**
     * 显示界面上的覆盖物，比如正在加载或者加载失败界面
     */
    protected void showBodyOverlay(int overlayViewId) {
        if (overlayViewId != 0) {
            View overlayView = mBodyOverlayViews.get(overlayViewId);
            if (overlayView == null) {
                overlayView = View.inflate(this, overlayViewId, null);
                mBodyOverlayViews.put(overlayViewId, overlayView);
            }

            mBodyOverlayContainer.removeAllViews();
            mBodyOverlayContainer.addView(overlayView);
            mBodyOverlayContainer.setVisibility(View.VISIBLE);

            //显示覆盖的界面，子类重写，实现相关功能
            onBodyOverlayShown(overlayViewId, overlayView);
        }
    }

    /**
     * 当覆盖界面显示的时候回调
     *
     * @param overlayId
     * @param overlayView
     */
    protected abstract void onBodyOverlayShown(int overlayId, View overlayView);


    /**
     * 隐藏覆盖的界面
     */
    protected void dismissBodyOverlay() {
        mBodyOverlayContainer.removeAllViews();
        mBodyOverlayContainer.setVisibility(View.GONE);
    }

    /**
     * 根据控件id将控件实例化，利用泛型，省略了findViewById时强制类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 根据控件id将控件实例化，利用泛型，省略了findViewById时强制类型转换
     *
     * @param parent
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <T extends View> T getView(View parent, int id) {
        return (T) parent.findViewById(id);
    }


    /**
     * 显示snackBar消息提示
     *
     * @param msg
     */
    public final void showSnackBar(String msg) {
        showSnackBar(mBaseLayout, msg);
    }

    /**
     * 显示snackBar消息提示
     *
     * @param v
     * @param msg
     */
    public final void showSnackBar(View v, String msg) {
        try {
            if (v != null) {
                //同样的内容，2秒内重复显示时，第二次显示的无效，这里自己修改条件控制
                if (!mLastShowContent.equals(msg) || System.currentTimeMillis() - mLastShowTime > 2000) {
                    Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
                    initSnackBar(snackbar);
                    snackbar.show();
                    mLastShowContent = msg;
                    mLastShowTime = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSnackBar(String action, String msg) {
        try {
            final Snackbar snackbar = Snackbar.make(mBaseLayout, msg, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            initSnackBar(snackbar);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSnackBar(Snackbar snackbar) {
        try {
            Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout) snackBarLayout.getChildAt(0);
            /*Class[] types = {};
            Object[] args = {};
            Method method = Snackbar.SnackbarLayout.class.getDeclaredMethod("getMessageView", types);
            method.setAccessible(true);
            TextView textView = (TextView) method.invoke(snackBarLayout, args);*/
            TextView textView = snackbarContentLayout.getMessageView();
            SnackbarContentLayout.LayoutParams layoutParams = (SnackbarContentLayout.LayoutParams) textView.getLayoutParams();
            textView.setLayoutParams(layoutParams);
            textView.setMaxLines(3);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --------------------------------------------------------------------------------

    /**
     * 发送get请求，返回字符串形式的请求结果
     *
     * @param url
     * @param callback
     */
    public void stringGetRequest(String url, CommonCallback<String> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.GET, callback);
        requestModel.setTag(getTag());
        RequestSender.sendRequest(requestModel);
    }

    /**
     * 发送post请求，返回字符串形式的请求结果
     *
     * @param url
     * @param params
     * @param callback
     */
    public void stringPostRequest(String url, RequestParams params, CommonCallback<String> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.POST, params, callback);
        requestModel.setTag(getTag());
        RequestSender.sendRequest(requestModel);
    }

    /**
     * 发送get请求，返回指定类型对象形式的请求结果
     *
     * @param url
     * @param clazz
     * @param callback
     */
    public void objectGetRequest(String url, Class<?> clazz, CommonCallback<?> callback) {
        objectGetRequest(url, null, clazz, callback);
    }

    /**
     * 发送get请求，返回指定类型对象形式的请求结果
     *
     * @param url
     * @param clazz
     * @param callback
     */
    public void objectGetRequest(String url, RequestParams params, Class<?> clazz, CommonCallback<?> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.GET, params, clazz, callback);
        requestModel.setTag(getTag());
        RequestSender.sendRequest(requestModel);
    }

    /**
     * 发送post请求，返回指定类型对象形式的请求结果
     *
     * @param url
     * @param params
     * @param clazz
     * @param callback
     */
    public void objectPostRequest(String url, RequestParams params, Class<?> clazz, CommonCallback<?> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.POST, params, clazz, callback);
        requestModel.setTag(getTag());
        RequestSender.sendRequest(requestModel);
    }


    //----------------------------根据TypeToken解析--------------------------//
    public void objectGetRequest(String url, Type jsonType, CommonCallback<?> callback) {
        objectGetRequest(url, null, jsonType, callback);
    }

    public void objectGetRequest(String url, RequestParams params, Type jsonType, CommonCallback<?> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.GET, params, jsonType, callback);
        requestModel.setTag(getTag());
        RequestSender.sendRequest(requestModel);
    }




}
