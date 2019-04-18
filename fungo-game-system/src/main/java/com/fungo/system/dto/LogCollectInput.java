package com.fungo.system.dto;

import java.io.Serializable;


/**
 * <p>
 * 		系统日志VO
 * </p>
 *
 * @author mxf
 * @since 2019-01-18
 */
public class LogCollectInput implements Serializable {

    /**
     *  1-web,
     *  2- android,
     *  3-ios,
     *  4-wap,
     *  5-小程序
     */
    private Integer origin;

    /**
     * 运行堆栈
     */
    private String runStack;

    /**
     * 功能页面地址
     */
    private String pageURL;

    /**
     * 系统运行环境
     */
    private String runEnvs;

    /**
     * 登录用户id
     */
    private String mb_id;

    /**
     *  其他
     */
    private String ext;

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getRunStack() {
        return runStack;
    }

    public void setRunStack(String runStack) {
        this.runStack = runStack;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getRunEnvs() {
        return runEnvs;
    }

    public void setRunEnvs(String runEnvs) {
        this.runEnvs = runEnvs;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }


    public String getMb_id() {
        return mb_id;
    }

    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }
}
