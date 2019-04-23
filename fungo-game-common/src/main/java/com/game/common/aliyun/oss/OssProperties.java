package com.game.common.aliyun.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 阿里oss配置
 * @author sam
 *
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
	/**
     * 节点地址：例如杭州节点：http://oss-cn-hangzhou.aliyuncs.com
     */
    private String endpoint;

    /**
     * 访问阿里云api的授权id
     */
    private String accessKeyId;

    /**
     * 访问阿里云api的授权密钥
     */
    private String accessKeySecret;


    /**
     * 文件存储bucket
     */
    private String bucket ;

    /**
     * 需要使用图片服务域名的key后缀名
     */
    private List<String> extensions;

    /**
     * 图片服务的域名
     */
    private String imgDomain;

    /**
     * 文件服务的域名
     */
    private String fileDomain;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}

	public String getImgDomain() {
		return imgDomain;
	}

	public void setImgDomain(String imgDomain) {
		this.imgDomain = imgDomain;
	}

	public String getFileDomain() {
		return fileDomain;
	}

	public void setFileDomain(String fileDomain) {
		this.fileDomain = fileDomain;
	}
    
}
