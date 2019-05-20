package com.game.common.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

//@Service
public class FunGoAliSTSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunGoAliSTSService.class);

    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    // 当前 STS API 版本
    public static final String STS_API_VERSION = "2015-04-01";


    /**
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    public HashMap<String, String> getSTSToken(String accessKeyId, String accessKeySecret) {


        // 只有RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys

        // AssumeRole API 请求参数: RoleArn, RoleSessionName, Policy, and DurationSeconds
        // RoleArn 需要在 RAM 控制台上获取
        String roleArn = "acs:ram::1811420479436984:role/oss-sam"; // 即角色详情页的Arn值
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        String roleSessionName = "STSroleSessionName"; // 自定义即可
        // 定制你的policy
        String policy = "{\n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": \"sts:AssumeRole\",\n" +
                "            \"Effect\": \"Allow\",\n" +
                "            \"Principal\": {\n" +
                "                \"RAM\": [\n" +
                "                    \"acs:ram::1811420479436984:root\"\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"Version\": \"1\"\n" +
                "}";
        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;


        try {

            final AssumeRoleResponse response = assumeRole(accessKeyId, accessKeySecret,
                    roleArn, roleSessionName, policy, protocolType);

            if (null == response) {

                return null;
            }

            String expiration = response.getCredentials().getExpiration();

            String accessKeyIDSTS = response.getCredentials().getAccessKeyId();

            String accessKeySecretSTS = response.getCredentials().getAccessKeySecret();
            String SecurityTokenSTS = response.getCredentials().getSecurityToken();


            HashMap<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("expiration", expiration);
            resultMap.put("accessKeyIDSTS", accessKeyIDSTS);
            resultMap.put("accessKeySecretSTS", accessKeySecretSTS);
            resultMap.put("SecurityTokenSTS", SecurityTokenSTS);


            LOGGER.info("FunGoAliSTSService--Expiration: " + expiration);
            LOGGER.info("FunGoAliSTSService--Access Key Id: " + accessKeyIDSTS);
            LOGGER.info("FunGoAliSTSService--Access Key Secret: " + accessKeySecretSTS);
            LOGGER.info("FunGoAliSTSService--Security Token: " + SecurityTokenSTS);


            return resultMap;

        } catch (ClientException e) {
            e.printStackTrace();
            LOGGER.error("FunGoAliSTSService--Failed to get a token.",e);
            LOGGER.error("FunGoAliSTSService--Error code: " + e.getErrCode());
            LOGGER.error("FunGoAliSTSService--Error message: " + e.getErrMsg());
        }
        return null;

    }


    /**
     * @param accessKeyId
     * @param accessKeySecret
     * @param roleArn
     * @param roleSessionName
     * @param policy
     * @param protocolType
     * @return
     * @throws ClientException
     */
    private AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                          String roleArn, String roleSessionName, String policy,
                                          ProtocolType protocolType) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            // request.setPolicy(policy);
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (ClientException e) {
            e.printStackTrace();
            LOGGER.error("FunGoAliSTSService--Error:",e);
            throw e;
        }
    }

//--------
}
