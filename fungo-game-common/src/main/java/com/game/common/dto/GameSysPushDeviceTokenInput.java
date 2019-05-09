package com.game.common.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 *      消息推送目标设备token入参数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class GameSysPushDeviceTokenInput implements Serializable {

    private String imei;  //设备的国际移动设备身份码
    private String imsi;  //设备的国际移动用户识别码
    private String model;  //设备的型号
    private String vendor;  //设备的生产厂商
    private String uuid;  //设备的唯一标识
    private String mac;  //手机wifi mac地址
    private String android_id;  //安卓设备的ANDROID_ID

    /**
     * 消息推送服务平台和设备绑定的token值
     */
    @NotNull(message = "token值 d_token 不能为空")
    private String d_token;

    /**
     * 服务商类型：<br/>
     * 1 华为平台<br/>
     * 2 小米<br/>
     * 3  Oppo
     */
    @NotNull(message = "服务商类型 sb_type 不能为空")
    private int sb_type;

    /**
     * 操作系统信息:<br />
     * 1 android ;<br />
     * 2  ios
     */
    private int os;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public String getD_token() {
        return d_token;
    }

    public void setD_token(String d_token) {
        this.d_token = d_token;
    }

    public int getSb_type() {
        return sb_type;
    }

    public void setSb_type(int sb_type) {
        this.sb_type = sb_type;
    }

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }

    @Override
    public String toString() {
        return "GameSysPushDeviceTokenInput{" +
                "imei='" + imei + '\'' +
                ", imsi='" + imsi + '\'' +
                ", model='" + model + '\'' +
                ", vendor='" + vendor + '\'' +
                ", uuid='" + uuid + '\'' +
                ", mac='" + mac + '\'' +
                ", android_id='" + android_id + '\'' +
                ", d_token='" + d_token + '\'' +
                ", sb_type=" + sb_type +
                ", os=" + os +
                '}';
    }
}
