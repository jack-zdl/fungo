package com.game.common.aliyun.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@ConditionalOnClass(OSSClient.class)
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssAutoConfigure.class);

	private final OssProperties properties;
    
    private OSSClient ossClient;
    
    public OssAutoConfigure(OssProperties ossProperties) {
        this.properties = ossProperties;
    }
    
    @PreDestroy
    public void shutdown() {
        if(null != this.ossClient) {
            this.ossClient.shutdown();
        }
    }
    
    @Bean
    ClientConfiguration clientConfiguration() {
        ClientConfiguration config = new ClientConfiguration();
//        config.setMaxConnections(properties.getMaxConnections());
//        config.setConnectionTimeout(properties.getConnectionTimeout());
//        config.setMaxErrorRetry(properties.getMaxErrorRetry());
//        config.setSocketTimeout(properties.getSocketTimeout());
        return config;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public OSSClient ossClient() {
    	try {
    		CredentialsProvider provider=new DefaultCredentialProvider(properties.getAccessKeyId(), properties.getAccessKeySecret());
    		this.ossClient = new OSSClient(properties.getEndpoint(),provider, clientConfiguration());    		
    	}catch (Exception e) {
    		e.printStackTrace();
            LOGGER.error("初始化ossClient失败！");
		}
    	return ossClient;
    }
}
