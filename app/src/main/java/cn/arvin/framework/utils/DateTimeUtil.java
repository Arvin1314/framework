package cn.arvin.framework.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {

    private DateTimeUtil() {

    }

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式：yyyy年MM月dd日
     **/
    public static final String DFCN_YYYY_MM_DD = "yyyy'年'MM'月'dd'日'";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String DF_HH_MM = "HH:mm";

    /**
     * 日期格式：yyyy-MM-dd'T'HH:mm:ssZ
     **/
    public static final String DF_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

    public static final long MINUTE = 60 * 1000;// 1分钟
    public static final long HOUR = 60 * MINUTE;// 1小时
    public static final long DAY = 24 * HOUR;// 1天
    public static final long MONTH = 31 * DAY;// 月
    public static final long YEAR = 12 * MONTH;// 年

    private static final String TAG = "DateTimeUtil";

    /**
     * 修改系统时间，前提是android系统已经root
     *
     * @param milliseconds
     */
    @SuppressLint("SimpleDateFormat")
    public static void setSystemTime(Long milliseconds) {
        try {
            String datetime = "";
            if (milliseconds == 0) {
                return;
            }

            Date date = new Date(milliseconds);
            SimpleDateFormat yyyy = new SimpleDateFormat("yyyyMMdd.HHmmss");
            datetime = yyyy.format(date);

            Process process = Runtime.getRuntime().exec("su");

            // String datetime="20131023_112800";//测试的设置的时间【时间格式yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT Asia/Shanghai\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            LogUtil.e("设置系统时间失败");
        }
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > MONTH) {
            r = (diff / MONTH);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > HOUR) {
            r = (diff / HOUR);
            return r + "个小时前";
        }
        if (diff > MINUTE) {
            r = (diff / MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将ISO8601时间转为yyyy-MM-dd HH:mm:ss输出
     *
     * @param isoDate
     * @return
     */
    public static String formatSimple(String isoDate) {
        if (TextUtils.isEmpty(isoDate)) {
            return null;
        }
        String destDate = "";
        try {
            SimpleDateFormat formatISO = new SimpleDateFormat(DF_ISO8601);
//            formatISO.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            Date date = formatISO.parse(isoDate);
            SimpleDateFormat format = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM);
            destDate = format.format(date);
//            String currentDate = format.format(new Date());
//            if (currentDate.equals(destDate)) {// 当日
//                SimpleDateFormat f = new SimpleDateFormat(DF_HH_MM);
//                destDate = f.format(date);
//            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return destDate;
    }


    /**
     * 将给定的剩余时间毫秒值转化成倒计时形式的字符串
     *
     * @param leftTimeInMillions 剩余时间的毫秒值
     * @return 剩余时间毫秒值转化成的倒计时形式的字符串
     */
    public static String getCountDownTime(long leftTimeInMillions) {
        StringBuilder countDown = new StringBuilder();
        long intervalTime = leftTimeInMillions / 1000; // 间隔时间，单位转化成秒

        long day = intervalTime / (24 * 3600);// 转换成天
        long hour = intervalTime % (24 * 3600) / 3600;// 转化成小时
        long minute = intervalTime % (3600) / 60;// 转换成分
        long second = intervalTime % 60;// 转化成秒

        if (day > 0) {
            countDown.append(day + "天");
        }
        countDown.append(hour + "时" + (minute + 1) + "分" + second + "秒");
        return countDown.toString();
    }


    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param milliseconds 时间的毫秒值
     * @return
     */
    public static String formatDateTime(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date date = new Date(milliseconds);
        return sdf.format(date);
    }


    /**
     * 将当前时间格式化成指定格式
     *
     * @param format
     * @return
     */
    public static String formatCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * 将日期以format样式格式化
     *
     * @param milliseconds
     * @param format
     * @return
     */
    public static String formatDateTime(long milliseconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(milliseconds));
    }


    /**
     * 将日期以format格式化
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型
     */
    public static Date parseDate(String strDate) {
        DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            Log.v(TAG, "parseDate failed !");
        }
        return returnDate;

    }


    /**
     * 对日期进行增加操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date addDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对日期进行相减操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date subDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }

        return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
    }


    /**
     * 获取当前的星期数
     *
     * @return
     */
    public static String getWeek() {
        int weekDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
        String myWeek = null;
        switch (weekDay) {
            case 1:
                myWeek = "日";
                break;
            case 2:
                myWeek = "一";
                break;
            case 3:
                myWeek = "二";
                break;
            case 4:
                myWeek = "三";
                break;
            case 5:
                myWeek = "四";
                break;
            case 6:
                myWeek = "五";
                break;
            case 7:
                myWeek = "六";
                break;
            default:
                break;
        }

        return "周" + myWeek;
    }
}
