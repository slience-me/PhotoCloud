package xyz.slienceme.tuyun.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {


    public static String getBeforeFirstMonthdate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至23
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至59
        calendar.set(Calendar.MINUTE, 0);
        //将秒至59
        calendar.set(Calendar.SECOND, 0);
        String format1 = format.format(calendar.getTime());
        return format1;
    }

    /**
     * 获取上个月的最后一天23点59分59秒的时间
     */
    public static String getBeforeLastMonthdate() throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        calendar.set(Calendar.MINUTE, 59);
        //将秒至59
        calendar.set(Calendar.SECOND, 59);
        String format = sf.format(calendar.getTime());
        return format;
    }

    /**
     * 获取当前时间指定日期格式
     *
     * @return String
     */
    public static String getDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(new Date());
        return time;
    }


    /**
     * @param time1 参数1
     * @param time2 参数2
     * @return 参数1 小于 参数2 返回true
     * @throws Exception
     */
    public static boolean timesize(String time1, String time2) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        return date1.before(date2);
    }


    /**
     * @return String
     */
    public static String getNewData(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }

    /**
     * @return String
     */
    public static String getNewData(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(date);
        return time;
    }

    /**
     * string转data
     *
     * @return String
     */
    public static Date getDateByString(String datetime) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(datetime);
        return date;
    }

    /**
     * string转data
     *
     * @return String
     */
    public static Date getDateByString(String dayformt, String datetime) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(dayformt);
        Date date = formatter.parse(datetime);
        return date;
    }

    /**
     * 时间向后追加或向前
     *
     * @return String
     * datetype 增加 1 天, 2 月, 3 年, 4 周
     * dataNum 增加的数量 整数往后推,负数往前移动
     */
    public static String getDateByTian(int datetype, int dataNum, Date date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        switch (datetype) {
            case 1:
                calendar.add(calendar.DATE, dataNum);
                break;
            case 2:
                calendar.add(calendar.MONTH, dataNum);
                break;
            case 3:
                calendar.add(calendar.YEAR, dataNum);
                break;
            case 4:
                calendar.add(calendar.WEEK_OF_MONTH, dataNum);
                break;
        }
        return formatter.format(calendar.getTime());
    }

    /**
     * 获取当前时间默认日期格式
     *
     * @return String
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }

    /**
     * 获取今年
     *
     * @return String
     */
    public static String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String time = sdf.format(new Date());
        return time;
    }

    /**
     * 获取本周 几的 日期 星期一至星期日 分别为 1-7
     *
     * @return String
     */
    public static String benZhouJi(int severalWeeks) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        cld.setTimeInMillis(System.currentTimeMillis());//当前时间
