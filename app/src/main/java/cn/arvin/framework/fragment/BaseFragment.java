package cn.arvin.framework.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Type;

import cn.arvin.framework.R;
import cn.arvin.framework.core.RequestSender;
import cn.arvin.framework.core.net.RequestModel;
import cn.arvin.framework.core.net.RequestParams;
import cn.arvin.framework.core.net.callback.CommonCallback;
import cn.arvin.framework.utils.LogUtil;


/**
 * fragment基类
 * Activity           fragment
 * onCreated() -----> onAttach()
 * -----------------> onCreate()
 * -----------------> onCreateView()
 * -----------------> onActivityCreated()
 * <p>
 * onStarted() -----> onStart()
 * <p>
 * onResumed() -----> onResume()
 * <p>
 * onPaused()  -----> onPause()
 * <p>
 * onStopped() -----> onStop()
 * <p>
 * onDestroyed()----> onDestroyView()
 * -----------------> onDestroy()
 * -----------------> onDetach()
 *
 * @author WWN
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
    protected String TAG = this.getClass().getSimpleName();
    protected LayoutInflater mInflater;

    protected Activity mActivity;

    private ViewGroup mContentView;
    private FrameLayout mBodyContainer;
    private FrameLayout mBodyOverlayContainer;
    private SparseArray<View> mBodyOverlayViews = new SparseArray<>();

    protected RequestModel mRequest;


    //用于友盟统计在Fragment中使用
    private boolean mPageStarted = false;

    //是否需要延迟加载
    protected boolean isLazyLoad = true;

    //是否已经加载完成
    protected boolean isAlreadyLoaded = false;

    //是否已经对用户可见过
    private boolean isAlreadyVisibleToUser = false;

    //Activity是否已经创建完
    private boolean onActivityCreated = false;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mActivity = (Activity) activity;
        LogUtil.d(TAG, "-------->  onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "-------->  onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(TAG, "-------->  onCreateView");
        mInflater = inflater;
        if (null == mContentView) {
            mContentView = (RelativeLayout) View.inflate(mActivity, R.layout.fragment_layout_base, null);

            //获取显示真正布局的容器
            mBodyContainer = getView(mContentView, R.id.body_container);

            // 获取覆盖层的容器
            mBodyOverlayContainer = getView(mContentView, R.id.body_overlay_container);

            //不需要延迟加载，则直接将真正布局页面显示进去
            if (!isLazyLoad) {
                onFirstVisibleToUser();
            } else {
                //如果需要延迟加载，加载之前需要显示的页面
                if (getPreviewLayoutId() != 0) {
                    showBodyOverlay(getPreviewLayoutId());
                }
            }
        }

        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(TAG, "-------->  onActivityCreated");

        onActivityCreated = true;
        // 当setUserVisibleHint先于onCreateView调用时，由于onCreateView还没有执行，
        // 所以setUserVisibleHint中没有调用onVisibleToUser(界面没有创建)，所以在onActivityCreated中(此时onCreateView已执行)
        // 对onVisibleToUser进行调用
        if (isAlreadyVisibleToUser) {
            onFirstVisibleToUser();
        }
    }

    protected void onFirstVisibleToUser() {
        //当界面对用户可见时，上面onCreateView中就没有加载真正的布局，这里第一次界面可见的时候加载
        if (!isAlreadyLoaded) {
            LogUtil.d(TAG, "onFirstVisibleToUser---------->load real layout view");

            mInflater.inflate(getContentId(), mBodyContainer);

            //当真正界面加载的时候
            onLoadRealView();

            //清除掉Overlay
            if (isLazyLoad && getPreviewLayoutId() != 0) {
                //dismissBodyOverlay();
                LogUtil.d(TAG, "dismissBodyOverlay---------->");
            }

            dealLogicOnFirstVisibleToUser();

            isAlreadyLoaded = true;

            //在这执行一次，可以保证onVisibleToUser时，真正界面已经加载，使用界面的控件时不会报null异常
            if (!mPageStarted) {
                mPageStarted = true;
                onVisibleToUser();
            }
        }
    }

    /**
     * 当真正界面被加载时
     */
    private void onLoadRealView() {
        dealLogicBeforeInitViews();

        initContentViews(getView());

        dealLogicAfterInitViews();

        setListeners();
    }

    /**
     * 当界面第一次被用户可见时执行
     */
    protected void dealLogicOnFirstVisibleToUser() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtil.d(TAG, "setUserVisibleHint---------> " + isVisibleToUser);
        if (isVisibleToUser) {
            // 界面原先不可见到可见，并且已经初始化好界面控件，执行一次,android 4.0系统中存在下面问题
            // (onAttach->onCreate->onCreateView->onActivityCreated->setUserVisibleHint)//正常顺序
            // (onAttach->onCreate->setUserVisibleHint->onCreateView->onActivityCreated)//ViewPager第一个
            // (1)isAlreadyVisibleToUser = false; -->onActivityCreated 中 onVisibleToUser不执行;
            //    onActivityCreated = true; -->setUserVisibleHint中onVisibleToUser()执行;
            // (2)onActivityCreated = false; -->setUserVisibleHint中onVisibleToUser()不执行;
            //    isAlreadyVisibleToUser = true; -->onActivityCreated 中 onVisibleToUser执行;
            // 最终--->onVisibleToUser中根据isAlreadyLoaded值只执行一次
            // 这里解决的问题是：当setUserVisibleHint先于onCreateView执行时，initContentViews中的ContentView还没创建
            // 但是onActivityCreated一定是在onCreateView之后执行，所以判断onActivityCreated是否执行过了，
            // 否则就在onActivityCreated里执行延迟加载
            // 还没有对用户可见过
            isAlreadyVisibleToUser = true;
            if (onActivityCreated) {
                onFirstVisibleToUser();
            }

            //Fragment可见(第一次可见的时候已经在onFirstVisibleToUser中执行了一次)，并且没有调用onPageStart()或者已经调用了onPageEnd();
            if (!mPageStarted && isAlreadyLoaded) {
                mPageStarted = true;
                onVisibleToUser();
            }
        } else {
            //Fragment不可见，并且已经调用onPageStart()
            if (mPageStarted) {
                onInVisibleToUser();
                mPageStarted = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtil.d(TAG, "onResume---------> userVisibleHint" + getUserVisibleHint());
        if (getUserVisibleHint()) {
            //Fragment可见，并且没有调用onPageStart()
            if (!mPageStarted) {
                onVisibleToUser();
                mPageStarted = true;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //LogUtil.d(TAG, "onPause---------> userVisibleHint"+ getUserVisibleHint());
        if (getUserVisibleHint()) {
            onInVisibleToUser();
            mPageStarted = false;
        }
    }

    /**
     * 显示界面上的覆盖物，比如正在加载或者加载失败界面
     */
    protected void showBodyOverlay(int overlayViewId) {
        if (overlayViewId != 0) {
            View overlayView = mBodyOverlayViews.get(overlayViewId);
            if (overlayView == null) {
                overlayView = View.inflate(mActivity, overlayViewId, null);
                mBodyOverlayViews.put(overlayViewId, overlayView);
            }

            int count = mBodyOverlayContainer.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = mBodyOverlayContainer.getChildAt(i);
                //说明要显示的界面已经正在显示，则不需显示
                if (childView == overlayView) {
                    return;
                }
            }

            mBodyOverlayContainer.removeAllViews();
            mBodyOverlayContainer.addView(overlayView);
            mBodyOverlayContainer.setVisibility(View.VISIBLE);

            //显示覆盖的界面，子类重写，实现相关功能
            onBodyOverlayShown(overlayViewId, overlayView);
        }
    }

    /**
     * 隐藏覆盖的界面
     */
    protected void dismissBodyOverlay() {
        mBodyOverlayContainer.removeAllViews();
        mBodyOverlayContainer.setVisibility(View.GONE);
    }

    /**
     * 当页面对用户可见，并且onResume()
     */
    protected abstract void onVisibleToUser();

    /**
     * 页面对用户不可见，或者页面对用户可见但是onPause()
     */
    protected abstract void onInVisibleToUser();

    /**
     * 获取布局文件ID
     *
     * @return
     */
    public abstract int getContentId();

    /**
     * 界面延迟加载时显示的预览界面
     *
     * @return
     */
    protected abstract int getPreviewLayoutId();

    /**
     * 在实例化布局之前处理的逻辑,不强制子类重写，需要时可重写
     */
    protected void dealLogicBeforeInitViews() {
    }

    /**
     * 初始化组件
     *
     * @param view
     */
    public abstract void initContentViews(View view);

    /**
     * 在实例化布局之后处理的逻辑
     */
    protected void dealLogicAfterInitViews() {
    }

    public void setRequest(RequestModel mRequest) {
        this.mRequest = mRequest;
    }

    /**
     * 当覆盖界面显示的时候回调
     *
     * @param overlayId
     * @param overlayView
     */
    protected abstract void onBodyOverlayShown(int overlayId, View overlayView);

    /**
     * 给控件设置监听事件
     */
    protected abstract void setListeners();


    /**
     * onClick方法的封装，在此方法中处理点击事件
     *
     * @param view
     */
    protected abstract void onClickEvent(int viewId, View view);

    @Override
    public void onClick(View v) {
        onClickEvent(v.getId(), v);
    }


    /**
     * 此处一定要返回flag标识，每一个handler都有一个flag标识，标识该联网线程是在哪个fragment中执行的
     * 如果该fragment被销毁了，那么所有该activity的联网都会停止
     *
     * @return 建议返回所在activity的类名——如"BaseFragment"
     */
    public final String getFlag() {
        return TAG;
    }

    /**
     * Fragment中的布局被移除时调用。表示fragment销毁相关联的UI布局
     * 重写onDestroyView的方法，将当前的rootView从父容器中移除，并且把所有正在进行的网络请求停止掉 有四个 fragment
     * 当滑动到第四页的时候 第一页执行onDestroyView(),但没有执行onDestroy。
     * 他依然和activity关联。当在滑动到第一页的时候又执行了 onCreateView()。 会出现重复加载view的局面，
     * 所以这里先将rootView从父容器中移除
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        //ButterKnife.unbind(this);
        //NetworkVolley.getInstance().getRequestQueue().cancelAll(this.getFlag());
    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(mActivity, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 利用泛型，省略了findViewByID的强制类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(View view, int id) {
        try {
            return (E) view.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    public final <E extends View> E getView(int id) {
        try {
            return (E) getView().findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }


    /**
     * 发送get请求，返回字符串形式的请求结果
     *
     * @param url
     * @param callback
     */
    public void stringGetRequest(String url, CommonCallback<String> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.GET, callback);
        requestModel.setTag(TAG);
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
        requestModel.setTag(TAG);
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
        requestModel.setTag(TAG);
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
        requestModel.setTag(TAG);
        RequestSender.sendRequest(requestModel);
    }


    //----------------------------根据TypeToken解析--------------------------//
    public void objectGetRequest(String url, Type jsonType, CommonCallback<?> callback) {
        objectGetRequest(url, null, jsonType, callback);
    }

    public void objectGetRequest(String url, RequestParams params, Type jsonType, CommonCallback<?> callback) {
        RequestModel requestModel = new RequestModel(url, RequestModel.Method.GET, params, jsonType, callback);
        requestModel.setTag(TAG);
        RequestSender.sendRequest(requestModel);
    }
}
