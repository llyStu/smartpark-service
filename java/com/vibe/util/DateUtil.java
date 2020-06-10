package com.vibe.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	//获取某月的天数
	 public static int getDaysOfMonth(String dateStr) {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		    Calendar calendar = Calendar.getInstance();
		    try {
				Date date = sdf.parse(dateStr);
				calendar.setTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	    }
	/**
     * 当月第一天
     * @return
     */
    public static String getFirstDay(String yearMonth) {  //2018-12
//    	// 获取当月第一天   
    	int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
		int month = Integer.parseInt(yearMonth.split("-")[1]); //月
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
    }
	public static String getLastDayOfMonth(String yearMonth) {//获取月份的最后一天 2018-12
		int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
		int month = Integer.parseInt(yearMonth.split("-")[1]); //月
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
	/**
	* 获取指定日期当月的第一天
	* @param dateStr
	* @param
	* @return
	*/
	public static String getFirstDayOfGivenMonth(String dateStr){  //2018-12-01
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try {
	Date date = sdf.parse(dateStr);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_MONTH,1);
	calendar.add(Calendar.MONTH, 0);
	return sdf.format(calendar.getTime());
	} catch (ParseException e) {
	e.printStackTrace();
	}
	return null;
	}
	
	/**

	 * 获取指定日期下个月的第一天
	* @param dateStr
	* @param
	* @return
	*/
	public static String getFirstDayOfNextMonth(String dateStr){  //2018-12-01
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try {
	Date date = sdf.parse(dateStr);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_MONTH,1);
	calendar.add(Calendar.MONTH, 1);
	return sdf.format(calendar.getTime());
	} catch (ParseException e) {
	e.printStackTrace();
	}
	return null;
	}
	/***
	 * 获去上年同日期
	 * 
	 */
	public static String getLastYear(String dateStr){  //2018-12-01
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		Date date = sdf.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
        Date y = calendar.getTime();
		return sdf.format(y);
		} catch (ParseException e) {
		e.printStackTrace();
		}
		return null;
		}
	
	/***
	 * 获去上月同日期
	 * 
	 */
	public static String getLastMonth(String dateStr){  //2018-12-01
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, -1);
			Date y = calendar.getTime();
			return sdf.format(y);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * 判断当前日期是否周末
	 * @Author: lxx-nice@163.com
	 * @Create: 15:34 2020/5/8
	 */
	public static boolean isWeekend(String time) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		} else{
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(isWeekend("2020-05-09 00:12:15"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
