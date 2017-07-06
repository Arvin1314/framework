package cn.arvin.framework.application;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arvin on 2016/3/15.
 */
public class AppCookieManager extends CookieHandler {

    private CookiePolicy policyCallback = CookiePolicy.ACCEPT_ALL;

    public AppCookieManager(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
    }

    public AppCookieManager(Context context, CookiePolicy cookiePolicy) {
        this(context);
        if (cookiePolicy != null) {
            policyCallback = cookiePolicy;
        }
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        //将之前返回的cookie持久化到WebView，这里访问的时候从WebViewCookieManager中取出
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList();
        String cookie = CookieManager.getInstance().getCookie(uri.toString());
        if (null != cookie && !cookie.isEmpty()) {
            list.add(cookie);
        }
        map.put("Cookie", list);
//        LogUtil.e("get\t" + uri + "\tcookie-->" + map.toString());
        return map;
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        //将http net 访问返回的cookie持久化到本地，这里是使用webView的CookieManager存储到本地数据库
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            String key = entry.getKey();
            if (null == key) {
                continue;
            }

            if ("set-cookie".equalsIgnoreCase(key) || "set-cookie2".equalsIgnoreCase(key)) {
                for (String cookie : entry.getValue()) {
                    addCookie(uri, cookie);
                }
            }
        }

    }

    private void addCookie(URI uri, String cookie) {
//        LogUtil.e("put\t" + uri + "\tcookie-->" + cookie);
        CookieManager.getInstance().setCookie(uri.toString(), cookie);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        }
    }

    public void clearCookies() {
        CookieManager.getInstance().removeAllCookie();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        }
    }

}
