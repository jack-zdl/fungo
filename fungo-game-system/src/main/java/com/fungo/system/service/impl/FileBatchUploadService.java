package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;


import com.fungo.system.upload.bean.resp.UploadFileOutBean;
import com.game.common.framework.file.IFileService;
import com.game.common.util.FileTypeHandlerUtils;
import com.game.common.util.SecurityMD5;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *      文件批量上传业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Service
public class FileBatchUploadService {


    private static final Logger logger = LoggerFactory.getLogger(FileBatchUploadService.class);

    @Autowired
    private IFileService fileService;


    /**
     * 执行上传
     * @param imageUrlList
     * @param uploadFileOutBeanList
     */
    public void excuteUpload(List<String> imageUrlList, List<UploadFileOutBean> uploadFileOutBeanList) {
        try {

            logger.info(".....执行上传---解析上传的文件URL--imageUrlList: {}", JSON.toJSONString(imageUrlList));
            // 解析上传的文件URL
            parseFileURL(imageUrlList, uploadFileOutBeanList);

            //开始上传
            logger.info(".............开始上传.............");
            startUplaod(uploadFileOutBeanList);
            logger.info(".....上传完成--uploadFileOutBeanList:{}", JSON.toJSONString(uploadFileOutBeanList));

        } catch (Exception ex) {
            logger.error("批量上传图片出现异常", ex);
        }
    }


    /**
     * 解析上传的文件URL
     * @param imageUrlList
     */
    private void parseFileURL(List<String> imageUrlList, List<UploadFileOutBean> uploadFileOutBeanList) throws Exception {

        for (String urlStr : imageUrlList) {

            UploadFileOutBean fileOutBean = new UploadFileOutBean();
//            if (!urlStr.startsWith("http")) {
//                urlStr = "http://" + urlStr;
//            }
//            if (!urlStr.startsWith("HTTP")) {
//                urlStr = "http://" + urlStr;
//            }

            fileOutBean.setFileOriginal(urlStr);

            URL url = new URL(urlStr);
            fileOutBean.setFileURL(url);

            uploadFileOutBeanList.add(fileOutBean);
        }
    }


    /**
     * 执行文件上传
     * 1.获取文件后缀
     * 2.先验证文件是否符合要求
     * 3.获取文件名
     * 4.执行上传
     * @param uploadFileOutBeanList
     */
    private void startUplaod(List<UploadFileOutBean> uploadFileOutBeanList) {

        if (null == uploadFileOutBeanList || uploadFileOutBeanList.isEmpty()) {
            return;
        }

        for (UploadFileOutBean outBean : uploadFileOutBeanList) {

            //1. 获取文件后缀
            String fileOriginal = outBean.getFileOriginal();

            try {
                //文件的网络URL
                URL fileURL = outBean.getFileURL();

                String fileTypeSuffix = FileTypeHandlerUtils.getImageFileType(fileOriginal);

                if (StringUtils.isBlank(fileTypeSuffix)) {

                    InputStream fileInputStream = fileURL.openStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int len = 1024;
                    byte tmp[] = new byte[len];
                    int i;
                    while ((i = fileInputStream.read(tmp, 0, len)) > 0) {
                        baos.write(tmp, 0, i);
                    }

                    fileTypeSuffix = FileTypeHandlerUtils.getFileTypeByStream(baos.toByteArray());
                }

                //2.先验证文件是否符合要求
                if (StringUtils.isNotBlank(fileTypeSuffix)) {
                    boolean allow = FileTypeHandlerUtils.isAllow(fileTypeSuffix, FileTypeHandlerUtils.FILE_TYPE_IMG);
                    if (!allow) {
                        outBean.setHandlerStatus(2);
                        continue;
                    }
                } else {
                    //若拿不到图片的后缀名，为了用户体验，使用jpg作为默认的图片文件格式
                    fileTypeSuffix = "jpg";
                }

                //3.获取文件名
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                String ossFilePath = "content/imgs/" + sdf.format(new Date()) + "/";
                //文件名： 使用文件源地址的MD5 16位
                String fileName = SecurityMD5.encrypt16(fileOriginal) + "." + fileTypeSuffix;
                //oss路径
                ossFilePath = ossFilePath + fileName;

                outBean.setFileName(fileName);

                //4.执行上传
                //获取文件的oss绝对路径
                String ossFileURL = fileService.saveFile(ossFilePath, fileTypeSuffix, fileURL.openStream());
                outBean.setFileFungoURL(ossFileURL);
                outBean.setHandlerStatus(1);
            } catch (Exception ex) {
                outBean.setHandlerStatus(-1);
                logger.error("批量上传单个文件出现异常--文件地址:{}", fileOriginal, ex);
            }

        }
    }

}
