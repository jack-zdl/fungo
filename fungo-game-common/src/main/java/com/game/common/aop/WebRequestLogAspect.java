package com.game.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.common.dto.UserProfile;
import com.game.common.dto.WebLog;
import com.game.common.framework.SpringContextUtil;
import com.game.common.log.IRequestLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Aspect
@Component
public class WebRequestLogAspect {

    private static Logger logger = LoggerFactory.getLogger(WebRequestLogAspect.class);

//	@Autowired
//	private ILogStorage  logStorage;


    @Pointcut("execution(public * com..*.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()") //之前执行
    public void deBefore(JoinPoint joinPoint) {
        try {
            WebLog weblog = new WebLog();
            weblog.setCreatedAt(new Date());
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // 记录下请求内容
            ObjectMapper objectMapper = new ObjectMapper();
            UserProfile member = null;
            String os = request.getHeader("os");
            if (os == null) {
                os = "";
            }
            weblog.setChannel(os);
            String brand = request.getHeader("brand");
            if (brand == null) {
                brand = "";
            }
            weblog.setChannel(os);
            String bizId = request.getHeader("bizId");
            if (bizId == null) {
                bizId = "";
            }
            weblog.setBizId(bizId);
            String appversion = request.getHeader("appversion");
            if (appversion == null) {
                appversion = "";
            }
            weblog.setAppversion(appversion);
            String height = request.getHeader("height");
            if (height == null) {
                height = "";
            }
            weblog.setHeight(height);
            String width = request.getHeader("width");
            if (width == null) {
                width = "";
            }
            weblog.setWidth(width);
            String version = request.getHeader("version");
            if (version == null) {
                version = "";
            }
            weblog.setVersion(version);
            String udid = request.getHeader("udid");
            if (udid == null) {
                udid = "";
            }
            weblog.setUdid(udid);

            request.setAttribute("os", os);

//		String os=request.getHeader("os");
//        if(os==null) {
//        	os="";
//        }

            Object inputObj = null;
            Object[] args = joinPoint.getArgs();
            try {
                for (int i = 0; i < args.length; i++) {
                    Object o = args[i];
                    if (o != null) {
                        Class<?> clz = o.getClass();
                        if (UserProfile.class.isAssignableFrom(clz)) {
                            member = (UserProfile) o;
                        } else if (MultipartFile.class.isAssignableFrom(clz)) {
                            break;
                        } else if (HttpServletResponse.class.isAssignableFrom(clz)) {
                            break;
                        } else if (HttpSession.class.isAssignableFrom(clz)) {
                            break;
                        } else if (HttpServletRequest.class.isAssignableFrom(clz)) {
                            break;
                        } else if (BeanPropertyBindingResult.class.isAssignableFrom(clz)) {
                            break;
                        } else {
                            inputObj = o;
                        }

                    }
                }
            } catch (Exception e) {
                logger.error("方法的请求处理出现异常:",e);
                e.printStackTrace();
            }
            StringBuffer sb = new StringBuffer();
            sb.append("!");
            sb.append("|").append(os);
            sb.append(request.getMethod());
            sb.append("|");
            weblog.setMethod(request.getMethod());
            sb.append("|").append(request.getRequestURI().toString() + "|");
            String path = request.getRequestURI().toString();
            weblog.setPath(path);
            String end = path.substring(path.lastIndexOf("/") + 1);
            if (32 == end.length()) {
                weblog.setBizId(end);
            }
            if (member == null) {
                sb.append("[Anonymous]");
                weblog.setMemberId("");
            } else {
                sb.append(member.getLoginId());
                weblog.setMemberId(member.getLoginId());
            }
            sb.append("|");
            sb.append(getIpAddr(request));
            weblog.setIp(getIpAddr(request));
            sb.append("|");
            sb.append(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            sb.append("|");
            String input = objectMapper.writeValueAsString(inputObj);
            sb.append(input);
            weblog.setInputData(input);
            request.setAttribute("webLog", weblog);

            logger.info("方法请求参数:{}", sb.toString());
        } catch (Exception ex) {
            logger.error("方法的请求处理出现异常:",ex);
            ex.printStackTrace();
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String out = objectMapper.writeValueAsString(ret);

            logger.info("方法的返回值:{} ", out);

            WebLog weblog = (WebLog) RequestContextHolder.currentRequestAttributes().getAttribute("webLog", RequestAttributes.SCOPE_REQUEST);
            weblog.setEndTime(new Date());
            weblog.setOutData(out);
            IRequestLogger logs = SpringContextUtil.getBean(IRequestLogger.class);
            if (logs != null) {
                logs.log(weblog);
            }
        } catch (Exception ex) {
            logger.error("方法的返回处理出现异常:",ex);
            ex.printStackTrace();
        }
    }

    /**
     * 拦截web层异常，记录异常日志，并返回友好信息到前端
     * 目前只拦截Exception，是否要拦截Error需再做考虑
     *
     * @param e 异常对象
     */
    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void handleThrowing(Exception e) {
        //不需要再记录ServiceException，因为在service异常切面中已经记录过
            logger.error(e.getMessage(),e);
    }

    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
