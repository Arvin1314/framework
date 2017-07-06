package cn.arvin.framework.constants;

/**
 * @author 23glh
 * @version 1.0
 * @description 程序中用到的一些配置常量的定义
 */
public class Config {
    /**
     * 是否打印日志,用于调试
     */
    public static boolean DEBUG = false;

    /**
     * 是否使用cookie
     */
    public static boolean USE_COOKIE = true;

    /**
     * 文件外部存储的基础地址
     */
    public static final String BASE_DIR = "lecare";

    /**
     * 图片在外部存储上的地址
     */
    public static final String IMAGE_CACHE_DIR = BASE_DIR + "/images";

    /**
     * 视频在外部存储上的地址
     */
    public static final String VIDEO_CACHE_DIR = BASE_DIR + "/videos";

    /**
     * 下载内容在外部存储上的地址
     */
    public static final String DOWNLOAD_CACHE_DIR = BASE_DIR + "/download";

    /**
     * 日志在外部存储上的地址
     */
    public static final String LOG_CACHE_DIR = BASE_DIR + "/log";

    /**
     * 数据库的名称
     */
    public static final String DB_NAME = "lecare.db";

    /**
     * 数据库的名称
     */
    public static final String SP_NAME = "lecare";

    /**
     * 线程池最大线程数
     */
    public static final int MAX_THREAD_COUNT = 4;

    /**
     * 每页显示的数据量
     */
    public static final int PAGE_SIZE = 20;

    /**
     * 获取验证码的间隔时间(秒)
     */
    public static final int FETCH_VALID_CODE_TIME_INTERVAL = 60;

    /**
     * 获取用户消息数量的间隔时间(秒)
     */
    public static final int FETCH_NOTICE_COUNT_TIME_INTERVAL = 60;

    /**
     * 网络连接超时时间（毫秒）
     */
    public static final int CONNECT_TIMEOUT = 15 * 1000;
}
