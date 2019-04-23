package com.game.common.aliyun.oss;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

public class StsServiceSample {
    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
//    public static final String REGION_CN_HANGZHOU = "cn-shanghai";
    // 当前 STS API 版本
    public static final String STS_API_VERSION = "2015-04-01";
    public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,String roleArn, String roleSessionName, String policy,ProtocolType protocolType) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            if(profile==null) {
            	System.out.println("profile为空");
            }else {
            	System.out.println("feikong");
            }
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);
 
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
 
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
 
            return response;
        } catch (ClientException e) {
            throw e;
        }
    }
    
    public static void main(String[] args) {

    	String accessKeyId = "LTAIsW4qLBG6M3Vf";
  	    String accessKeySecret = "EBQbTTZGhPptMw3tGJ1grmG4r6uD7l";//需要在RAM控制台获取，此时要给子账号权限，并建立一个角色，把这个角色赋给子账户，这个角色会有一串值，就是rolearn要填的　　　　　　　　　　//记得角色的权限，子账户的权限要分配好，不然会报错
  	    String roleArn = "acs:ram::1811420479436984:role/oss-sam";//临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
  	    String roleSessionName = "alice-002";//这个可以为空，不好写，格式要对，无要求建议为空
  	    String policy = null;
  	    ProtocolType protocolType = ProtocolType.HTTPS;
  	    try {
			AssumeRoleResponse response = StsServiceSample.assumeRole(accessKeyId, accessKeySecret, roleArn,roleSessionName, policy, protocolType);
			 String accesskeyid = response.getCredentials().getAccessKeyId();
	  	        String accesskeysecret = response.getCredentials().getAccessKeySecret();//这个就是我们想要的安全token　　
	  	        String securitytoken = response.getCredentials().getSecurityToken();
	  	        System.out.println("accesskeyid"+ accesskeyid);
	  	      System.out.println("accesskeysecret"+accesskeysecret);
	  	    System.out.println("securitytoken"+securitytoken);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	    
    }
}
