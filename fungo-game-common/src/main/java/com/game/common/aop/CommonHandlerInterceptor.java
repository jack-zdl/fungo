package com.game.common.aop;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangdelei
 * @description
 * @date 2023/3/28 09:59
 */
public class CommonHandlerInterceptor implements HandlerInterceptor {
    /**
     * @Description:  在方法被调用前执行
     * @Author: zhangdelei  2023/3/28 10:30
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * @Description: 在方法执行后调用
     * @Author: zhangdelei  2023/3/28 10:30
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
    
    /**
     * @Description:  在整个请求处理完毕后进行回调，也就是说视图渲染完毕或者调用方已经拿到响应。
     * @Author: zhangdelei  2023/3/28 10:30
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
