package com.fungo.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p></p>
 *
 * @Date: 2019/12/6
 */

//@Component
//@Configuration
//@ConfigurationProperties(prefix = "auth-skip")
public class AuthSkipUrlsProperties  { // implements InitializingBean

    private static final Logger LOGGER = LoggerFactory.getLogger( AuthSkipUrlsProperties.class);

    private static final String NORMAL = "(\\w|\\d|-)+";
    private List<Pattern> urlPatterns = new ArrayList(10);
    private List<Pattern> serverPatterns = new ArrayList(10);
    private List<String> instanceServers;
    private List<String> apiUrls;

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        instanceServers.stream().map(d -> d.replace("*", NORMAL)).map(Pattern::compile).forEach(serverPatterns::add);
//        apiUrls.stream().map(s -> s.replace("*", NORMAL)).map(Pattern::compile).forEach(urlPatterns::add);
//        LOGGER.info("============> 配置服务器ID : {} , 白名单Url : {}", serverPatterns, urlPatterns);
//    }
}
