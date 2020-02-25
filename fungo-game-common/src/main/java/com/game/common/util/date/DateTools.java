package com.game.common.util.date;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

public class DateTools {
    public static Date getAfterMinutes(int count) {
        long curren = System.currentTimeMillis();
        curren += count * 60 * 1000;
        Date da = new Date(curren);
        return da;
    }

    public static String fmtDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        return time;
    }

    public static String fmtSimpleDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(date);
        return time;
    }


    public static String fmtSimpleDateToStringZhCn(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String time = formatter.format(date);
        return time;
    }



    public static String fmtSimpleDate(Date date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//		 SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String time = formatter.format(date);
//		 Date t = formatter2.parse(time);
        return time;
    }


    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date str2Date(String str, String parrten) {
        if (StringUtils.isBlank(parrten)) {
            parrten = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(parrten);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取当前日期
     * @param pattern 日期分割符，如-
     * @return
     */
    public static String getCurrentDate(String pattern) {
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        StringBuffer dateSb = new StringBuffer();
        dateSb.append(year);
        //年后面添加日期格式符
        if (StringUtils.isNotBlank(pattern)) {
            dateSb.append(pattern);
        }
        if (month < 10) {
            dateSb.append("0" + month);
        } else {
            dateSb.append(month);
        }
        //月后面添加日期格式符
        if (StringUtils.isNotBlank(pattern)) {
            dateSb.append(pattern);
        }

        if (day < 10) {
            dateSb.append("0" + day);
        } else {
            dateSb.append(day);
        }
        return dateSb.toString();
    }


    /**
     * 时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
            //System.out.println("相隔的天数="+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


    /**
     * 时间相减得到天数
     * @param dateStr
     * @return
     */
    public static long getHour(String dateStr) {
        long hour = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(dateStr);
            hour = beginDate.getHours();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour;
    }


    /**
     * 获取本周的开始时间
     * @return
     */
    public static Date getBeginDayOfWeek(Date date) {

        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }


    /**
     * 获取本周的结束时间
     * @return
     */
    public static Date getEndDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek(date));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }


    /**
     * 获取某个日期的开始时间
     * @param d
     * @return
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }


    /**
     * 获取某个日期的结束时间
     * @param d
     * @return
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());

    }


    /**
     * 判断选择的日期是否是本周
     * @param date
     * @return
     */
    public static boolean isThisWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(date);
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }


    /**
     * 根据年-月 获取对应的月份天数
     *
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * @description 时间转换为字符串
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        if (date !=null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String dateTimeToString(Date date){
        if(date != null){
            return  "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 判断当前时间是否在时间段内
     * @param beginDate 开始时间
     * @param endDateStr 结束时间
     * @param newDate 当前时间
     * @return
     */
    public static boolean betweenDate(Date beginDate, Date endDateStr,Date newDate) {

        try {
            long startTime = (newDate.getTime() - beginDate.getTime());
            long endTime = (endDateStr.getTime() - newDate.getTime());
            if(startTime > 0 && endTime > 0){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param date
     * @return
     */
    public static String channelDateToString(Date date) {

        try {
            Calendar calendar = Calendar.getInstance();
            int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(date);
            java.util.Date updateDate = calendar.getTime();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
            String dateStr=sdf.format(updateDate);
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 功能描述: 将当前时间加 指定得天数返回时间戳
     * @param: []
     * @return: java.lang.String
     * @date: 2019/11/21 10:27
     */
    public static long getAddDays(int days){
        try {
            LocalDate afterThreeDays = LocalDate.now().plusDays(days);
            long day1 = afterThreeDays.atStartOfDay( ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
            return day1;
        }catch (Exception e){
            e.printStackTrace();//( "将当前时间加指定得天数返回时间戳出现异常",e);
        }
        return System.currentTimeMillis()+100000;

    }

    /**
     * 功能描述: 将当前时间加 指定得天数返回时间戳
     * @param: []
     * @return: java.lang.String
     * @date: 2019/11/21 10:27
     */
    /**
     * 功能描述: 将当前时间加 指定得天数返回时间戳
     * @param: []
     * @return: java.lang.String
     * @date: 2019/11/21 10:27
     */
    public static long getAddMinute(int minute){
        try {
            LocalDateTime afterThreeDays = LocalDateTime.now();
//            System.out.println("MINUTES"+afterThreeDays);
//            afterThreeDays = afterThreeDays.minusMinutes(minute);
//            System.out.println("MINUTES----"+afterThreeDays);
//            afterThreeDays = afterThreeDays.plusMinutes(minute);
//            System.out.println("MINUTES----====="+afterThreeDays);
//            long day1 = afterThreeDays.atStartOfDay( ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
//            Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(); // 毫秒数
            afterThreeDays= afterThreeDays.withSecond(0).withNano(0).withMinute(0);
            System.out.println("MINUTES----=====10"+afterThreeDays);
//            val newNow = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC)
            Long milliSecond = afterThreeDays.toInstant(ZoneOffset.of("+8")).toEpochMilli(); // 毫秒数
            return milliSecond;
        }catch (Exception e){
            // LOGGER.error( "将当前时间加指定得天数返回时间戳出现异常",e);
        }
        return System.currentTimeMillis()+60000;
    }
    //--------
}
