package com.fungo.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能说明: <br>
 * 系统版本: 2.0 <br>
 * 开发人员: zhangdl <br>
 * 开发时间: 2018/3/1 17:22<br>
 */
@Component
public class AccessFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return "pre";
    }
    @Override
    public int filterOrder() {
        return 0;
    }
    @Override
    public boolean shouldFilter() {
        return true;
    }
    @Override
    public Object run() {
        System.out.println("这个请求经过gateway");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest(); //获取request

        System.out.println(String.format("%s AccessPasswordFilter request to %s", request.getMethod(), request.getRequestURL().toString()));
//        String token = request.getHeader("token");
//        String url = request.getRequestURL().toString();
//        if("/".equals(url)){
//            requestContext.setSendZuulResponse(true);  //对该请求进行路由
//            requestContext.setResponseStatusCode(HttpStatus.OK.value());
//            requestContext.set("isSuccess",true); //设置一个状态，下一个过滤器可能会用到的
//            requestContext.put(FilterConstants.REQUEST_URI_KEY, "/api/sys/index");
//            return null;
//        }
        return null;
    }
}
