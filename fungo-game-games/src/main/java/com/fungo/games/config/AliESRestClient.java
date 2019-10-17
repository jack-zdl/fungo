package com.fungo.games.config;

import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/17
 */
public class AliESRestClient {


    private static final RequestOptions COMMON_OPTIONS;

    private static RestHighLevelClient highClient;


    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        // 默认缓冲限制为100MB，此处修改为30MB。
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    public static void initClinet(){
        NacosFungoCircleConfig nacosFungoCircleConfig = new NacosFungoCircleConfig();
        // 阿里云ES集群需要basic auth验证。
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //访问用户名和密码为您创建阿里云Elasticsearch实例时设置的用户名和密码，也是Kibana控制台的登录用户名和密码。
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(nacosFungoCircleConfig.getEsUser(), nacosFungoCircleConfig.getEsPassword()));

        // 通过builder创建rest client，配置http client的HttpClientConfigCallback。
        // 单击所创建的Elasticsearch实例ID，在基本信息页面获取公网地址，即为ES集群地址。
        RestClientBuilder builder = RestClient.builder(new HttpHost(nacosFungoCircleConfig.getEsHttpIp(), 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        // RestHighLevelClient实例通过REST low-level client builder进行构造。
         highClient = new RestHighLevelClient(builder);
//        return highClient;
    }

    public static RestHighLevelClient getAliEsHighClient(){
        if(highClient != null){
            return highClient;
        }else {
            initClinet();
            return highClient;
        }
    }

    public static RequestOptions getCommonOptions(){
        return COMMON_OPTIONS;
    }

}
