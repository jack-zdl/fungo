package com.game.common.util;

import com.alibaba.fastjson.JSONObject;
import com.game.common.util.validate.ValidateUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.http.util.TextUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

	public static void delNull(Map<String,Object> map) {
		for(String key:map.keySet()) {
			if(map.get(key) == null) {
				map.put(key, "");
			}
		}
	}
	
	public static void deleteNull(Object object) throws Exception {
		Class clazz = object.getClass();
		Method[] sourceMethods = clazz.getMethods();
		for(int i=0;i<sourceMethods.length;i++){
		  if(sourceMethods[i].getName().startsWith("get")){
			  Object loValue = sourceMethods[i].invoke(object, null);  // 值
			  String lsSourceType = sourceMethods[i].getReturnType().getName(); //类型
		     //String lsName = sourceMethods[i].getName().substring(3);   // 属性
		     //System.out.println(lsSourceType);
		     if(loValue == null) {
		    	if("java.lang.String".equals(lsSourceType)) {
		    		Method method = clazz.getMethod("set"+sourceMethods[i].getName().substring(3),String.class);   
		    		method.invoke(object, "");
		    	}else if("java.lang.Integer".equals(lsSourceType)) {
		    		Method method = clazz.getMethod("set"+sourceMethods[i].getName().substring(3),Integer.class);   
		    		method.invoke(object, 0);
		    	}else if("java.lang.Boolean".equals(lsSourceType)) {
		    		Method method = clazz.getMethod("set"+sourceMethods[i].getName().substring(3),Boolean.class);   
		    		method.invoke(object, false);
		    	}
		     }
		     
		  }/*else if(sourceMethods[i].getName().startsWith("is")) {
			  Object loValue = sourceMethods[i].invoke(object, null);  // 值
			  String lsSourceType = sourceMethods[i].getReturnType().getName(); //类型
			  System.out.println(lsSourceType);
			  if("java.lang.Boolean".equals(lsSourceType)) {
				 Method method = clazz.getMethod("set"+sourceMethods[i].getName().substring(2),boolean.class);   
		    	method.invoke(object, false);
		    }
		  }*/

		}
	}
	
	
	public static String getTableName(int tagertType) {
		if (tagertType == 1) {
			return "t_cmm_post";
		} else if (tagertType == 2) {
			return "t_moo_mood";
		} else if (tagertType == 3) {
			return "t_game";
		} else if (tagertType == 4) {
			return "t_cmm_community";
		} else if (tagertType == 5) {
			return "t_cmm_comment";
		} else if (tagertType == 6) {
			return "t_game_evaluation";
		} else if (tagertType == 8) {
			return "t_moo_message";
		}else {
			throw new RuntimeException("表名称判定错误");
		}
	}
	
	public static String filterWord(String content) {
		if(CommonUtil.isNull(content)) {
			return "";
		}
		if(SensitiveWordUtil.contains(content, SensitiveWordUtil.MaxMatchType)) {
			content = SensitiveWordUtil.replaceSensitiveWord(content, '*', SensitiveWordUtil.MaxMatchType);
		}
		
		
		return content;
	}
	
	public static ArrayList<String> returnImageUrlListFromHtml(String htmlCode) {
		ArrayList<String> imageSrcList = new ArrayList<>();
		Pattern p = Pattern.compile(
				"<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+"
						+ "(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlCode);
		String quote = null;
		String src = null;
		while (m.find()) {
			quote = m.group(1);
			src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
			imageSrcList.add(src);
		}
		// if (imageSrcList == null || imageSrcList.size() == 0) {
		// LogUtils.e("imageSrcList", "资讯中未匹配到图片链接");
		// return null;
		// }
		return imageSrcList;
	}
	
	public static String reduceString(String content,int length) {
		if(content==null){
			return "";
		}
		return content.length()>length?content.substring(0, length)+"......[省略]":content;
		
	}
	
	public static String genRandomNum(int digits) {
		int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < digits) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
//		System.out.println(pwd.toString());
        return pwd.toString();
    }



    /**
     * 按位数生成随机数字
     * @param num
     * @return
     */
    public static String getRandomDigit(int num) {
        if (num <= 0) {
            return "";
        }
        String[] vec = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        num = num <= 0 ? 1 : num;
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int r1 = Long.valueOf(Math.round(Math.random() * (vec.length - 1))).intValue();
            str.append(vec[r1]);
        }
        return str.toString();

    }

    /**
     * 版本适配
     * @param currentVersion 当前版本
     * @param targetVersion  目标版本
     */
    public static boolean versionAdapte(String currentVersion,String targetVersion) {
    	
    	if(CommonUtil.isNull(currentVersion)) {
    		return false;
    	}
    	
    	List<String> cur = Arrays.asList(currentVersion.split("\\."));
    	
    	List<String> tar = Arrays.asList(targetVersion.split("\\."));
    	
    	boolean b = true;
    	
    	for(int i = 0;i<cur.size();i++) {
    		
    		if(tar.size() > i) {//如果当前版本小于目标版本
    			if(Integer.parseInt(tar.get(i)) > Integer.parseInt(cur.get(i))) {
    				b = false;
    			}
    		}
    	}
    	
    	if(cur.size() < tar.size()) {
    		b = false;
    	}
    	
    	return b;
    	
    }
    
    public static void main(String[] args) {
    	System.out.println(versionAdapte("2.4.2","2.4.3"));
	}

	/**
	 * 功能描述: 
	 * @param: [src] 原先的对象集合 [T] 目标类
	 * @return: java.util.List<T> 拷贝后的新对象集合
	 * @auther: dl.zhang
	 * @date: 2019/5/10 16:28
	 */
	public static <T> List<T> deepCopy(List src,Class T) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}


	//泛型+反射实现json数据读取到java类
	public static <T> T getJsonClass(String json, Class<T> beanclass) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(json);
			Object obj = beanclass.newInstance();
			//拿到所以元素
			Field[] declaredFields = beanclass.getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);

				if (field.getGenericType().toString().equals(String.class.toString())) {
					String value=jsonObject.getString(field.getName());
					if(value!=null){
						field.set(obj,value);
						System.out.println(value);
					}
				} else if (field.getGenericType().toString().equals(int.class.toString())) {
					if(jsonObject.getInteger(field.getName())!=null)
						field.set(obj,jsonObject.getInteger(field.getName()));

				}

			}
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object deepCopy(Object from) {
		Object obj = null;
		try {
			// 将对象写成 Byte Array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(from);
			out.flush();
			out.close();

			// 从流中读出 byte array，调用readObject函数反序列化出对象
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		return obj;
	}


	public static boolean isNewVersion(String oldVersion, String newVersion) {
		boolean b = false;

//        oldVersion = formatVersion(oldVersion);
//        newVersion = formatVersion(newVersion);

		if (!CommonUtil.isNull(oldVersion) && !CommonUtil.isNull(newVersion)) {
			String[] oldList = oldVersion.split("\\.");
			String[] newList = newVersion.split("\\.");

			List<String> filteredOldList = subZeroAndDot(oldList);
			List<String> filteredNewList = subZeroAndDot(newList);

			for (int i = 0; i < filteredNewList.size(); i++) {
				if (filteredOldList.size() > i) {
					b = Integer.valueOf( filteredNewList.get(i)) > Integer.valueOf( filteredOldList.get(i));
					if (b) break;
				} else {
					b = true;
					break;
				}
			}
		}
		return b;
	}

	public static List<String> subZeroAndDot(String[] input) {
		int lastZeroPosition = 0;
		for (int i = 0; i < input.length; i++) {
			String numStr = input[i];
			if (!TextUtils.isEmpty(numStr) && ValidateUtils.numberToInt(numStr)) {
				int num = Integer.valueOf(numStr);
				if (num != 0) {
					lastZeroPosition = i;
				}
			}
		}
		List<String> inputList = Arrays.asList(input);
		return inputList.subList(0, lastZeroPosition + 1);
	}

	public static String  getVersion(String mainVersion,String childVersion){
		String version = null;
		if(!CommonUtil.isNull( childVersion  )){
			version = mainVersion +"."+childVersion;
		}else {
			version = mainVersion;
		}
		return  version;
	}

	public static String comparingByName(Map<String, Object> map){
		return (String) map.get("objectId");
	}

}
