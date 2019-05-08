package com.fungo.system.dto;

import java.io.Serializable;

/**
 * <p>
 * 上传视频信息封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class VodIntroInput implements Serializable {
    
    //视频文件名称(包含文件后缀名) 必选
    private String vdFileName;

    //视频标题  必选
    private String vdTitle;

    //视频标签 可选 ,多个标签中间用,逗号分隔
    private String vdTags;

    //视频描述 可选
    private String vdDesc;

    //视频ID，若是刷新视频上传凭证,  必选
    private String videoId;

    //视频分类ID,可选
    private String cateId = "0";

    public String getVdFileName() {
        return vdFileName;
    }

    public void setVdFileName(String vdFileName) {
        this.vdFileName = vdFileName;
    }

    public String getVdTitle() {
        return vdTitle;
    }

    public void setVdTitle(String vdTitle) {
        this.vdTitle = vdTitle;
    }

    public String getVdTags() {
        return vdTags;
    }

    public void setVdTags(String vdTags) {
        this.vdTags = vdTags;
    }

    public String getVdDesc() {
        return vdDesc;
    }

    public void setVdDesc(String vdDesc) {
        this.vdDesc = vdDesc;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }


    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    @Override
    public String toString() {
        return "VodIntroInput{" +
                "vdFileName='" + vdFileName + '\'' +
                ", vdTitle='" + vdTitle + '\'' +
                ", vdTags='" + vdTags + '\'' +
                ", vdDesc='" + vdDesc + '\'' +
                ", videoId='" + videoId + '\'' +
                ", cateId='" + cateId + '\'' +
                '}';
    }

    //--------
}
