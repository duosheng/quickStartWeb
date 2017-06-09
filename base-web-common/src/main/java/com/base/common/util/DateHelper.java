package com.base.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_HOUR_FORMAT = "yyyy-MM-dd HH:00:00";

    public static final String getSimpleDate() {
        return getSimpleDate(new Date());
    }

    /**
     * 格式化日期为字符串：yyyyMMdd
     *
     * @param date
     * @return
     */
    public static final String getSimpleDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * 格式化日期：格式由参数指定
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static final String getSimpleDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 格式化当前日期+时间字符串：yyyyMMdd HH:mm:ss
     *
     * @return
     */
    public static final String getSimpleDateTime() {
        return getSimpleDateTime(new Date());
    }

    /**
     * 格式化日期+时间字符串：yyyyMMdd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static final String getSimpleDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * 格式化当前日期+时间字符串：yyyyMMdd HH:00:00
     *
     * @return
     */
    public static final String getSimpleDateHour() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_HOUR_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * 昨天时间
     *
     * @return
     */
    public static final Date getYesterdayDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 昨天日期 yyyy-MM-dd 00:00:00
     *
     * @return
     */
    public static final Date getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**
     * 今天日期 yyyy-MM-dd 00:00:00
     *
     * @return
     */
    public static final Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * 前一天
     *
     * @param date
     * @return
     */
    public static final Date getPreviousDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
    /**
     * 下一天
     *
     * @param date
     * @return
     */
    public static final Date getNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 格式化字符串为日期
     *
     * @param formartDate
     * @param date_formater
     * @return
     */
    public static final Date parseToDate(String formartDate, String date_formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(date_formater);
        try {
            return sdf.parse(formartDate);
        } catch (ParseException e) {
            //
        }
        return null;
    }

    /**
     * 格式化字符串为日期：yyyyMMdd
     *
     * @param formartDate
     * @return
     */
    public static final Date parseToSimpleDate(String formartDate) {
        if (StringUtils.isBlank(formartDate)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            return sdf.parse(formartDate);
        } catch (ParseException e) {
            //
        }
        return null;
    }

    /**
     * 格式化日期+时间：yyyy-MM-dd HH:mm:ss
     *
     * @param formartDate
     * @return
     */
    public static final Date parseToSimpleDateTime(String formartDate) {
        if (StringUtils.isBlank(formartDate)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        try {
            return sdf.parse(formartDate);
        } catch (ParseException e) {
            //
        }
        return null;
    }


    /**
     * 多少天前
     *
     * @param date
     * @return
     */
    public static final Date getPreviousDate(Date date, int previousDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -previousDate);
        return calendar.getTime();
    }



    // 获得当前日期与本周日相差的天数
    private static int getMondayPlus(Date gmtCreate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gmtCreate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 因为按中国礼拜一作为第一天所以这里减1
        calendar.add(Calendar.DATE, -1);
        // 获得今天是一周的第几天，星期一是第一天，星期二是第二天......
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获得下周星期一的日期
     */
    public static Date getNextMonday(Date gmtCreate) {
        int mondayPlus = getMondayPlus(gmtCreate);
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        Date monday = currentDate.getTime();
        return monday;
    }

    /**
     * 获取下个月一号
     * @return
     */
    public static Date nextMonthFirstDate(Date gmtCreate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gmtCreate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当月,多少号
     * @return
     */
    public static Date monthFromDate(Date gmtCreate, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gmtCreate);
        calendar.set(Calendar.DAY_OF_MONTH, count);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取此时间   是当月的第几天
     * @return
     */
    public static int getCurrentMonthCount(Date gmtCreate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gmtCreate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;
    }


    public static void main(String[] args) {

        System.out.println(getSimpleDateTime(getNextMonday(parseToSimpleDate("2017-01-22 00:00:00"))));
        System.out.println(getSimpleDateTime(nextMonthFirstDate(parseToSimpleDate("2017-01-22 00:00:00"))));
        System.out.println(getSimpleDateTime(monthFromDate(parseToSimpleDate("2017-01-22 00:00:00"),16)));
        System.out.println(getCurrentMonthCount(new Date()));
    }






}
