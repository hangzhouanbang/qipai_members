package com.anbang.qipai.members.util;

public class TimeUtil {
	//获得多少天前的毫秒数
		public static long getDate(long date,long day){
			day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
			date+=day; // 相减得到新的毫秒数
			return date; 
		}
		
		//根据毫秒数获得天数
		public static int getDay(long date) {
			int day = (int) (date/24/60/60/1000);
			return day;
		}
		
		//根据天数获得毫秒数
		public static long getTimeOnDay(int day) {
			long time = day*24*60*60*1000;
			return time;
		}
}
