package cn.arvin.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.arvin.framework.application.FrameApplication;
import cn.arvin.framework.constants.Config;

public class PreUtil {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = Config.SP_NAME;

    private static final SharedPreferences preferences = FrameApplication.get().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

    private PreUtil() {

    }

    public static void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public static String loadString(String key, String defaultString) {
        return preferences.getString(key, defaultString);
    }

    public static void saveBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean loadBoolean(String key, boolean defaultBoolean) {
        return preferences.getBoolean(key, defaultBoolean);
    }

    public static void saveInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public static int loadInt(String key, int defaultInt) {
        return preferences.getInt(key, defaultInt);
    }

    public static void saveLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public static long loadLong(String key, long defaultLong) {
        return preferences.getLong(key, defaultLong);
    }

    public static <T> void saveList(String key, List<T> list) {
        if (null == list || list.size() <= 0) {
            preferences.edit().putString(key, "").apply();
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list);
        preferences.edit().putString(key, strJson).apply();
    }

    public static <T> List<T> loadList(String key) {
        String strJson = preferences.getString(key, "");
        if (TextUtils.isEmpty(strJson)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
    }
}
