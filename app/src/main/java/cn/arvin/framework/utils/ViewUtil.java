package cn.arvin.framework.utils;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by youkang-bd on 2016-08-15.
 */
public class ViewUtil {
    private ViewUtil() {
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildPullDown(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0
                        || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return canScrollVertically(view, -1);
        }
    }

    /**
     * 判断View是否可以上拉滚动
     *
     * @param view
     * @return
     */
    public boolean canChildPullUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                AbsListView absListView = (AbsListView) view;
                return absListView.getLastVisiblePosition() != absListView.getChildCount() - 1;
            } else if (view instanceof WebView) {
                WebView webview = (WebView) view;
                return webview.getContentHeight() * webview.getScale() != webview.getHeight() + webview.getScrollY();
            } else if (view instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) view;
                View childView = scrollView.getChildAt(0);
                if (childView != null) {
                    return scrollView.getScrollY() != childView.getHeight() - scrollView.getHeight();
                }
            }
        } else {
            return canScrollVertically(view, 1);
        }

        return true;
    }

    /**
     * 用来判断view在竖直方向上能不能向上或者向下滑动
     *
     * @param view      v
     * @param direction 方向    负数代表向上滑动 ，正数则反之
     * @return
     */
    public boolean canScrollVertically(View view, int direction) {
        return ViewCompat.canScrollVertically(view, direction);
    }
}
