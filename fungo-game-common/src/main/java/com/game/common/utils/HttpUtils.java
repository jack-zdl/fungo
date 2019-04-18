package com.game.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpUtils {

	
	public static void main(String[] args) {
		
		String url = "http://localhost:8088/api/notice/pushnotice";
		JSONObject params = new JSONObject();
		params.put("name", "name");
		params.put("word", "word");
		JSONObject jsonObject = doPost(url,params);
		System.out.println(jsonObject.get("message"));
		System.out.println((boolean)jsonObject.get("success") == true);
		
	}

 /**
   * post请求
   * @param url
   * @param json
   * @return
   */
  public static JSONObject doPost(String url,JSONObject json){
	  // DefaultHttpClient client = new DefaultHttpClient(); 已过时
      HttpClient client = HttpClientBuilder.create().build();
      HttpPost post = new HttpPost(url);
      JSONObject response = null;
      try {
          StringEntity s = new StringEntity(json.toString());
          s.setContentEncoding("UTF-8");
          s.setContentType("application/json");//
          post.setEntity(s);
//          client.execute(post);
          	HttpResponse res = client.execute(post);
//          if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//           HttpEntity entity = res.getEntity();
//             String result = EntityUtils.toString(res.getEntity());// 返回json格式：
//             response = JSONObject.parseObject(result);
//          }
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
      return response;
  }
}
