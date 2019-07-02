package com.game.common.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.enums.CommonEnum;
import com.game.common.enums.IEnum;
import com.game.common.util.PageTools;

import java.util.ArrayList;
import java.util.List;

public class FungoPageResultDto<T> extends AbstractResultDto {
    private static final long serialVersionUID = 1L;
    private int count = 0;
    private int after = -1;
    private int before = -1;
    /**
     * 总页数
     */
    private int pages;

    private List<T> data = new ArrayList<>();

    public FungoPageResultDto() {
        super();
        this.code = CommonEnum.SUCCESS.code();
        status = Integer.valueOf(code);
        this.message = CommonEnum.SUCCESS.message();
    }

    public FungoPageResultDto(String code, String message) {
        super();
        this.code = code;
        status = Integer.valueOf(code);
        this.message = message;
    }

    public FungoPageResultDto(IEnum ienum) {
        this.code = ienum.code();
        this.message = ienum.message();
        status = Integer.valueOf(code);
    }

    public static <T> FungoPageResultDto error(String code, String message) {
        return new FungoPageResultDto<>(code, message);
    }

    public static <T> FungoPageResultDto<T> error(IEnum ienum) {
        return new FungoPageResultDto<>(ienum);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        if (null != data) {
            this.data = data;
        }
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public static class FungoPageResultDtoFactory{


        /**
         * 警告返回请求结果和结果信息的方法
         * @param msg 结果信息
         * @return RespJson
         */
        public static FungoPageResultDto buildWarning(String code,String msg) {
            return buildInfo(CommonEnum.ERROR.code(), code, msg, null,-1,-1);
        }


        /**
         * 成功返回请求结果和结果信息和数据
         * @param data 数据
         * @return  RespJson
         */
        public static  FungoPageResultDto buildSuccess( Object data,int before,Page page) {
            FungoPageResultDto re =  buildInfo(CommonEnum.SUCCESS.code(), CommonEnum.SUCCESS.code(), CommonEnum.SUCCESS.message(), data, before,-1);
            PageTools.pageToResultDto(re,page);
            return re;
        }


        private static FungoPageResultDto buildInfo(String resultStr, String code, String msg, Object data, int before,int after) {
            FungoPageResultDto respJson = new FungoPageResultDto();
            respJson.setStatus(Integer.valueOf(resultStr));
            respJson.setCode(code);
            respJson.setMessage(msg);
            respJson.setData((List) data);
            respJson.setCount(data == null ? 0 : ((List) data).size());
            respJson.setBefore(before);
            respJson.setAfter(after);
            return respJson;
        }
    }

}

