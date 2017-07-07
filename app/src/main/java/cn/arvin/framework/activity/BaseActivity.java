package cn.arvin.framework.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import cn.arvin.framework.application.AppManager;
import cn.arvin.framework.application.FrameApplication;
import cn.arvin.framework.utils.LogUtil;
import cn.arvin.framework.utils.ToastUtil;


abstract class BaseActivity extends AppCompatActivity {

    private String TAG;

    protected Activity mActivity;

    protected FrameApplication mApplication;

    protected AppManager mAppManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getActivityName();

        mActivity = this;

        mApplication = FrameApplication.get();

        mAppManager = AppManager.getInstance();

        mAppManager.addActivity(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击空白区域 自动隐藏软键盘
        View view = this.getCurrentFocus();
        if (null != view) {
            return dismissSoftKeyboard();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        mAppManager.removeActivity(this);
        super.onDestroy();
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
     * 是否全屏和显示标题，true为全屏和无标题，false为无标题，请在setContentView()方法前调用
     *
     * @param fullScreen
     */
    protected void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    public void showKeyboard(View view) {
        try {
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManage.showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏键盘
     */
    public boolean dismissSoftKeyboard() {
        try {
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (this.getCurrentFocus() == null) {
                return inputMethodManage.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
            } else {
                return inputMethodManage.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 此处返回一个Tag标识，每一个request都有一个Tag标识，标识该联网线程是在哪个Activity中执行的
     * 如果该Activity被移出栈顶了，那么所有该activity的联网都应该被停止，可以利用这个标示来指定哪些网络请求被取消
     *
     * @return 建议返回所在activity的类名——如"MainActivity"
     */
    public final String getTag() {
        if (TextUtils.isEmpty(TAG)) {
            TAG = getActivityName();
        }
        return TAG;
    }

    protected String getActivityName() {
        return this.getClass().getSimpleName();
    }


    /**
     * 显示toast通知
     *
     * @param msg 显示的内容
     */
    public final void showToast(CharSequence msg) {
        ToastUtil.show(msg);
    }

    public final void showToast(int resId) {
        ToastUtil.show(resId);
    }

    /**
     * 打印日志
     *
     * @param msg 打印的内容
     * @return void
     */
    public final void LogE(String msg) {
        LogUtil.e(getTag(), msg);
    }

    public final void LogD(String msg) {
        LogUtil.d(getTag(), msg);
    }

    public final void LogI(String msg) {
        LogUtil.i(getTag(), msg);
    }

    public final void LogV(String msg) {
        LogUtil.v(getTag(), msg);
    }
}
