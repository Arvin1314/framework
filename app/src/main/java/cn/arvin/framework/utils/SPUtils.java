package cn.arvin.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.arvin.framework.application.FrameApplication;
import cn.arvin.framework.constants.Config;


public class SPUtils {
    private SPUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = Config.SP_NAME;

    private static final SharedPreferences sSharedPreferences = FrameApplication.get().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();

        if (object == null) {
            throw new IllegalArgumentException("传人的值不能为null");
        }

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sSharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sSharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sSharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sSharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sSharedPreferences.getLong(key, (Long) defaultObject);
        }

        return null;
    }


    public static void saveString(String key, String value) {
        sSharedPreferences.edit().putString(key, value).apply();
    }

    public static String loadString(String key, String defaultString) {
        return sSharedPreferences.getString(key, defaultString);
    }

    public static void saveBoolean(String key, boolean value) {
        sSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean loadBoolean(String key, boolean defaultBoolean) {
        return sSharedPreferences.getBoolean(key, defaultBoolean);
    }

    public static void saveInt(String key, int value) {
        sSharedPreferences.edit().putInt(key, value).apply();
    }

    public static int loadInt(String key, int defaultInt) {
        return sSharedPreferences.getInt(key, defaultInt);
    }

    public static void saveLong(String key, long value) {
        sSharedPreferences.edit().putLong(key, value).apply();
    }

    public static long loadLong(String key, long defaultLong) {
        return sSharedPreferences.getLong(key, defaultLong);
    }

    public static <E> void saveList(String key, List<E> list) {
        if (CollectionsUtil.isEmpty(list)) {
            sSharedPreferences.edit().putString(key, "").apply();
        }else{
            Gson gson = new Gson();
            //转换成json数据，再保存
            String strJson = gson.toJson(list);
            sSharedPreferences.edit().putString(key, strJson).apply();
        }
    }

    public static <E> List<E> loadList(String key) {
        String strJson = sSharedPreferences.getString(key, "");
        if (TextUtils.isEmpty(strJson)) {
            return new ArrayList<E>();
        }
        Gson gson = new Gson();
        return gson.fromJson(strJson, new TypeToken<List<E>>() {
        }.getType());
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        Context context = FrameApplication.get();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        Context context = FrameApplication.get();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        Context context = FrameApplication.get();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        Context context = FrameApplication.get();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                LogUtil.e(e.getMessage());
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
                LogUtil.e(e.getMessage());
            } catch (IllegalAccessException e) {
                LogUtil.e(e.getMessage());
            } catch (InvocationTargetException e) {
                LogUtil.e(e.getMessage());
            }
            editor.commit();
        }
    }
}