package com.game.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class RandomsAndId {
	/**
	* 生成订单编号<br/>
	 * 暂定为年月日+时间戳后6位+2位随机数<br/>
	 * 形如：2013091626020210<br/>
	 * 2013年09月16日 11:31:18
	 * @return String 订单ID
	 */
	public static String generate(){
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//年月日
		sb.append(sdf.format(cal.getTime()));
		//时间戳最后6位
		sb.append(String.valueOf(cal.getTimeInMillis()).substring(7));
		//2位随机数
		sb.append(String.valueOf(Math.round(Math.random()*90+10)));
		
		return sb.toString();
	}
	 
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}
	
	/**
	 * 随机获取字符串
	 * 
	 * @param length
	 *            随机字符串长度
	 * 
	 * @return 随机字符串
	 */
	public static String getRandomString(int length) {
		if (length <= 0) {
			return "";
		}
		char[] randomChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
				'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			stringBuffer.append(randomChar[Math.abs(random.nextInt()) % randomChar.length]);
		}
		return stringBuffer.toString();
	}
	
	   /**
     * 随机获取数字
     * 
     * @param length
     *            随机字符串长度
     * 
     * @return 随机字符串
     */
    public static String getRandomNum(int length) {
        if (length <= 0) {
            return "";
        }
        char[] randomChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(randomChar[Math.abs(random.nextInt()) % randomChar.length]);
        }
       return stringBuffer.toString();
    }

}
