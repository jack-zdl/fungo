package com.fungo.community.service;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *
 *   视频服务接口
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IVideoService {

    public String compress(String url);

    public String getReUrl(String videoId);

    public void del(String videoId);


    /**
     *  获取上传授权凭证
     * @param param
     * @return
     */
    public Map<String, String>  getUploadAuth(Map<String, String> param);




    /**
     *  获取视频播放地址
     * @param param
     * @return
     */
    public List< Map<String, String> > getVideoPayURL(Map<String, String> param);

	String getVideoImgInfo(String videoId);

//	List<PlayInfo> getPlayInfos(String videoId);



}
