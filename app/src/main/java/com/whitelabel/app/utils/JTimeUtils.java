package com.whitelabel.app.utils;

import android.content.Context;
import android.text.TextUtils;

import com.whitelabel.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by imaginato on 2015/6/10.
 */
public class JTimeUtils {
    private final static String TAG = "JTimeUtils";

    public final static String TIME_FORMAT_yyyy_MM_dd_HHmmss = "yyyy/MM/dd HH:mm:ss";
    public final static String TIME_FORMAT_HHmm = "HH:mm";
    public final static String TIME_FORMAT_hhmm = "hh:mm";
    public final static String TIME_FORMAT_hhmmss = "hh:mm:ss";
    public final static String TIME_FORMAT_yyyyMMdd_hhmmss = "yyyy-MM-dd hh:mm:ss";
    public final static String TIME_FORMAT_yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    public final static String TIME_FORMAT_hhmmss_ddMMyyyy = "HH:mm:ss dd MM yyyy";

    public final static String TIME_FORMAT_yyyyMMdd = "yyyy-MM-dd";
    public final static String TIME_FORMAT_MM_dd_yyyy = "MM/dd/yyyy";

    //  miss is sec
    public static String getDay(long miss) {
        long dd = miss / 3600 / 24;
        String ddStr = dd > 9 ? dd + "" : "0" + dd;
        return ddStr;
    }

    public static String getHour(long miss) {
        long hh = (miss % (3600 * 24)) / (3600);
        String hhStr = hh > 9 ? hh + "" : "0" + hh;
        return hhStr;
    }

    public static String getMin(long miss) {
        long mm = (miss % (3600)) / 60;
        String mmStr = mm > 9 ? mm + "" : "0" + mm;
        return mmStr;
    }

    public static String getSec(long miss) {
        long ss = (miss % (60)) / 1;
        String ssStr = ss > 9 ? ss + "" : "0" + ss;
        return ssStr;
    }

    public static long getCurrentTimeLong() {
        return System.currentTimeMillis();
    }

