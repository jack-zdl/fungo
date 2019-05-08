package com.fungo.system.service;

import com.fungo.system.dto.VodIntroInput;

/**
 * <p>
 *    阿里云vod视频业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IFGoGameApiAliVodService {



    /**
     *获取上传授权凭证
     * @param vodIntroInput 阿里云视频上传信息
     * @return
     * @throws Exception
     */
    public String getUploadAuth(VodIntroInput vodIntroInput);

    /**
     *
     * @param video
     * @throws Exception 
     */
    public void callBack(String video) throws Exception;


    /**
     *  获取视频播放地址
     * @param vodIntroInput
     * @return
     */
    public String  getVideoPayURL(VodIntroInput vodIntroInput);
}
