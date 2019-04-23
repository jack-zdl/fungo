package com.game.common.dto;


import com.game.common.enums.IEnum;

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

}
