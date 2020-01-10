package com.fungo.community.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.common.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SerUtils {

    private static final String head = "<!DOCTYPE html>\r\n" +
            " <html lang=\"en\">\r\n" +
            " <head><meta charset=\"UTF-8\">\r\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0,  user-scalable=0\"/>" +
            " <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n" +
            " <style>\r\n" +
            "img {\r\n" +
            "  max-width: 100%;\r\n" +
            "}\r\n" +
            "body {\r\n" +
            "  padding: 12px 4%;\r\n" +
            "  margin: 0;\r\n" +
            "}\r\n" +
            "p {\r\n" +
            "  line-height: 27px;\r\n" +
            "  font-size: 17px;\r\n" +
            "  color: #242529;\r\n" +
            "  margin: 12px 0;\r\n" +
            "  word-wrap: break-word;\r\n" +
            "}\r\n" +
            ".game {\r\n" +
            "	max-width: 80%;\r\n" +
            "  display: flex;\r\n" +
            "	box-sizing: border-box;\r\n" +
            "	justify-content: center;\r\n" +
            "  margin: 1em auto;\r\n" +
            "  box-shadow: 0px 0px 7px 0px rgba(172, 172, 172, 0.5);\r\n" +
            "  border-radius: 4px;\r\n" +
            "  height: 80px;\r\n" +
            "  padding: 10px 20px;\r\n" +
            "  cursor: pointer;\r\n" +
            "}\r\n" +
            ".game img {\r\n" +
            "  width: 60px;\r\n" +
            "  height: 60px;\r\n" +
            "  border-radius: 10px;\r\n" +
            "  margin: 0;\r\n" +
            "	cursor: auto;\r\n" +
            "}\r\n" +
            ".game .score {\r\n" +
            "  line-height: 60px;\r\n" +
            "  color: #242529;\r\n" +
            "}\r\n" +
            ".game .score .num {\r\n" +
            "  font-size: 16px;\r\n" +
            "  font-weight: bold;\r\n" +
            "}\r\n" +
            ".game .score .unit {\r\n" +
            "  font-size: 12px;\r\n" +
            "  margin-left: 4px;\r\n" +
            "}\r\n" +
            ".game .info {\r\n" +
            "  padding: 9px 10px;\r\n" +
            "  flex-grow: 1;\r\n" +
            "  overflow: hidden;\r\n" +
            "}\r\n" +
            ".game .info .name {\r\n" +
            "  color: #242529;\r\n" +
            "  font-size: 16px;\r\n" +
            "  line-height: 16px;\r\n" +
            "  white-space: nowrap;\r\n" +
            "  text-overflow: ellipsis;\r\n" +
            "  overflow: hidden;\r\n" +
            "}\r\n" +
            ".game .info .category {\r\n" +
            "  color: #12a192;\r\n" +
            "  font-size: 12px;\r\n" +
            "  margin-top: 8px;\r\n" +
            "}\r\n" +
            "" +
            " </style>\r\n" +
            " </head>\r\n" +
            " <body>";


    private static final String pcHead = "<!DOCTYPE html>\r\n" +
            " <html lang=\"en\">\r\n" +
            " <head><meta charset=\"UTF-8\">\r\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0,  user-scalable=0\"/>" +
            " <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n" +
            " <style>\r\n" +
            "img {\r\n" +
            "  max-width: 100%;\r\n" +
            " margin: 1em auto;\n"+
            " display: block;\n"+
            "  cursor: zoom-in;\n"+
            "}\r\n" +
            "body {\r\n" +
            "  padding: 12px 4%;\r\n" +
            "  margin: 0;\r\n" +
            "}\r\n" +
            "p {\r\n" +
            "  line-height: 27px;\r\n" +
            "  font-size: 17px;\r\n" +
            "  color: #242529;\r\n" +
            "  margin: 12px 0;\r\n" +
            "  word-wrap: break-word;\r\n" +
            "}\r\n" +
            ".game {\r\n" +
            "	max-width: 100%;\r\n" +
            "  display: flex;\r\n" +
            "	box-sizing: border-box;\r\n" +
//            "	justify-content: center;\r\n" +
            "	  align-items: center;\r\n" +
            "  margin: 1em auto;\r\n" +
//            "  box-shadow: 0px 0px 7px 0px rgba(172, 172, 172, 0.5);\r\n" +
            "  border-radius: 25px;\r\n" +
            "  height: 50px;\r\n" +
            "  padding: 0px 15px 0px 5px;\r\n" +
            "   background-color: #fff ;\r\n" +
            "  color: #12a192 ;\r\n" +
            "  cursor: pointer;\r\n" +
            "  box-shadow: 0px 0px 4px #B2B2B2 ;\r\n" +
            "}\r\n" +
            ".game img {\r\n" +
            "  width: 43px;\r\n" +
            "  height: 43px;\r\n" +
            "  border-radius: 50%;\r\n" +
            "  margin: 0;\r\n" +
            "	cursor: auto;\r\n" +
            "}\r\n" +
            ".game .score {\r\n" +
//            "  line-height: 60px;\r\n" +
//            "  color: #242529;\r\n" +
            "}\r\n" +
            ".game .score .num {\r\n" +
            "  font-size: 18px;\r\n" +
            "  font-weight: bold;\r\n" +
            "  color: #333333 ;\r\n" +
            "}\r\n" +
            ".game .score .unit {\r\n" +
            "  color :#B2B2B2;\r\n" +
            "  font-size: 12px;\r\n" +
            "  margin-left: 4px;\r\n" +
            "}\r\n" +
            ".game .info {\r\n" +
            "  padding: 0px 10px;\r\n" +
            "  flex-grow: 1;\r\n" +
            "  overflow: hidden;\r\n" +
            "}\r\n" +
            ".game .info .name {\r\n" +
            "  font-size: 14px;\r\n" +
//            "  color: #242529;\r\n" +

            "  line-height: 14px;\r\n" +
            "  white-space: nowrap;\r\n" +
            "  text-overflow: ellipsis;\r\n" +
            "  overflow: hidden;\r\n" +
            "  font-weight : bold;\r\n" +
            "}\r\n" +
            ".game .info .category {\r\n" +
            "  display: inline-block;\r\n" +
            "   background-color : #fff ;\r\n" +
            "   font-size :  10px ;\r\n" +
            "  padding: 0px 8px ;\r\n" +
            "    border-radius: 8px ;\r\n" +
            "    color : #12a192 ;\r\n" +
            "    border : 1px solid #12a192 ;\r\n" +
//            "  font-size: 12px;\r\n" +
//            "  margin-top: 8px;\r\n" +
            "  margin-left: 10px;\r\n" +
            "}\r\n" +
            "" +
            " </style>\r\n" +
            " </head>\r\n" +
            " <body>";


    private static final String waterMakerSuffix =
            "?x-oss-process=image/watermark,image_aW1ncy93YXRlcm1hcmsxLnBuZz94LW9zcy1wcm9jZXNzPWltYWdlL3Jlc2l6ZSxQXzE1Cg==,t_90,g_se,x_10,y_10";

    //展示帖子富文本，图片加标签
    public static String returnHtml(String origin) {
        //正则匹配，图片加标签
//		int preIndex;
//		int lastIndex;
//		Pattern p_image;
//		Matcher m_image;
//		String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
//		String regEx_img = "<\\s*img\\s+([^>]+)\\s*>";
//		p_image = Pattern.compile(regEx_img, 0);
//		m_image = p_image.matcher(origin);
//		while (m_image.find()) {
//			String group = m_image.group(0);
//			preIndex = group.indexOf("\"") + 1;
//			lastIndex = group.lastIndexOf("\"");
//			String sub = group.substring(preIndex, lastIndex);
//			origin = origin.replace(group, "<a href=\"" + sub + "\">" + group + "</a>");
//		}

        String url = head + origin + "</body></html>";
        return url;
    }

    //富文本去标签存入数据库
    public static String saveOrigin(String origin) {
        String regex = "<\\s*/?a+.*?\\s*>";//<a> </a> 分别
        Pattern p = Pattern.compile("<\\s*a.*?/a\\s*>");//完整的标签内容
        Matcher m = p.matcher(origin);

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img, 0);
        Matcher m_image;

        //如果有标签
        while (m.find()) {
            String replace = m.group().replaceAll(regex, "");
            //如果是图片
            m_image = p_image.matcher(replace);
            if (m_image.find()) {
                origin = origin.replace(m.group(), replace);
            }
        }
        return origin;
    }

    //替换图片(避免被过滤)
    public static String toOriginImageContent(String filterString, List<String> imageList) {
        if (imageList == null || imageList.size() == 0) {
            return filterString;
        }

        String regEx_img = "<\\s*img\\s+([^>]+)\\s*>";
        Matcher m_image = Pattern.compile(regEx_img).matcher(filterString);
        int a = 0;
        while (m_image.find()) {
            String group = m_image.group();
//	    	  System.out.println(group);
//	    	  filterString = filterString.replace(group,"<img src=\"" + imageList.get(a) + "\"/>");
//	    	  filterString = filterString.replace(group,"<a href=\""+imageList.get(a)+ "\">"+"<img src=\"" + imageList.get(a) + "\"/>"+"</a>");
            filterString = filterString.replace(group, "<img src='" + imageList.get(a) + "'/>");
            a++;
        }

        return filterString;
    }



    public static String returnOriginHrml(String origin) {

        return pcHead + origin + "</body></html>";

    }



    //游戏标签解析
    public static String getGameLabelFormPost(String html, String gameList) throws Exception {
        if (CommonUtil.isNull(html)) {
            return "";
        }
        if (CommonUtil.isNull(gameList)) {
            return html;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> readValue = objectMapper.readValue(gameList, ArrayList.class);
        String regEx_img = "<game[^>]+>";
        Matcher m_game = Pattern.compile(regEx_img).matcher(html);
        int i = 0;
        while (m_game.find()) {
            String group = m_game.group();
            Map<String, Object> map = readValue.get(i);
            StringBuilder s = new StringBuilder();
            s.append("<div class='game' objectId = '").append(map.get("objectId")).append("'>").append("<img src='").append(map.get("icon")).append("' alt='游戏图标' /> <div class='info'> <div class='name'>").append(map.get("name")).append("</div> <div class='category'>").append(map.get("category")).append("</div> </div>")
                    .append("<div class='score'><span class='num'>").append(map.get("rating")).append("</span><span class='unit'>分</span></div></div>");
            html = html.replace(group, s.toString());
            i++;
        }
        return html;

    }

    //图片、标签处理
    public static String getOriginImageContent(String filterString, List<String> imageList, String gameList) throws Exception {

        return getGameLabelFormPost(toOriginImageContent(filterString, imageList), gameList);
    }

    //水印、标签处理
    public static String getWatermarkImageContent(String filterString, List<String> imageList, String gameList) throws Exception {

        return getGameLabelFormPost(addWatermarkImageContent(filterString, imageList), gameList);
    }

    //显示加水印图片
    private static String addWatermarkImageContent(String filterString, List<String> imageList) {
        if (imageList == null || imageList.size() == 0) {
            return filterString;
        }

        String regEx_img = "<\\s*img\\s+([^>]+)\\s*>";
        Matcher m_image = Pattern.compile(regEx_img).matcher(filterString);
        int a = 0;
        while (m_image.find()) {
            String group = m_image.group();
            String img = imageList.get(a);
            if (!img.endsWith("gif") && !img.endsWith("GIF")) {
                filterString = filterString.replace(group, "<img src='" + img + waterMakerSuffix + "'/>");
            } else {
                filterString = filterString.replace(group, "<img src='" + img + "'/>");
            }
            a++;
        }

        return filterString;
    }


}