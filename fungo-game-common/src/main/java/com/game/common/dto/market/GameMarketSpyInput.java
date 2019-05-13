package com.game.common.dto.market;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 *      运营市场推广下载游戏数据识别入参数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class GameMarketSpyInput implements Serializable {



    private String imei          ;  //设备的国际移动设备身份码

    private String imsi          ;  //设备的国际移动用户识别码
    private String model         ;  //设备的型号
    private String vendor        ;  //设备的生产厂商


    private String uuid          ;  //设备的唯一标识

    private String mac           ;  //手机wifi mac地址
    private String android_id    ;  //安卓设备的ANDROID_ID

    @NotNull(message = "game_id 不能为空")
    private String game_id       ;  //游戏Id

    private String game_name     ;  //游戏名称

    private int    action_type   ;  //运营活动数据识别类型： 1 定位点击某个游戏
    private int os               ;  //操作系统信息:android | ios

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

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    @Override
    public String toString() {
        return "GameMarketSpyInput{" +
                "imei='" + imei + '\'' +
                ", imsi='" + imsi + '\'' +
                ", model='" + model + '\'' +
                ", vendor='" + vendor + '\'' +
                ", uuid='" + uuid + '\'' +
                ", os='" + os + '\'' +
                ", mac='" + mac + '\'' +
                ", android_id='" + android_id + '\'' +
                ", game_id='" + game_id + '\'' +
                ", game_name='" + game_name + '\'' +
                ", action_type=" + action_type +
                '}';
    }

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }
}
