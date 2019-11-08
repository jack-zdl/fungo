package com.game.common.aliyun.green;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.game.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.game.common.aliyun.green.MyIAcsClient.getIAcsClient;
import static com.game.common.aliyun.green.MyIAcsClient.recognitionClient;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/11/7
 */
@Component
public class AliAcsCheck {

    private static final Logger logger = LoggerFactory.getLogger(AliAcsCheck.class);

    /**
     * 功能描述:  检查文本
     * @date: 2019/11/7 17:42
     */
    public JSONObject checkText(String text){
        JSONObject result = new JSONObject();
        result.put( "result",false);
        result.put( "text",text);
        try {
            recognitionClient = getIAcsClient();
            TextScanRequest textScanRequest = new TextScanRequest();
            textScanRequest.setAcceptFormat( FormatType.JSON); // 指定api返回格式
            textScanRequest.setHttpContentType( FormatType.JSON);
            textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
            textScanRequest.setEncoding("UTF-8");
            textScanRequest.setRegionId("cn-shanghai");
            List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
            Map<String, Object> task1 = new LinkedHashMap<String, Object>();
            task1.put("dataId", UUID.randomUUID().toString());
            /**
             * 待检测的文本，长度不超过10000个字符
             */
            task1.put("content", text);
            tasks.add(task1);
            JSONObject data = new JSONObject();

            /**
             * 检测场景，文本垃圾检测传递：antispam
             **/
            data.put("bizType", "fungo_green");
            data.put("scenes", Arrays.asList("antispam"));
            data.put("tasks", tasks);
            System.out.println( JSON.toJSONString(data, true));
            try {
                textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 请务必设置超时时间
            textScanRequest.setConnectTimeout(3000);
            textScanRequest.setReadTimeout(6000);
            try {
                HttpResponse httpResponse = recognitionClient.doAction(textScanRequest);
                if(httpResponse.isSuccess()){
                    JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                    System.out.println(JSON.toJSONString(scrResponse, true));
                    if (200 == scrResponse.getInteger("code")) {
                        JSONArray taskResults = scrResponse.getJSONArray("data");
                        for (Object taskResult : taskResults) {
                            if(200 == ((JSONObject)taskResult).getInteger("code")){
                                JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                                for (Object sceneResult : sceneResults) {
                                    String scene = ((JSONObject)sceneResult).getString("scene");
                                    String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                    //根据scene和suggetion做相关处理
                                    //suggestion == pass 未命中垃圾  suggestion == block 命中了垃圾，可以通过label字段查看命中的垃圾分类
                                    System.out.println("args = [" + scene + "]");
                                    System.out.println("args = [" + suggestion + "]");
                                    if("block".equals(suggestion)){
                                        String label =  ((JSONObject)sceneResult).getString("label");
//                                        List<String> contexts =
                                        JSONArray detailsJson = ((JSONArray)((JSONObject) sceneResult).get("details"));
                                        if(!CommonUtil.isNull(detailsJson) && detailsJson.size() > 0 ){
                                            StringBuffer contextString = new StringBuffer();
                                            JSONArray contextsArray =  ((JSONObject)detailsJson.get(0)).getJSONArray("contexts");
                                            if(!CommonUtil.isNull( contextsArray )){
                                                contextsArray.stream().forEach( s -> {
                                                    JSONObject o = (JSONObject) s;
                                                    String context = o.get( "context")+",";
                                                    contextString.append(context)  ;
                                                } );
                                            }
                                            result.put( "contexts",contextString.toString());
                                        }
                                        result.put( "result",true);
                                        result.put( "label",label);
                                    }
                                }
                                String  filteredContent = ((JSONObject)taskResult).getString("filteredContent");
                                System.out.println("111111111111"+filteredContent);
                                if(!CommonUtil.isNull( filteredContent)){
                                    result.put( "text",filteredContent);
                                }
                            }else{
                                System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                            }
                        }
                    } else {
                        System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                    }
                }else{
                    System.out.println("response not success. status:" + httpResponse.getStatus());
                }
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            logger.error( "检查文本异常",e);
        }
        return result;
    }
}
