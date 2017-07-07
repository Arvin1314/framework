package cn.arvin.framework.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.Snackbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.arvin.framework.R;
import cn.arvin.framework.widget.HeaderBar;


public abstract class FrameActivity extends BaseActivity implements View.OnClickListener {

    public static final int INIT_HEADER = -1;

    public static final int NO_HEADER = 0;

    private View mBaseLayout;
    private FrameLayout mHeadContainer;
    private FrameLayout mBodyContainer;
    private FrameLayout mBodyOverlayContainer;

    protected View mHeadView;
    protected View mBodyView;
    protected ImageView mShadow;
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

        if (getHeadViewId() == NO_HEADER) {
            mHeadContainer.setVisibility(View.GONE);
        } else {
            if (getHeadViewId() == INIT_HEADER) {
                mHeadView = new HeaderBar(this);
                mHeadContainer.addView(mHeadView);
            } else {
                mHeadView = layoutInflater.inflate(getHeadViewId(), mHeadContainer, true);
            }

            initHeadViews(mHeadView);
        }

        mBodyView = layoutInflater.inflate(getBodyViewId(), mBodyContainer, true);

        setShadow(getHeadViewId() != NO_HEADER);

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
    protected int getHeadViewId() {
        return INIT_HEADER;
    }

    /**
     * 初始化头部View的方法
     *
     * @param head
     */
    protected void initHeadViews(View head) {
    }

    /**
     * second step:获取界面主体部分要显示的内容布局文件的id
     *
     * @return 标题栏以下部分要显示的内容布局文件的id
     */
    public abstract int getBodyViewId();

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


    protected void setShadow(boolean show) {
        if (mShadow == null) {
            if (!show) {
                return;
            }
            mShadow = new ImageView(this);
            mShadow.setImageResource(R.mipmap.header_shadow);
        }
        mBodyContainer.removeView(mShadow);

        if (mHeadView != null && mHeadView instanceof HeaderBar) {
            ((HeaderBar) mHeadView).setDividerVisible(!show);
        }

        if (show) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mBodyContainer.addView(mShadow, params);
        }
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

}
