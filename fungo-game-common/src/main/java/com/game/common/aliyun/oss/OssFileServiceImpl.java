package com.game.common.aliyun.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.game.common.framework.file.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OssFileServiceImpl implements IFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OssFileServiceImpl.class);

	@Autowired
	private OssProperties ossProperties;

	@Autowired
	private OSSClient ossClient;


	public OSSClient getOssClient() {
		return ossClient;
	}

	public void setOssClient(OSSClient ossClient) {
		this.ossClient = ossClient;
	}

	@Override
	public String saveFile(String pathUrl,String type,File file) throws Exception {
		InputStream inputStream = null;
		try {
 			inputStream = new FileInputStream(file);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(type);
			objectMetadata.setContentLength(file.length());
			ossClient.putObject(ossProperties.getBucket(), pathUrl, inputStream, objectMetadata);
//			ossClient.copyObject(copyObjectRequest)
//			ossClient.CopyObject(new CopyObjectRequest("jchubby", "", "jchubby", ""));  
		} catch (Exception e) {
			LOGGER.error("OssFileServiceImpl-saveFile",e);
			e.printStackTrace();
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
			
		}
		return ossProperties.getImgDomain()+pathUrl;
	}

	@Override
	public String saveFile(String pathUrl, String type, InputStream fileContent) throws Exception {
		InputStream inputStream = null;
		try {
 			inputStream = fileContent;
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(type);
//			objectMetadata.setContentLength(fileContent.);
			ossClient.putObject(ossProperties.getBucket(), pathUrl, inputStream, objectMetadata);
//			ossClient.copyObject(copyObjectRequest)
//			ossClient.CopyObject(new CopyObjectRequest("jchubby", "", "jchubby", ""));  
		} catch (Exception e) {
			LOGGER.error("OssFileServiceImpl-saveFile",e);
			e.printStackTrace();
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
		return ossProperties.getImgDomain()+pathUrl;
	}
	
	
//	acs:ram::1811420479436984:role/oss-sam

	@Override
	public Map<String, String> getOssToken() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String accessKeyId = "LTAIsW4qLBG6M3Vf";
  	    String accessKeySecret = "EBQbTTZGhPptMw3tGJ1grmG4r6uD7l";//需要在RAM控制台获取，此时要给子账号权限，并建立一个角色，把这个角色赋给子账户，这个角色会有一串值，就是rolearn要填的　　　　　　　　　　//记得角色的权限，子账户的权限要分配好，不然会报错
  	    String roleArn = "acs:ram::1811420479436984:role/oss-sam";//临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
  	    String roleSessionName = "alice-002";//这个可以为空，不好写，格式要对，无要求建议为空

  	    String policy = null;
  	    ProtocolType protocolType = ProtocolType.HTTPS;
  	    AssumeRoleResponse response = StsServiceSample.assumeRole(accessKeyId, accessKeySecret, roleArn,roleSessionName, policy, protocolType);
  	        String accesskeyid = response.getCredentials().getAccessKeyId();
  	        String accesskeysecret = response.getCredentials().getAccessKeySecret();//这个就是我们想要的安全token　　
  	        String securitytoken = response.getCredentials().getSecurityToken();
  	      map.put("accesskeyid", accesskeyid);
  	      map.put("accesskeysecret", accesskeysecret);
  	      map.put("securitytoken", securitytoken);
		return map;
	}
	
	public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
	    // 当前 STS API 版本
	public static final String STS_API_VERSION = "2015-04-01";
	public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,String roleArn, String roleSessionName, String policy,     ProtocolType protocolType) throws ClientException {
		try {
	            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
	       DefaultAcsClient client = new DefaultAcsClient(profile);
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
				LOGGER.error("OssFileServiceImpl-assumeRole",e);
	            throw e;
	        }
	    }
	
	public void videoCompress(String inputPath,String filePath,String fileName,String suffix) {
		
	}
	
	public void saveVideo(String filePath,String fileName){
		// Endpoint以杭州为例，其它Region请按实际情况填写。
		String endpoint = "http://oss-cn-beijing.aliyuncs.com";
		// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
		String accessKeyId = "LTAIsW4qLBG6M3Vf";
		String accessKeySecret = "EBQbTTZGhPptMw3tGJ1grmG4r6uD7l";

		// 创建OSSClient实例。
//		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);s
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		
		// 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
		ossClient.putObject("output-mingbo", "upload/"+fileName, new File("filePath"));

		// 关闭OSSClient。
		ossClient.shutdown();
		//fix:Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		String url = ossClient.generatePresignedUrl("output-mingbo", "upload/"+fileName, expiration).toString();


		LOGGER.info("OssFileServiceImpl-saveVideo:{}",url);
	}
}
