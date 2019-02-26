package cn.ssm.utils.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 获取当天0点0时0分的Date
	 * 
	 * @return
	 */
	public static Date beginOfDate() {
		// 今天0点的时间
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		Date beginOfDate = calendar.getTime();
		return beginOfDate;

	}

	/**
	 * 获取当天23点59分59秒Date
	 * 
	 * @return
	 */
	public static Date endOfDate() {
		// 今天23:59:59秒的时间
		// 获取当天23点59分59秒Date
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		Date endOfDate = calendar.getTime();
		return endOfDate;
	}

	/**
	 * 
	 * @param simpleDateFormat
	 *            yyyyMMdd
	 * @return
	 */
	public static String getTimeStr(String simpleDateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
		long now = System.currentTimeMillis();
		return sdf.format(now);
	}

}
