package com.game.common.dto;

import com.game.common.enums.CommonEnum;
import com.game.common.enums.IEnum;

import java.util.ArrayList;
import java.util.List;

public class FungoPageResultDto<T> extends AbstractResultDto {
    private static final long serialVersionUID = 1L;
    private int count = 0;
    private int after = -1;
    private int before = -1;
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

    public static <T> FungoPageResultDto<T> error(String code, String message) {
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
