package cn.arvin.framework.application;

/**
 * Created by youkang-bd on 2016-08-23.
 */
public class CacheManager {
    private CacheManager() {
    }

    public static CacheManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    static class InstanceHolder {
        private static final CacheManager INSTANCE = new CacheManager();
    }
}
