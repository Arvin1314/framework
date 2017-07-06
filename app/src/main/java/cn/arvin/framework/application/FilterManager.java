package cn.arvin.framework.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.LinkedList;

import cn.arvin.framework.core.net.filter.Filter;

/**
 * Created by youkang-bd on 2016-08-23.
 */
public class FilterManager {
    private static LinkedList<Filter> mFilters = new LinkedList<>();

    public static LinkedList<Filter> getFilters() {
        return mFilters;
    }

    public static void init(LinkedList<Filter> filters){
        mFilters = filters;
    }

    public static void init(Context context) {
        try {
            String packageName = context.getPackageName();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            String filterNameString = appInfo.metaData.getString("FILTERS");

            mFilters.clear();
            if (!TextUtils.isEmpty(filterNameString)) {
                String[] filterNameArray = filterNameString.split(",");
                for (int i = 0; i < filterNameArray.length; i++) {
                    String filterName = filterNameArray[i].trim();
                    Filter filter = (Filter) Class.forName(filterName).newInstance();
                    System.out.println("load filter ---->" + filterName);
                    mFilters.add(filter);
                }
            }
            System.out.println("FilterManager init finished ! filters count " + mFilters.size());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Filter Class not found!");
        }
    }
}
