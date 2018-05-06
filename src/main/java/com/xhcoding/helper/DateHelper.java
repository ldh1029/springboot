package com.xhcoding.helper;

import org.apache.commons.lang3.Validate;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类.
 * <p>
 * 在不方便使用joda-time时，使用本类降低Date处理的复杂度与性能消耗, 封装Common Lang及移植Jodd的最常用日期方法
 *
 * @author calvin
 */
public class DateHelper {

    public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YMD_CH = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_YMD_NUM = "yyyyMMdd";
    public static final String DATE_FORMAT_HMS_NUM = "HHmmss";
    public static final String DATE_FORMAT_YMDHMS_NUM = "yyyyMMddHHmmss";
    private static SimpleDateFormat dateFormat = null;

    public static final long MILLIS_PER_SECOND = 1000; // Number of milliseconds in a standard second.

    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND; // Number of milliseconds in a standard minute.

    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE; // Number of milliseconds in a standard hour.

    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR; // Number of milliseconds in a standard day.

    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //////// 日期比较 ///////////

    /**
     * 是否同一天.
     *
     * @see org.apache.commons.lang3.time.DateUtils#isSameDay(Date, Date)
     */
    public static boolean isSameDay(final Date date1, final Date date2) {
        return org.apache.commons.lang3.time.DateUtils.isSameDay(date1, date2);
    }

    /**
     * 是否同一时刻.
     */
    public static boolean isSameTime(final Date date1, final Date date2) {
        // date.getMillisOf() 比date.getTime()快
        return date1.compareTo(date2) == 0;
    }


    /**
     * 返回时间差 （天）
     *
     * @param date1
     * @param date
     * @param absolute 是否返回绝对值
     * @return
     */
    public static int diffInDays(Date date1, Date date, boolean absolute) {
        long time1 = date1.getTime();
        long time2 = date.getTime();
        int days = (int) ((time1 - time2) / (1000 * 60 * 60 * 24));
        if (absolute) {
            days = Math.abs(days);
        }
        return days;
    }

