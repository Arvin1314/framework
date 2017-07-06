package cn.arvin.framework.application;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.Stack;

import cn.arvin.framework.core.net.impl.VolleyNetwork;
import cn.arvin.framework.utils.CollectionsUtil;
import cn.arvin.framework.utils.LogUtil;

/**
 * 类名称 ：AppManager 类描述 ：应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static Stack<Activity> sActivityStack;

    private static AppManager sManagerInstance = new AppManager();

    private RequestQueue mRequestQueue;

    private AppManager() {

    }

    /**
     * 获取AppManager单一实例
     */
    public static AppManager getInstance() {
        return sManagerInstance;
    }

    /**
     * 获取RequestQueue单例
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = VolleyNetwork.getInstance().getRequestQueue();
            LogUtil.d("RequestQueue created：---->" + mRequestQueue);
        }
        return mRequestQueue;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     * 从堆栈里删除对应的Activity
     */
    public void removeActivity(Activity activity) {
        if (sActivityStack != null) {
            sActivityStack.remove(activity);
        }

        LogUtil.d(activity.getClass().getSimpleName() + "被销毁，移除Activity栈！");

        //取消对应界面的网络请求
        final String tag = activity.getClass().getSimpleName();
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return tag.equals(request.getTag());
            }
        });
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        if (sActivityStack != null) {
            Activity activity = sActivityStack.lastElement();
            return activity;
        }
        return null;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        Activity activity = sActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (!CollectionsUtil.isEmpty(sActivityStack)) {
            // 用迭代器直接遍历之后，最后清空
            for (int i = sActivityStack.size() - 1; i >= 0; i--) {
                Activity activity = sActivityStack.get(i);
                finishActivity(activity);
            }
            sActivityStack.clear();
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApplication(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //保存友盟统计的数据
            //MobclickAgent.onKillProcess(mContext);

            activityMgr.killBackgroundProcesses(context.getPackageName());
            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());

            //System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
