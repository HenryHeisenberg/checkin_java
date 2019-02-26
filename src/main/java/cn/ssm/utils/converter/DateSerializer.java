package cn.ssm.utils.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


@Component
public class DateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		long nowtimelong = System.currentTimeMillis();
		long ctimelong = value.getTime();
		long result = (long) Math.abs(nowtimelong - ctimelong);
		long oneDay = (long) 86400000;
		long oneHour = (long) 3600000;
		long oneMinute = (long) 60000;
		long oneSecond = (long) 1000;
		String r = "";
		if (result < oneMinute)// 一分钟内
		{
			long seconds = result / oneSecond;
			r = seconds + "秒钟前";
		} else if (result >= oneMinute && result < oneHour)// 一小时内
		{
			long minute = result / oneMinute;
			r = minute + "分钟前";
		} else if (result >= oneHour && result < oneDay)// 一天内
		{
			long hour = result / oneHour;
			r = hour + "小时前";
		} else if (result >= oneDay && result < (oneDay * (long) 2))// 昨天
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			r = "昨天 " + sdf.format(value);
		} else if (result >= (oneDay * (long) 2) && result < (oneDay * (long) 3))// 前天
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			r = "前天 " + sdf.format(value);
		} else if (result >= (oneDay * (long) 3) && result < (oneDay * (long) 31))// 31天内
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			r = sdf.format(value);
		} else// 日期格式
		{
			// System.out.println(result);
			// System.out.println(oneDay * 30);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			r = sdf.format(value);
		}
		jsonGenerator.writeString(r);
	}

}
