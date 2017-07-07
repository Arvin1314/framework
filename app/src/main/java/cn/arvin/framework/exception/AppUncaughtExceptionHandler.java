package cn.arvin.framework.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.arvin.framework.application.AppManager;
import cn.arvin.framework.application.FrameApplication;
import cn.arvin.framework.utils.LogUtil;


/**
 * 类名称 ：UncaughtException 类描述 ：全局异常的捕获
 */
public enum AppUncaughtExceptionHandler implements UncaughtExceptionHandler {
    INSTANCE;

    /**
     * 捕获并处理异常,我们还可以把异常信息写入文件，以供后来分析。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();

        //showDialog(ex);

        //统计应用Crash次数
        //MobclickAgent.onEvent(mContext, Events.ERROR);

        // 保存异常信息
        LogUtil.saveExceptionInfo(ex);

        //SystemClock.sleep(2000);
        //退出应用
        AppManager.getInstance().exitApplication(FrameApplication.get());
    }

}
