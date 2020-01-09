package com.game.common.dto;


import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.enums.CommonEnum;
import com.game.common.enums.IEnum;
import com.game.common.util.PageTools;

import java.util.List;

@SuppressWarnings("serial")
public class ResultDto<T> extends AbstractResultDto {

    private T data;

    public ResultDto() {
        super();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if (null != data) {
            this.data = data;
        }
    }

    public ResultDto(IEnum ienum) {
        this.code = ienum.code();
        this.status = Integer.valueOf(this.code);
        this.message = ienum.message();
    }

    public ResultDto(IEnum ienum, T data) {
        this.code = ienum.code();
        this.status = Integer.valueOf(this.code);
        this.message = ienum.message();

        if (null != data) {
            this.data = data;
        }
    }

    private ResultDto(String code, String message) {
        super();
        this.code = code;
        this.status = Integer.valueOf(this.code);
        this.message = message;
    }

    public boolean isSuccess() {
        return "1".equals(code);
    }

    public ResultDto(T data) {
        super();
        if (null != data) {
            this.data = data;
        }
    }

    public static <T> ResultDto<T> error(String code, String message) {
        return new ResultDto<>(code, message);
    }

    public static <T> ResultDto<T> returnShowErrorMsg(String message) {
        return new ResultDto<>("0", message);
    }

    public static <T> ResultDto<T> error(IEnum ienum) {
        return new ResultDto<>(ienum);
    }

    public static <T> ResultDto<T> success(T data) {
        return new ResultDto<>(data);
    }

    public static <T> ResultDto<T> success() {
        return new ResultDto<>();
    }

    public static <T> ResultDto<T> success(String msg) {
        ResultDto<T> re = new ResultDto<>();
        re.setMessage(msg);
        return re;
    }

    public static class ResultDtoFactory{
        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static ResultDto buildError(String msg) {
            return buildInfo(CommonEnum.ERROR.code(), "-1", msg, null);
        }

        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static ResultDto buildError(String code,String msg) {
            return buildInfo(CommonEnum.ERROR.code(), code, msg, null);
        }

        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static ResultDto buildWarning(String code,String msg) {
            return buildInfo(CommonEnum.WARMING.code(), code, msg, null);
        }



        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess( String msg,Object data) {
            ResultDto re =  buildInfo(CommonEnum.SUCCESS.code(), CommonEnum.SUCCESS.code(), msg, data);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess(String code, String msg) {
            ResultDto re =  buildInfo(CommonEnum.SUCCESS.code(), code, msg, null);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess(String msg) {
            ResultDto re =  buildInfo(CommonEnum.SUCCESS.code(), CommonEnum.SUCCESS.code(), msg, null);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess(String code, String msg,Object data) {
            ResultDto re =  buildInfo(CommonEnum.SUCCESS.code(),code, msg, data);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess( Object data) {
            ResultDto re =  buildInfo(CommonEnum.SUCCESS.code(), CommonEnum.SUCCESS.code(), "", data);
            return re;
        }


        private static ResultDto buildInfo(String status, String code, String msg, Object data) {
            ResultDto resultDto = new ResultDto();
            resultDto.setStatus(Integer.valueOf(status));
            resultDto.setCode(code);
            resultDto.setMessage(msg);
            resultDto.setData(data);
            return resultDto;
        }
    }

}
