package com.game.common.util.properties;


/**
 * 
 * 功能说明:
 *
 * 系统版本：V0.1
 *
 * 开发人员：chx
 *
 * 开发时间:2010-上午10:59:00
 * 
 * TODO
 */
public class UIExtendPropertiesUtil {
	
//	/**
//	 * UI扩展信息配置文件路径
//	 */
//	private static final String UIEXT_PROPERTIES_FILE_PATH = "/ui-ext.properties";
//
//	/**
//	 * UI扩展信息配置
//	 */
//	private static Properties uiExtProperties = new Properties();
//	static {
//		try {
//			InputStream stream = BizframeException.class
//					.getResourceAsStream(UIEXT_PROPERTIES_FILE_PATH);
//			uiExtProperties.load(stream);
//			stream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	/**
//	 * 根据key值获取配置文件中的属性值
//	 *
//	 * @param key
//	 * 			配置键值
//	 * @return
//	 *          配置值，若无key键值配置 还回空字符串：""
//	 */
//	public static String getProperty(String key){
//		return uiExtProperties.getProperty(key, "");
//	}
//
//
//	/**
//	 * 根据key值获取配置文件中的属性值
//	 *
//	 * @param key
//	 * 			配置键值
//	 * @param defaultValue
//	 * 			无此配置时的默认值
//	 * @return
//	 *          配置值,若无key值配置则返回 defaultValue
//	 */
//	public static String getProperty(String key,String defaultValue){
//		return uiExtProperties.getProperty(key, defaultValue);
//	}
//
//
//	/**
//	 * 根据key值获取配置文件中的boolean属性值
//	 *
//	 * @param key
//	 * 			配置键值
//	 * @return
//	 *          配置值，若无key键值配置 还回false
//	 */
//	public static boolean getBoolProperty(String key){
//		String bool=uiExtProperties.getProperty(key);
//		return (null!=bool&&"true".equals(bool.toLowerCase()));
//	}
//
//	/**
//	 * 根据key值获取配置文件中的boolean属性值
//	 *
//	 * @param key
//	 * 			配置键值
//	 * @param defaultValue
//	 * 			无此配置时的默认值
//	 * @return
//	 *          配置值,若无key值配置则返回 defaultValue
//	 */
//	public static boolean getBoolProperty(String key,boolean defaultValue){
//		String bool=uiExtProperties.getProperty(key);
//		if(null==bool||"".equals(bool)){
//			return defaultValue;
//		}else{
//			return "true".equals(bool.toLowerCase());
//		}
//
//	}
//
//
//	/**
//	 *
//	 * @param key
//	 * @param message
//	 * @return
//	 */
//	public static String getProperty(String key, Object... message){
//		String pattern = uiExtProperties.getProperty(key);
//		if (pattern != null) {
//			return MessageFormat.format(pattern, message);
//		} else {
//			return MessageFormat.format("{0}", message);
//		}
//	}
}
