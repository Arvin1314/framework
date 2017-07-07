package cn.arvin.framework.application;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by youkang-bd on 2016-09-09.
 */
public class NotificationController {
    private static final int DEFAULT_NOTIFICATION_ID = 1000;

    private Context mContext;

    private int mMaxNotificationCount;

    private ArrayList<String> mNotificationTagList;

    private NotificationManager mNotificationManager;

    private NotificationController() {
        mMaxNotificationCount = 5;
        mContext = FrameApplication.get();
        mNotificationTagList = new ArrayList<>();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationController getInstance() {
        return InstanceHolder.INSTANCE;
    }

    static class InstanceHolder {
        private static final NotificationController INSTANCE = new NotificationController();
    }

    public ArrayList<String> getAllNotificationTags() {
        return mNotificationTagList;
    }

    public void clearNotification(String tag, int id) {
        mNotificationManager.cancel(tag, id);
    }

    public void clearAllNotifications() {
        mNotificationManager.cancelAll();
    }

    public void send(String tag, Notification notification) {
        //超过指定数量时，删除最先发送的通知
        while (mNotificationTagList.size() >= mMaxNotificationCount) {
            String tempTag = mNotificationTagList.remove(mNotificationTagList.size() - 1);
            clearNotification(tempTag, DEFAULT_NOTIFICATION_ID);
        }
        mNotificationManager.notify(tag, DEFAULT_NOTIFICATION_ID, notification);

        mNotificationTagList.add(0, tag);
    }
}
