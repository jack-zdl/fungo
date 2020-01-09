package com.game.common.config;

import com.game.common.dto.ResultDto;
import com.game.common.dto.WebLog;
import com.game.common.framework.SpringContextUtil;
import com.game.common.framework.StringPrintWriter;
import com.game.common.log.IRequestLogger;
import com.game.common.util.exception.BusinessException;
import com.game.common.util.exception.CommonException;
import com.game.common.util.exception.ParamsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultDto<String> handler(Exception e) {
        WebLog weblog = (WebLog) RequestContextHolder.currentRequestAttributes().getAttribute("webLog", RequestAttributes.SCOPE_REQUEST);
        if (weblog != null) {
            weblog.setEndTime(new Date());
            weblog.setReCode("-1");
            StringPrintWriter spw = new StringPrintWriter();
            e.printStackTrace(spw);
            weblog.setOutData(spw.toString());
            IRequestLogger logs = SpringContextUtil.getBean(IRequestLogger.class);
            if (logs != null) {
                logs.log(weblog);
            }
        }
        if (e instanceof BusinessException) {
            BusinessException bizException = (BusinessException) e;
            return ResultDto.error(bizException.getCode(), bizException.getMessage());
        } else if (e instanceof ParamsException) {
            e.printStackTrace();
            return ResultDto.error("13", "请求参数错误");
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            e.printStackTrace();
            return ResultDto.error("-1", "不支持请求方式");
            //参数验证异常
        } else if (e instanceof MethodArgumentNotValidException) {

            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            BindingResult result = validException.getBindingResult();

            final List<FieldError> fieldErrors = result.getFieldErrors();

            StringBuilder builder = new StringBuilder();

            for (FieldError error : fieldErrors) {
                builder.append(error.getDefaultMessage() + " ,");
            }
            builder.deleteCharAt(builder.length() - 1);
            e.printStackTrace();
            return ResultDto.error("-1", "入参未通过验证:" + builder.toString());
        } else if(e instanceof CommonException){
            CommonException commonException = (CommonException) e;
            return ResultDto.ResultDtoFactory.buildWarning( commonException.getCode(), commonException.getMessage());
        }else {
            e.printStackTrace();
            return ResultDto.error("-1", "服务器非常繁忙，请耐心等一下");
        }
    }
}
