package com.fungo.system.upload.bean.resp;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.net.URL;

/**
 * <p>
 *      文件上传结果出参封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class UploadFileOutBean implements Serializable {

    /**
     * 文件源路径
     * 网络资源
     * 本地file
     */
    private String fileOriginal;

    /**
     * 网络文件的URL地址
     */
    @JsonIgnore
    private URL fileURL;


    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件源路径MD5
     */
    @JsonIgnore
    private String originalMd5;


    /**
     * 文件上传后在Fungo平台的URL
     */
    private String fileFungoURL;

    /**
     * 文件处理状态：
     * 1 上传成功
     * -1 上传失败
     * 2 文件不符合要求
     */
    private Integer handlerStatus;

    /**
     * 文件大小 KB(千字节)
     */
    private Long fileSize;


    public String getFileOriginal() {
        return fileOriginal;
    }

    public void setFileOriginal(String fileOriginal) {
        this.fileOriginal = fileOriginal;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalMd5() {
        return originalMd5;
    }

    public void setOriginalMd5(String originalMd5) {
        this.originalMd5 = originalMd5;
    }

    public Integer getHandlerStatus() {
        return handlerStatus;
    }

    public void setHandlerStatus(Integer handlerStatus) {
        this.handlerStatus = handlerStatus;
    }

    public URL getFileURL() {
        return fileURL;
    }

    public void setFileURL(URL fileURL) {
        this.fileURL = fileURL;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileFungoURL() {
        return fileFungoURL;
    }

    public void setFileFungoURL(String fileFungoURL) {
        this.fileFungoURL = fileFungoURL;
    }
}