//        cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一
        cld.set(Calendar.DAY_OF_WEEK, severalWeeks + 1);//指定周几
        return df.format(cld.getTime());
    }

    /**
     * 获取本月第几天
     *
     * @return String
     */
    public static String benYue(int dayNum) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, dayNum);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取今天周几
     *
     * @return String
     */
    public static int getZhouJi() {
        int week = 0;
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            week = 0;
        } else if (weekday == 2) {
            week = 1;
        } else if (weekday == 3) {
            week = 2;
        } else if (weekday == 4) {
            week = 3;
        } else if (weekday == 5) {
            week = 4;
        } else if (weekday == 6) {
            week = 5;
        } else if (weekday == 7) {
            week = 6;
        }
        return week;
    }

    /**
     * 或取时间格式
     * 2012年9月11日13时22分
     *
     * @return String
     */
    public static String getDateYearMonth() {
        Calendar now = Calendar.getInstance();
        //年
        int year = now.get(Calendar.YEAR);
        //月
        int month = (now.get(Calendar.MONTH) + 1);
        //日
        int DAY_OF_MONTH = now.get(Calendar.DAY_OF_MONTH);
        //时
        int HOUR_OF_DAY = now.get(Calendar.HOUR_OF_DAY);
        //分
        int MINUTE = now.get(Calendar.MINUTE);
        return year + "年" + month + "月" + DAY_OF_MONTH + "日" + HOUR_OF_DAY + "时" + MINUTE + "分";
    }

    public static String secondToTimeByTian(long second) {

        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days) {
            return days + "天" + hours + "小时" + minutes + "分钟";
        } else if (0 < hours) {
            return hours + "小时" + minutes + "分钟";
        } else if (0 < minutes) {
            return minutes + "分钟";
        } else {
            return "1分钟";
        }
    }

    /**
     * 返回日时分秒
     *
     * @param second 单位为秒
     * @return
     */
    public static String secondToTime(long second) {

        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days) {
            return days + " " + hours + ":" + minutes + ":" + second;
        } else if (0 < hours) {
            return hours + ":" + minutes + ":" + second;
        } else if (0 < minutes) {
            return minutes + ":" + second;
        } else {
            return second + "";
        }
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @param date
     * @return
     */
    public static Long getSecondTimestampTwo(Date date) {
        if (null == date) {
            return 0L;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Long.valueOf(timestamp);
    }

    /**
     * @param dateformat
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param interval   间隔时间
     * @param type       间隔时间类型 1:小时 yyyy-MM-dd hh 2:分钟 yyyy-MM-dd hh:mm 3:天 yyyy-MM-dd 4:月 yyyy-MM 5:年 yyyy
     * @return
     */
    public static List<String> getDateTimeListByStartAndEnd(String dateformat, String startTime, String endTime, Integer interval, Integer type) throws Exception {
        List<String> arr = new ArrayList<>();
        Date beginDate = new Date();
        Date endDate = new Date();
        long msCount = 0;
        if (type == 1) {//设置小时间隔
            beginDate = getDateByString("yyyy-MM-dd hh:mm", startTime + " 00:00");
            endDate = getDateByString("yyyy-MM-dd hh:mm", endTime + " 23:59");
            msCount = interval * 60 * 60 * 1000;  //1小时 2小时 3小时
        } else if (type == 2) {//设置分钟间隔
            beginDate = getDateByString("yyyy-MM-dd hh:mm", startTime + ":00:00");
            endDate = getDateByString("yyyy-MM-dd hh:mm", endTime + ":23:59");
            msCount = interval * 60 * 1000;  //30分钟 10分钟 5分钟 15分钟
        } else if (type == 3) {//设置天间隔
            beginDate = getDateByString("yyyy-MM-dd hh:mm", startTime + " 00:00");
            endDate = getDateByString("yyyy-MM-dd hh:mm", endTime + " 00:00");
            msCount = interval * 24 * 60 * 60 * 1000;  //1天 2天 3天
        } else if (type == 4) {//获取月间隔
            String[] startDate = startTime.split("-");
            String[] endDate1 = endTime.split("-");
            Integer intervalMonth = (Integer.parseInt(endDate1[0]) * 12 + Integer.parseInt(endDate1[1])) - (Integer.parseInt(startDate[0]) * 12 + Integer.parseInt(startDate[1]));
            arr = getMonthArr(endTime, intervalMonth + 1);
            return arr;
        } else if (type == 5) {//获取年间隔
            for (int i = Integer.parseInt(startTime); i <= Integer.parseInt(endTime); i++) {
                arr.add(String.valueOf(i));
            }
            return arr;
        }
        long beginMs = beginDate.getTime();
        long endMs = endDate.getTime();
        for (long i = beginMs; i <= endMs; i += msCount) {
            if (type == 1) {
                arr.add(dataformat(i, type) + ":00");
            } else if (type == 2) {
                arr.add(dataformat(i, type));
            } else if (type == 3) {
                arr.add(dataformat(i, type));
            }
        }
        return arr;
    }

    private static String dataformat(long datetime, Integer type) {
        if (type == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            Date date = new Date(datetime);
            return sdf.format(date);
        }
        if (type == 2) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date(datetime);
            return sdf.format(date);
        }
        if (type == 3) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(datetime);
            return sdf.format(date);
        }
        return null;
    }

    //得到最近n个月份的数组
    public static List<String> getMonthArr(String sDate, Integer n) {
        List<String> arr = new ArrayList<>();
        sDate = sDate.substring(0, 7);
        arr.add(sDate);
        while (n > 1) {
            arr.add(0, getBeforMonth(sDate));
            sDate = getBeforMonth(sDate);
            n--;
        }
        return arr;
    }

    public static String getBeforMonth(String date) {
        String lastMonth = "";
        String year = date.split("-")[0], month = date.split("-")[1];
        if (Objects.equals(month, 1)) {
            lastMonth = (Integer.parseInt(year) - 1) + "-" + 12;
        } else {
            lastMonth = year + "-" + (Integer.parseInt(month) - 1);
        }
        return lastMonth;
    }

    /**
     * 获取两个时间之间相差天数
     *
     * @param starttime
     * @return
     */
    public static int dataTimeBeApart(String starttime, String endtime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(starttime));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endtime));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return (int) between_days;
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dateDiff(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {//不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {//闰年
                    timeDistance += 366;
                } else {//不是闰年
                    timeDistance += 365;
                }
            }

            return (timeDistance + (day2 - day1) + 1);
        } else {//同一年
//            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return ((day2 - day1) + 1);
        }
    }

    /**
     * date2比date1多的周数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int calcWeekOffset(Date startTime, Date endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) dayOfWeek = 7;

        int dayOffset = dateDiff(startTime, endTime);

        int weekOffset = dayOffset / 7;
        int a;
        if (dayOffset > 0) {
            a = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
        } else {
            a = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
        }
        weekOffset = weekOffset + a;
        return weekOffset;
    }

    /**
     * 获取两个日期相差的月数
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }


    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dateDiff(String date1, String date2, String formatStr) throws ParseException {
        return dateDiff(parseDate(date1, formatStr), parseDate(date2, formatStr));
    }

    /**
     * 日期格式化
     *
     * @param date      日期
     * @param formatStr 格式化字符串
     * @return
     */
    public static String dateFormat(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     *
     * @param date      日期字符串
     * @param formatStr 格式化字符串
     * @return
     */
    public static Date parseDate(String date, String formatStr) throws ParseException {
        if (formatStr == null) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.parse(date);
    }

    /**
     * 字符串格式化字符串
     *
     * @param date      日期字符串
     * @param formatStr 格式化字符串
     * @return
     */
    public static String parseStrTime(String date, String formatStr) throws ParseException {
        if (formatStr == null) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        return dateFormat(parseDate(date, formatStr), formatStr);
    }

    /**
     * 获取本月15号 ，或者下个月15号
     *
     * @param dateTime
     */
    public static String day15(String dateTime) throws Exception {
        String dd = DateUtil.dateFormat(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", dateTime), "dd");
        String mouth = DateUtil.dateFormat(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", dateTime), "yyyy-MM");
        if (Integer.valueOf(dd) <= 15) {//获取本月15号
            return mouth + "-15 00:00:00";
        } else {//获取下个月15号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 15);
            calendar.add(Calendar.MONTH, 1);
            return sdf.format(calendar.getTime());
        }
    }

//    /**
//     * 计算两个日期之间的 刨除工作日的天数
//     *
//     * @param startDateString
//     * @param endDateString
//     * @return
//     */
//    public static int getDutyDays(String startDateString, String endDateString) throws Exception {
//        Date startDate = getDateByString(startDateString);
//        Date endDate = getDateByString(endDateString);
//        int result = 0;
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
//        while (startDate.compareTo(endDate) <= 0) {
//            if (startDate.getDay() != 6 && startDate.getDay() != 0)
//                result++;
//            startDate.setDate(startDate.getDate() + 1);
//        }
//
//        return result;
//    }

    //计算指定日期的 月的天数
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    //指定日期的上个月的天数
    public static int getDaysOfZDMonth(String day) throws Exception {
        int daysOfMonth = getDaysOfMonth(getDateByString("yyyy-MM-dd", getDateByTian(2, 1, getDateByString("yyyy-MM-dd", day))));
        return daysOfMonth;
    }

    //计算两个时间段的交集天数
    public static int computeIntersectionDays(Date start1, Date end1, Date start2, Date end2) {

        long b1 = start1.getTime();
        long e1 = end1.getTime();
        long b2 = start2.getTime();
        long e2 = end2.getTime();
        assert (b1 < e1 && b2 < e2);
        int intersectionDays;
        if (b1 <= b2 && e1 >= e2) {//（b1---【b2-----e2】--e1）
            System.out.println("1包含2");
            intersectionDays = getDayDifference(end2, start2);
        } else if (b1 >= b2 && e1 <= e2) {//【b2---（b1-----e1）--e2】
            System.out.println("2包含1");
            intersectionDays = getDayDifference(end1, start1);
        } else if (b1 >= b2 && b1 <= e2) {//【b2---(b1---e2】----e1)
            System.out.println("相交");
            intersectionDays = getDayDifference(end2, start1);
        } else if (b1 <= b2 && e1 >= b2) {//（b1---【b2---e1）----e2】
            System.out.println("相交");
            intersectionDays = getDayDifference(end1, start2);
        } else {
            intersectionDays = 0;
        }
        System.out.println("重合天数为[" + intersectionDays + "]天。");
        return intersectionDays;
    }

    /**
     * 计算两个日期的相差天数(d1-d2)
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getDayDifference(Date d1, Date d2) {
        long num = (d1.getTime() - d2.getTime()) / 1000;
        long days = num / (3600 * 24) + 1;
        return (int) days;
    }

    public static LocalDateTime StringToLocalDateTime(String stringTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /*System.out.println("\nString类型的时间转成LocalDateTime："+ldt);*/
        return LocalDateTime.parse(stringTime,df);
    }

    public static String LocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(localDateTime);
    }

    public static void main(String[] args) throws Exception {

        /*Date date1 = DateUtil.getDateByString("2020-02-01 00:00:00");
        Date date2 = DateUtil.getDateByString("2020-02-29 00:00:00");
        Date date3 = DateUtil.getDateByString("2020-01-04 00:00:00");
        Date date4 = DateUtil.getDateByString("2020-02-19 00:00:00");

        System.out.println(computeIntersectionDays(date1, date2, date3, date4));*/
        /*LocalDateTime localDateTime = StringToLocalDateTime("2022-01-16 09:44:25");
        System.out.println(localDateTime);*/
        String date = getDate("yyyyMMddHHmmss");
        System.out.println(date);

    }


}