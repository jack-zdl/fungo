package com.game.common.enums;


public enum CommonEnum implements IEnum {
	SUCCESS(
			"1", "请求成功"
	), ERROR(
			"-1", "系统异常"
	), ERROR_ALIYUN_SMS(
			"-1", "获取验证码次数已超过上限，账号异常，请24小时后重试"
	),
	//////////////////////
	PARAM_VALID_FAIL(
			"-1", "系统异常"
	),
	//////////////////////
	HTTP_FAIL_400(
			"-1", "系统异常"
	), HTTP_FAIL_403(
			"-1", "权限异常"
	), HTTP_FAIL_405(
			"-1", "系统异常"
	), HTTP_FAIL_415(
			"-1", "系统异常"
	), HTTP_FAIL_500(
			"-1", "系统异常"
	), HTTP_FAIL_666(
			"-1", "非法请求"
	),
	UNACCESSRULE(
			"-1","权限不足"
	),
	//////////////////////
	LOGIN_TIMEOUT(
			"24", "登录超时，请稍后再试"
	);
	private CommonEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	private String code;
	private String message;

	@Override
	public String code() {
		return this.code;
	}

	@Override
	public String message() {
		return this.message;
	}
}