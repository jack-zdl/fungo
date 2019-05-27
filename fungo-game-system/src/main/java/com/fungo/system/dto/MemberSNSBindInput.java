package com.fungo.system.dto;

import com.game.common.dto.InputDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


/**
 * <p>
 *      登录绑定第三方账号入参数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
//@Setter
//@Getter
//@ToString
public class MemberSNSBindInput extends InputDto {

    /**
     * SNS平台类型
     * 0 微博
     * 1 微信
     * 4 QQ
     */
    @NotNull (message = "snsType 不能为空")
    private Integer snsType;

    /**
     * 用户SNS平台身份ID
     */
    @NotNull (message = "uid 不能为空")
    private String uid;

    /**
     * 用户SNS平台 有效期
     */
    private String expiration;

    /**
     * 用户SNS平台 访问令牌
     */
    @NotNull (message = "accessToken 不能为空")
    private String accessToken;

    /**
     * 用户SNS平台名称
     */
    @NotNull (message = "name 不能为空")
    private String name;

    /**
     * 用户SNS平台头像URL
     */
    @NotNull (message = "iconurl 不能为空")
    private String iconurl;

    /**
     * 用户SNS平台身份ID
     */
    @NotNull (message = "openid 不能为空")
    private String openid;

    /**
     * 用户SNS平台 唯一身份ID
     */
    @NotNull (message = "unionid 不能为空")
    private String unionid;


    /**
     * 用户SNS平台 性别
     */
    @NotNull (message = "gender 不能为空")
    private Integer gender;


    /**
     * 用户SNS平台绑定操作类型：
     *  -1 正常绑定 (默认)
     *  1 注销原账号 ,把当前手机号与当前第三方账号绑定
     *  2 重新绑定，当前第三方账号与其他手机号取消绑定
     *
     */
    private Integer bindAction = -1;

    public Integer getSnsType() {
        return snsType;
    }

    public void setSnsType(Integer snsType) {
        this.snsType = snsType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getBindAction() {
        return bindAction;
    }

    public void setBindAction(Integer bindAction) {
        this.bindAction = bindAction;
    }
}
