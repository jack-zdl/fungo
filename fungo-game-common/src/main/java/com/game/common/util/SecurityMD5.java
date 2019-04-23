
package com.game.common.util;

import java.security.MessageDigest;

/**
 * 对字符串进行MD5加密
 */
public class SecurityMD5 {
	public final static String MD5(String s){ 
		char hexDigits[] = { 
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 
		'e', 'f'}; 
		try { 
			byte[] strTemp = s.getBytes(); 
			MessageDigest mdTemp = MessageDigest.getInstance("MD5"); 
			mdTemp.update(strTemp); 
			byte[] md = mdTemp.digest(); 
			int j = md.length; 
			char str[] = new char[j * 2]; 
			int k = 0; 
			for (int i = 0; i < j; i++) { 
				byte byte0 = md[i]; 
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
				str[k++] = hexDigits[byte0 & 0xf]; 
			} 
			return new String(str); 
		} 
		catch (Exception e){ 
			return null; 
		}
	}

	/**
	 * MD16位
	 * @param SourceString
	 * @return
	 */
	public static String encrypt16(String SourceString) {
		return MD5(SourceString).substring(8, 24);
	}

	public static void main(String[] args){
		System.out.println(SecurityMD5.encrypt16("63EE2018124CA450783502EE3F19E221"));
	}
}