    /**
     * 创建时间
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return
     */
    public static Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Date tempDate = new Date();
        tempDate = DateHelper.setYears(tempDate, year);
        tempDate = DateHelper.setMonths(tempDate, month - 1);
        tempDate = DateHelper.setDays(tempDate, day);
        tempDate = DateHelper.setHours(tempDate, hour);
        tempDate = DateHelper.setMinutes(tempDate, minute);
        tempDate = DateHelper.setSeconds(tempDate, second);
        return tempDate;
    }


    /**
     * 判断日期是否在范围内，包含相等的日期
     */
    public static boolean isBetween(final Date date, final Date start, final Date end) {
        if (date == null || start == null || end == null || start.after(end)) {
            throw new IllegalArgumentException("some date parameters is null or dateBein after dateEnd");
        }
        return !date.before(start) && !date.after(end);
    }

    //////////// 往前往后滚动时间//////////////

    /**
     * 加一年
     */
    public static Date addYears(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
    }

    /**
     * 减一年
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date subYears(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addYears(date, -amount);
    }

    /**
     * 加一月
     */
    public static Date addMonths(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
    }

    /**
     * 减一月
     */
    public static Date subMonths(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, -amount);
    }

    /**
     * 加一周
     */
    public static Date addWeeks(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
    }

    /**
     * 减一周
     */
    public static Date subWeeks(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, -amount);
    }

    /**
     * 加一天
     */
    public static Date addDays(final Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
    }

    /**
     * 减一天
     */
    public static Date subDays(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, -amount);
    }

    /**
     * 加一小时
     */
    public static Date addHours(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
    }

    /**
     * 减一小时
     */
    public static Date subHours(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, -amount);
    }

    /**
     * 加一分钟
     */
    public static Date addMinutes(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, amount);
    }

    /**
     * 减一分钟
     */
    public static Date subMinutes(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, -amount);
    }

    /**
     * 终于到了，续一秒.
     */
    public static Date addSeconds(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, amount);
    }

    /**
     * 减一秒.
     */
    public static Date subSeconds(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, -amount);
    }

    //////////// 直接设置时间//////////////

    /**
     * 设置年份, 公元纪年.
     */
    public static Date setYears(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setYears(date, amount);
    }

    /**
     * 设置月份, 0-11.
     */
    public static Date setMonths(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setMonths(date, amount);
    }

    /**
     * 设置日期, 1-31.
     */
    public static Date setDays(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setDays(date, amount);
    }

    /**
     * 设置日期, 1-31.，自适应最大日期最小日期
     * 强化
     */
    public static Date setDaysPlus(final Date date, int amount) {
        int maxDate = getDay(subDays(addMonths(beginOfMonth(date), 1), 1));
        amount = MathHelper.min(maxDate, amount);
        return org.apache.commons.lang3.time.DateUtils.setDays(date, amount);
    }

    /**
     * 设置小时, 0-23.
     */
    public static Date setHours(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setHours(date, amount);
    }

    /**
     * 设置分钟, 0-59.
     */
    public static Date setMinutes(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setMinutes(date, amount);
    }

    /**
     * 设置秒, 0-59.
     */
    public static Date setSeconds(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setSeconds(date, amount);
    }

    /**
     * 设置毫秒.
     */
    public static Date setMilliseconds(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.setMilliseconds(date, amount);
    }

    ///// 获取日期的位置//////

    /**
     * 获得日期是一周的第几天. 已改为中国习惯，1 是Monday，而不是Sundays.
     */
    public static int getDayOfWeek(final Date date) {
        int result = get(date, Calendar.DAY_OF_WEEK);
        return result == 1 ? 7 : result - 1;
    }

    /**
     * 获得日期是一年的第几天，返回值从1开始
     */
    public static int getDayOfYear(final Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }


    /**
     * 获得日期是一个月的第几天，返回值从1开始
     */
    public static int getDayOfMonth(final Date date) {
        return get(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得日期是一月的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那个月里都算. 已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfMonth(final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得日期是一年的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那一年里都算.已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfYear(final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_YEAR);
    }


    /**
     * 根据日期获取月份
     *
     * @param date
     * @return
     */
    public static int getMonth(final Date date) {
        return get(date, Calendar.MONTH) + 1;
    }

    /**
     * 根据日期获取年份
     *
     * @param date
     * @return
     */
    public static int getYear(final Date date) {
        return get(date, Calendar.YEAR);
    }

    /**
     * 根据日期获取月份
     *
     * @param date
     * @return
     */
    public static int getDay(final Date date) {
        return get(date, Calendar.DATE);
    }


    /**
     * 根据日期获取小时 12小时制
     *
     * @param date
     * @return
     */
    public static int getDateFor12(final Date date) {
        return get(date, Calendar.HOUR);
    }

    /**
     * 根据日期获取小时  24小时制
     *
     * @param date
     * @return
     */
    public static int getDateFor24(final Date date) {
        return get(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 根据日期获取分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(final Date date) {
        return get(date, Calendar.MINUTE);
    }


    private static int get(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);

        return cal.get(field);
    }

    private static int getWithMondayFirst(final Date date, int field) {
        return get(date, field);
    }

    ///// 获得往前往后的日期//////

    /**
     * 2016-11-10 07:33:23, 则返回2016-1-1 00:00:00
     */
    public static Date beginOfYear(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.YEAR);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-12-31 23:59:59.999
     */
    public static Date endOfYear(final Date date) {
        return new Date(nextYear(date).getTime() - 1);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2017-1-1 00:00:00
     */
    public static Date nextYear(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.ceiling(date, Calendar.YEAR);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-1 00:00:00
     */
    public static Date beginOfMonth(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.MONTH);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-30 23:59:59.999
     */
    public static Date endOfMonth(final Date date) {
        return new Date(nextMonth(date).getTime() - 1);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-12-1 00:00:00
     */
    public static Date nextMonth(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.ceiling(date, Calendar.MONTH);
    }

    /**
     * 2017-1-20 07:33:23, 则返回2017-1-16 00:00:00
     */
    public static Date beginOfWeek(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(DateHelper.subDays(date, DateHelper.getDayOfWeek(date) - 1), Calendar.DATE);
    }

    /**
     * 2017-1-20 07:33:23, 则返回2017-1-22 23:59:59.999
     */
    public static Date endOfWeek(final Date date) {
        return new Date(nextWeek(date).getTime() - 1);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-22 00:00:00
     */
    public static Date nextWeek(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(DateHelper.addDays(date, 8 - DateHelper.getDayOfWeek(date)), Calendar.DATE);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-10 00:00:00
     */
    public static Date beginOfDate(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-23 23:59:59.999
     */
    public static Date endOfDate(final Date date) {
        return new Date(nextDate(date).getTime() - 1);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-11 00:00:00
     */
    public static Date nextDate(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.ceiling(date, Calendar.DATE);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 07:00:00
     */
    public static Date beginOfHour(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-23 07:59:59.999
     */
    public static Date endOfHour(final Date date) {
        return new Date(nextHour(date).getTime() - 1);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 08:00:00
     */
    public static Date nextHour(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.ceiling(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 07:33:00
     */
    public static Date beginOfMinute(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.MINUTE);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-23 07:33:59.999
     */
    public static Date endOfMinute(final Date date) {
        return new Date(nextMinute(date).getTime() - 1);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 07:34:00
     */
    public static Date nextMinute(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.ceiling(date, Calendar.MINUTE);
    }

    ////// 闰年及每月天数///////

    /**
     * 是否闰年.
     */
    public static boolean isLeapYear(final Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }

    /**
     * 是否闰年，移植Jodd Core的TimeUtil
     * <p>
     * 参数是公元计数, 如2016
     */
    public static boolean isLeapYear(int y) {
        boolean result = false;

        if (((y % 4) == 0) && // must be divisible by 4...
                ((y < 1582) || // and either before reform year...
                        ((y % 100) != 0) || // or not a century...
                        ((y % 400) == 0))) { // or a multiple of 400...
            result = true; // for leap year.
        }
        return result;
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(final Date date) {
        int year = get(date, Calendar.YEAR);
        int month = get(date, Calendar.MONTH) + 1;
        return getMonthLength(year, month);
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(int year, int month) {

        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("Invalid MONTH: " + month);
        }
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return MONTH_LENGTH[month];
    }

    /**
     * 将时间转化 为 yyyy-MM-dd HH:mm:ss 字符串
     *
     * @param tempDate
     * @return
     */
    public static String dateToString(Date tempDate) {
        return dateToString(tempDate, DATE_FORMAT_YMDHMS);
    }


    /**
     * 将时间转化 为 yyyy-MM-dd字符串
     *
     * @param tempDate
     * @return
     */
    public static String dateToStringYearMouthDay(Date tempDate) {
        return dateToString(tempDate, DATE_FORMAT_YMD);
    }

    /**
     * 将时间转化字符串
     *
     * @param tempDate 需要转化的时间
     * @param format   格式
     * @return
     */
    public static String dateToString(Date tempDate, String format) {

        if (ObjectUtils.isEmpty(tempDate)) {
            return null;
        }

        if (ObjectUtils.isEmpty(format)) {
            return tempDate.toString();
        }
        dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(tempDate);
    }


    /**
     * 将字符串转化时间
     *
     * @param dateStr 需要转化的时间
     * @param format  格式
     * @return
     */
    public static Date stringToDate(String dateStr, String format) {
        if (ObjectUtils.isEmpty(format)) {
            return null;
        }
        dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 将字符串转化时间
     *
     * @param dateStr 需要转化的时间 格式默认 yyyy-MM-dd HH:mm:ss
     * @return Date
     */
    public static Date stringToDate(String dateStr) {

        dateFormat = new SimpleDateFormat(DATE_FORMAT_YMDHMS);
        try {
            return dateFormat.parse(dateStr);
        } catch (Throwable e) {
            return null;
        }
    }


    /**
     * 比较时间返回最大的
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Date max(Date date1, Date date2) {
        return date1.getTime() > date2.getTime() ? date1 : date2;
    }

    /**
     * 判断时间是否在未来
     *
     * @param date
     * @return
     */
    public static boolean isFuture(final Date date) {
        return date.getTime() > new Date().getTime();
    }

    /**
     * 获取天（yyyyMMdd））
     *
     * @return
     */
    public static String getDateFor24() {
        Date nowDate = new Date();
        dateFormat = new SimpleDateFormat(DATE_FORMAT_YMD_NUM);
        try {
            return dateFormat.format(nowDate);
        } catch (Throwable e) {
            return null;
        }
    }


    /**
     * 获取时间(HHmmss)
     *
     * @return
     */
    public static String getTime() {
        Date nowDate = new Date();
        dateFormat = new SimpleDateFormat(DATE_FORMAT_HMS_NUM);
        try {
            return dateFormat.format(nowDate);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 获取时间(yyyyMMddHHmmss)
     *
     * @return
     */
    public static String getDateTime() {
        Date nowDate = new Date();
        dateFormat = new SimpleDateFormat(DATE_FORMAT_YMDHMS_NUM);
        try {
            return dateFormat.format(nowDate);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 获取距今过去的时间
     *
     * @param time
     * @return
     */
    public static String getPastTime(long time) {
        long nowTime = Calendar.getInstance().getTimeInMillis();
        long between = nowTime - time;
        if (between > DateHelper.MILLIS_PER_DAY * 365) {
            return between / (DateHelper.MILLIS_PER_DAY * 365) + "年前";
        } else if (between > DateHelper.MILLIS_PER_DAY * 30) {
            return between / (DateHelper.MILLIS_PER_DAY * 30) + "月前";
        } else if (between > DateHelper.MILLIS_PER_DAY * 7) {
            return between / (DateHelper.MILLIS_PER_DAY * 7) + "周前";
        } else if (between >= DateHelper.MILLIS_PER_DAY) {
            return between / DateHelper.MILLIS_PER_DAY + "天前";
        } else if (between >= DateHelper.MILLIS_PER_HOUR) {
            return between / DateHelper.MILLIS_PER_HOUR + "小时前";
        } else if (between >= DateHelper.MILLIS_PER_MINUTE) {
            return between / DateHelper.MILLIS_PER_MINUTE + "分钟前";
        } else {
            return "1分钟前";
        }
    }

    /**
     * 获取两个日期之间相差几个月
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String date1, String date2) {

        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(sdf.parse(date1));
            c2.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return result == 0 ? 1 : Math.abs(result);

    }

}