    public static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getCurrentTimeLong());
        return calendar;
    }

    public static Date getCurrentDate() {
        Calendar calendar = getCurrentCalendar();
        return calendar.getTime();
    }

    /**
     * @return ERROR==>null
     */
    public static String getCurrentTimeString(String format) {
        String result = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            result = formatter.format(getCurrentDate());
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getCurrentTimeString", ex);
        }

        return result;
    }

    /**
     * @return ERROR==>Current Date
     */
    public static Date getDateValueOfTheString(String str, String format) {
        Date date = getCurrentDate();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDateValueOfTheString", ex);
        }
        return date;
    }

    /**
     * @return ERROR==>Current Calendar
     */
    public static Calendar getCalendarValueOfTheString(String str, String format) {
        Calendar calendar = getCurrentCalendar();
        try {
            calendar.setTime(getDateValueOfTheString(str, format));
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDateValueOfTheString", ex);
        }
        return calendar;
    }

    /**
     * @return ERROR==>null
     */
    public static String getStringFromDateValue(Date date, String format) {
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDateStringFromDateValue", ex);
        }

        return result;
    }

    /**
     * @return ERROR==>null
     */
    public static String getStringFromCalendarValue(Calendar calendar, String format) {
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(calendar.getTime());
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDateStringFromCalendarValue", ex);
        }

        return result;
    }

    /**
     * @return ERROR==>null
     */
    public static String getStringFromStringValue(String value, String oldFormat, String newFormat) {
        String result = null;
        try {
            final Date olddate = getDateValueOfTheString(value, oldFormat);
            result = getStringFromDateValue(olddate, newFormat);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getStringFromStringValue", ex);
        }
        return result;
    }

    /**
     * "43200000" => "12:00"
     *
     * @return ERROR=>00:00
     */
    public static String getTimeStringOfTheDayHHmm(long value) {
        String result = "00:00";

        int hour = 0;
        int minute = 0;
        try {
            value = value / 1000;
            value = value / 60;
            hour = (int) (value / 60);
            hour = hour % 24;
            minute = (int) (value % 60);
            minute = minute % 60;
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getTimeStringOfTheDayHHmm", ex);
            return result;
        }

        result = (hour < 10 ? ("0" + hour) : ("" + hour));
        result = result + ":" + (minute < 10 ? ("0" + minute) : ("" + minute));
        return result;
    }

    /**
     * "43200000" => "12:00"
     *
     * @return ERROR=>00:00
     */
    public static String getTimeStringOfTheDayHHmm(String value) {
        String result = "00:00";
        if (JDataUtils.isEmpty(value)) {
            return result;
        }

        try {
            result = getTimeStringOfTheDayHHmm(JDataUtils.string2long(value));
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getTimeStringOfTheDayHHmm", ex);
        }

        return result;
    }

    /**
     * "12:00" => "43200000"
     *
     * @return ERROR=>0
     */
    public static long getTimeLongOfTheDay(String value) {
        long result = 0l;
        if (JDataUtils.isEmpty(value)) {
            return result;
        }

        try {
            final String valuetimestring = "2015-01-20 " + value + ":00";
            final String basetimestring = "2015-01-20 00:00:00";
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final Date valuedate = sdf.parse(valuetimestring);
            final Date basedate = sdf.parse(basetimestring);
            result = valuedate.getTime() - basedate.getTime();
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getTimeLongOfTheDay", ex);
        }

        return result;
    }

    /**
     * "12:00" => "43200000"
     * ERROR=>0
     */
    public static String getTimeStringOfTheDay(String value) {
        return "" + getTimeLongOfTheDay(value);
    }

    /**
     * "43200000"
     *
     * @return ERROR=>0
     */
    public static long getTimeLongOfTheDay(Date date) {
        if (date == null) {
            return 0l;
        }

        long result = 0l;
        try {
            String basetimestring = getStringFromDateValue(date, TIME_FORMAT_yyyyMMdd);
            basetimestring = basetimestring + " 00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date basedate = sdf.parse(basetimestring);
            result = date.getTime() - basedate.getTime();
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getTimeLongOfTheDay", ex);
        }
        return result;
    }

    /**
     * "43200000"
     *
     * @return ERROR=>0
     */
    public static String getTimeStringOfTheDay(Date date) {
        return "" + getTimeLongOfTheDay(date);
    }

    /**
     * "43200000"
     *
     * @return ERROR=>0
     */
    public static long getTimeLongOfTheDay(Calendar calendar) {
        if (calendar == null) {
            return 0l;
        }

        long result = 0l;
        try {
            getTimeLongOfTheDay(calendar.getTime());
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getTimeLongOfTheDay", ex);
        }
        return result;
    }

    /**
     * "43200000"
     *
     * @return ERROR=>0
     */
    public static String getTimeStringOfTheDay(Calendar calendar) {
        return "" + getTimeLongOfTheDay(calendar);
    }

    /**
     * "05/30/2015" -> "2015-05-30"
     *
     * @return ERROR==>Current Date String Value
     */
    public static String getDateStringFromMM_dd_yyyy2yyyyMMdd(String value) {
        Date date = getDateValueOfTheString(value, TIME_FORMAT_MM_dd_yyyy);
        return getStringFromDateValue(date, TIME_FORMAT_yyyyMMdd);
    }

    /**
     * milliseconds==>09/14/2014(Based on 1970/01/01/ 00:00:00)
     *
     * @return ERROR=>null
     */
    public static String getTimeOfLong(String longStr) {
        String currentStr = null;
        try {
            Date originalDate = null;
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String originalStr = "01/01/1970 00:00:00";

            originalDate = formatter.parse(originalStr);
            calendar.setTime(originalDate);
            long timeLong = Long.parseLong(longStr) / (1000 * 60);
            calendar.add(Calendar.MINUTE, (int) timeLong);
            currentStr = formatter.format(calendar.getTime());
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getTimeOfLong", ex);
        }
        return currentStr;
    }

    /**
     * 2016-12-05 00:00:00(Based on 1970/01/01/ 00:00:00)==>milliseconds
     *
     * @param dateStr 2016-12-05 00:00:00
     * @return error=>-1
     */
    public static long getLongOfTime(String dateStr) {
        long result=-1;
        try {
//            String monthNumberDate=getMonthNumber(dateStr);
            if(!TextUtils.isEmpty(dateStr)){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_yyyyMMdd_HHmmss);
                Date date = simpleDateFormat.parse(dateStr);
                result=date.getTime();
            }
        } catch (ParseException e) {
            JLogUtils.e(TAG, "getLongOfTime", e);
        }
        return result;
    }

    //"00:00:00 05 Dec 2016"
    private static  String getMonthNumber(String originalDate) {
        //英文数字月份映射
        String month[] = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        HashMap<String, String> monthMap = new HashMap<String, String>();
        for (int i = 0; i < 12; i++) {
            if (i < 9) {
                monthMap.put("0" + (i + 1), month[i]);
            } else {
                monthMap.put("" + (i + 1), month[i]);
            }
        }
        if (!TextUtils.isEmpty(originalDate)) {
            String[] splitArr = originalDate.split(" ");
            JLogUtils.d("jay", "date=---->" + splitArr.length);
            if (splitArr.length == 4) {
                String monthNumber = "";
                String value = splitArr[2];
                Set<Map.Entry<String, String>> entriesSet = monthMap.entrySet();
                Iterator<Map.Entry<String, String>> iterator = entriesSet.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    if (entry.getValue().equals(value)) {
                        monthNumber = entry.getKey();
                    }
                }
                return splitArr[0] + " " + splitArr[1] + " " + monthNumber + " " + splitArr[3];
            }

        }
        return null;

    }

    /**
     * @return Error, return -1
     * Sun->0 Sat->6
     */
    public static int getDayIndexOfTheWeek(String dateString, String format) {
        if (JDataUtils.isEmpty(dateString)) {
            return -1;
        }

        int result = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.setTime(sdf.parse(dateString));
            result = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDayIndexOfTheWeek", ex);
        }
        return result;
    }

    /**
     * @return Error, return -1
     * Sun->0 Sat->6
     */
    public static int getDayIndexOfTheWeek(Date date, String format) {
        int result = -1;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            result = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDayIndexOfTheWeek", ex);
        }
        return result;
    }

    /**
     * @return Error, return -1
     * Sun->0 Sat->6
     */
    public static int getDayIndexOfTheWeek(Calendar calendar, String format) {
        int result = -1;
        try {
            result = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDayIndexOfTheWeek", ex);
        }
        return result;
    }

    /**
     * @return ERROR=>-1
     * JANUARY->0 December->11
     */
    public static int getMonthIndexOfTheYear(String dateString, String format) {
        int result = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(dateString));
            result = calendar.get(Calendar.MONTH);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthIndexOfTheYear", ex);
        }
        return result;
    }

    /**
     * @return ERROR=>-1
     * JANUARY->0 December->11
     */
    public static int getMonthIndexOfTheYear(Date date, String format) {
        int result = -1;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            result = calendar.get(Calendar.MONTH);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthIndexOfTheYear", ex);
        }
        return result;
    }

    /**
     * @return ERROR=>-1
     * JANUARY->0 December->11
     */
    public static int getMonthIndexOfTheYear(Calendar calendar, String format) {
        int result = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = calendar.get(Calendar.MONTH);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthIndexOfTheYear", ex);
        }
        return result;
    }

    /**
     * @return ERROR->null
     * 2015/06/10=>Monday
     */
    public static String getWeekOfTheDay(Context context, String value, String format) {
        String result = null;

        try {
            String[] weekarray = context.getResources().getStringArray(R.array.Week);
            if (weekarray == null || weekarray.length <= 0) {
                return null;
            }

            final int arraylength = weekarray.length;
            int dayindex = getDayIndexOfTheWeek(value, format);
            if (arraylength <= dayindex) {
                return null;
            }

            result = weekarray[dayindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getWeekOfTheDay", ex);
        }

        return result;
    }

    /**
     * @return ERROR->null
     * 2015/06/10=>Monday
     */
    public static String getWeekOfTheDay(Context context, Date date, String format) {
        String result = null;

        try {
            String[] weekarray = context.getResources().getStringArray(R.array.Week);
            if (weekarray == null || weekarray.length <= 0) {
                return null;
            }

            final int arraylength = weekarray.length;
            final int dayindex = getDayIndexOfTheWeek(date, format);
            if (arraylength <= dayindex) {
                return null;
            }

            result = weekarray[dayindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getWeekOfTheDay", ex);
        }

        return result;
    }

    /**
     * @return ERROR->null
     * 2015/06/10=>Monday
     */
    public static String getWeekOfTheDay(Context context, Calendar calendar, String format) {
        String result = null;

        try {
            String[] weekarray = context.getResources().getStringArray(R.array.Week);
            if (weekarray == null || weekarray.length <= 0) {
                return null;
            }

            final int arraylength = weekarray.length;
            final int dayindex = getDayIndexOfTheWeek(calendar, format);
            if (arraylength <= dayindex) {
                return null;
            }

            result = weekarray[dayindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getWeekOfTheDay", ex);
        }

        return result;
    }

    /**
     * @return ERROR->null
     * 2015/06/10=>Mon
     */
    public static String getWeekAbbrOfTheDay(Context context, String value, String format) {
        String result = null;

        try {
            String[] weekarray = context.getResources().getStringArray(R.array.WeekAbbr);
            if (weekarray == null || weekarray.length <= 0) {
                return null;
            }

            final int arraylength = weekarray.length;
            int dayindex = getDayIndexOfTheWeek(value, format);
            if (arraylength <= dayindex) {
                return null;
            }

            result = weekarray[dayindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getWeekAbbrOfTheDay", ex);
        }

        return result;
    }

    /**
     * @return ERROR->null
     * 2015/06/10=>Mon
     */
    public static String getWeekAbbrOfTheDay(Context context, Date date, String format) {
        String result = null;

        try {
            String[] weekarray = context.getResources().getStringArray(R.array.WeekAbbr);
            if (weekarray == null || weekarray.length <= 0) {
                return null;
            }

            final int arraylength = weekarray.length;
            final int dayindex = getDayIndexOfTheWeek(date, format);
            if (arraylength <= dayindex) {
                return null;
            }

            result = weekarray[dayindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getWeekAbbrOfTheDay", ex);
        }

        return result;
    }

    /**
     * @return ERROR->null
     * 2015/06/10=>Mon
     */
    public static String getWeekAbbrOfTheDay(Context context, Calendar calendar, String format) {
        String result = null;

        try {
            String[] weekarray = context.getResources().getStringArray(R.array.WeekAbbr);
            if (weekarray == null || weekarray.length <= 0) {
                return null;
            }

            final int arraylength = weekarray.length;
            final int dayindex = getDayIndexOfTheWeek(calendar, format);
            if (arraylength <= dayindex) {
                return null;
            }

            result = weekarray[dayindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getWeekAbbrOfTheDay", ex);
        }

        return result;
    }

    /**
     * @return ERROR->null
     * 2015/03/10=>March
     */
    public static String getMonthOfTheDay(Context context, String value, String format) {
        String result = null;

        try {
            String[] montharray = context.getResources().getStringArray(R.array.Month);
            if (montharray == null || montharray.length <= 0) {
                return result;
            }

            final int arraylength = montharray.length;
            final int monthindex = getMonthIndexOfTheYear(value, format);
            if (monthindex <= -1 || monthindex >= arraylength) {
                return result;
            }

            result = montharray[monthindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthOfTheDay", ex);
        }
        return result;
    }

    /**
     * @return ERROR->null
     * 2015/03/10=>March
     */
    public static String getMonthOfTheDay(Context context, Date date, String format) {
        String result = null;

        try {
            String[] montharray = context.getResources().getStringArray(R.array.Month);
            if (montharray == null || montharray.length <= 0) {
                return result;
            }

            final int arraylength = montharray.length;
            final int monthindex = getMonthIndexOfTheYear(date, format);
            if (monthindex <= -1 || monthindex >= arraylength) {
                return result;
            }

            result = montharray[monthindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthOfTheDay", ex);
        }
        return result;
    }

    /**
     * @return ERROR->null
     * 2015/03/10=>March
     */
    public static String getMonthOfTheDay(Context context, Calendar calendar, String format) {
        String result = null;

        try {
            String[] montharray = context.getResources().getStringArray(R.array.Month);
            if (montharray == null || montharray.length <= 0) {
                return result;
            }

            final int arraylength = montharray.length;
            final int monthindex = getMonthIndexOfTheYear(calendar, format);
            if (monthindex <= -1 || monthindex >= arraylength) {
                return result;
            }

            result = montharray[monthindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthOfTheDay", ex);
        }
        return result;
    }

    /**
     * @return ERROR->null
     * 2015/03/10=>Mar
     */
    public static String getMonthAbbrOfTheDay(Context context, String value, String format) {
        String result = null;

        try {
            String[] montharray = context.getResources().getStringArray(R.array.MonthAbbr);
            if (montharray == null || montharray.length <= 0) {
                return result;
            }

            final int arraylength = montharray.length;
            final int monthindex = getMonthIndexOfTheYear(value, format);
            if (monthindex <= -1 || monthindex >= arraylength) {
                return result;
            }

            result = montharray[monthindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthOfTheDay", ex);
        }
        return result;
    }

    /**
     * @return ERROR->null
     * 2015/03/10=>Mar
     */
    public static String getMonthAbbrOfTheDay(Context context, Date date, String format) {
        String result = null;

        try {
            String[] montharray = context.getResources().getStringArray(R.array.MonthAbbr);
            if (montharray == null || montharray.length <= 0) {
                return result;
            }

            final int arraylength = montharray.length;
            final int monthindex = getMonthIndexOfTheYear(date, format);
            if (monthindex <= -1 || monthindex >= arraylength) {
                return result;
            }

            result = montharray[monthindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthOfTheDay", ex);
        }
        return result;
    }

    /**
     * @return ERROR->null
     * 2015/03/10=>Mar
     */
    public static String getMonthAbbrOfTheDay(Context context, Calendar calendar, String format) {
        String result = null;

        try {
            String[] montharray = context.getResources().getStringArray(R.array.MonthAbbr);
            if (montharray == null || montharray.length <= 0) {
                return result;
            }

            final int arraylength = montharray.length;
            final int monthindex = getMonthIndexOfTheYear(calendar, format);
            if (monthindex <= -1 || monthindex >= arraylength) {
                return result;
            }

            result = montharray[monthindex];
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getMonthOfTheDay", ex);
        }
        return result;
    }

    public static String getDayOfTheDay(String value, String format) {
        String result = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(value));
            result = "" + calendar.get(Calendar.DAY_OF_MONTH);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDayOfTheDay", ex);
        }

        return result;
    }

    public static String getDayOfTheDay(Date date) {
        String result = null;

        try {
            Calendar calendar = getCurrentCalendar();
            calendar.setTime(date);
            result = "" + calendar.get(Calendar.DAY_OF_MONTH);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDayOfTheDay", ex);
        }
        return result;
    }

    public static String getDayOfTheDay(Calendar calendar) {
        String result = null;

        try {
            result = "" + calendar.get(Calendar.DAY_OF_MONTH);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getDayOfTheDay", ex);
        }
        return result;
    }

    /**
     * return:(leftCalendar - rightCalendar)
     */
    public static int daysOfTwo(Calendar leftCalendar, Calendar rightCalendar) {
        int day1 = leftCalendar.get(Calendar.DAY_OF_YEAR);
        int day2 = rightCalendar.get(Calendar.DAY_OF_YEAR);
        return (day1 - day2);
    }

    /**
     * return:(leftDate - rightDate)
     */
    public static int daysOfTwo(Date leftDate, Date rightDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(leftDate);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(rightDate);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        return (day1 - day2);
    }

    /**
     * return:(leftDate - rightDate)
     */
    public static int daysOfTwo(String leftDateString, String rightDateString) {
        Date left = getDateValueOfTheString(leftDateString, JTimeUtils.TIME_FORMAT_yyyyMMdd);
        Date right = getDateValueOfTheString(rightDateString, JTimeUtils.TIME_FORMAT_yyyyMMdd);
        return daysOfTwo(left, right);
    }
}
