package cn.arvin.framework.core.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by youkang-bd on 2016-06-30.
 */
public class NetworkHeader {
    private Map<String, String> mHeaders;

    public NetworkHeader() {
        mHeaders = new HashMap<>();
    }

    public NetworkHeader(Map<String, String> headers) {
        mHeaders = headers;
    }

    public void put(String key, String value) {
        mHeaders.put(key, value);
    }

    public String get(String key) {
        return mHeaders.get(key);
    }

    public String remove(String key) {
        return mHeaders.remove(key);
    }

    public void clear() {
        mHeaders.clear();
    }

    public Set<String> keySet() {
        return mHeaders.keySet();
    }

    public boolean containsHeader(String key) {
        return mHeaders.containsKey(key);
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return mHeaders.entrySet();
    }

    public String toString() {
        return mHeaders.toString();
    }
}
