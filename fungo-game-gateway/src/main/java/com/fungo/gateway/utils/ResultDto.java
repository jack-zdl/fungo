package com.fungo.gateway.utils;


@SuppressWarnings("serial")
public class ResultDto<T>  {

    private T data;
    protected int status;
    protected String code;
    protected String message;
    private int showState;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getShowState() {
        return showState;
    }

    public void setShowState(int showState) {
        this.showState = showState;
    }

    public static class ResultDtoFactory{

        static final String ERROR = "-1";
        static final String WARMING = "0";
        static final String SUCCESS = "1";
        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static ResultDto buildError(String msg) {
            return buildInfo(ERROR, ERROR, msg, null);
        }

        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static ResultDto buildError(String code,String msg) {
            return buildInfo(ERROR, code, msg, null);
        }

        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static ResultDto buildWarning(String code,String msg) {
            return buildInfo(WARMING, code, msg, null);
        }



        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess( String msg,Object data) {
            ResultDto re =  buildInfo(SUCCESS, SUCCESS, msg, data);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess(String code, String msg) {
            ResultDto re =  buildInfo(SUCCESS, code, msg, null);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess(String msg) {
            ResultDto re =  buildInfo(SUCCESS, SUCCESS, msg, null);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess(String code, String msg,Object data) {
            ResultDto re =  buildInfo(SUCCESS,code, msg, data);
            return re;
        }

        /**
         * 成功返回请求结果和结果信息和数据
         * @return  RespJson
         */
        public static  ResultDto buildSuccess( Object data) {
            ResultDto re =  buildInfo(SUCCESS, SUCCESS, "", data);
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
