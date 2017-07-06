package cn.arvin.framework.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by youkang-bd on 2016-08-02.
 */
public class CollectionsUtil {

    private CollectionsUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断List集合是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断Map集合是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断字节数组是否为空
     * @param bytes
     * @return
     */
    public static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }
}
