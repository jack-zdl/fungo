package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dto.StreamInfo;
import com.fungo.system.dto.VideoBean;
import com.fungo.system.dto.VodIntroInput;
import com.fungo.system.service.IFGoGameApiAliVodService;
import com.fungo.system.service.IVdService;
import com.game.common.dto.ResultDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 阿里云vod视频业务层
 */
@Service
public class FGoGameApiAliVodServiceImpl implements IFGoGameApiAliVodService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FGoGameApiAliVodServiceImpl.class);

    //视频处理公共业务
    @Autowired
    private IVdService vdService;


    @Override
    public String getUploadAuth(VodIntroInput vodIntroInput) {

        ResultDto<Map<String, String>> resultDto = new ResultDto<Map<String, String>>();

        try {

            if (null == vodIntroInput) {
                ResultDto<Object> errorMsg = ResultDto.error("-1", "参数不能为空");
                return JSONObject.toJSONString(errorMsg);
            }

            Map<String, String> vodParamMap = new HashMap<String, String>();
            vodParamMap.put("vdFileName", vodIntroInput.getVdFileName());
            vodParamMap.put("vdTitle", vodIntroInput.getVdTitle());
            vodParamMap.put("vdTags", vodIntroInput.getVdTags());
            vodParamMap.put("vdDesc", vodIntroInput.getVdDesc());
            vodParamMap.put("videoId", vodIntroInput.getVideoId());
            vodParamMap.put("cateId", vodIntroInput.getCateId());

            Map<String, String> resultMap = vdService.getUploadAuth(vodParamMap);

            if (null == resultMap || resultMap.isEmpty()) {
                return JSONObject.toJSONString(ResultDto.error("-1", "阿里云视频点播服务获取上传凭证出现异常"));
            }

            resultDto.setData(resultMap);

        } catch (Exception ex) {
            ex.printStackTrace();
            return JSONObject.toJSONString(ResultDto.error("-1", "阿里云视频点播服务获取上传凭证出现异常"));
        }
        return JSONObject.toJSONString(resultDto);

    }


    /**
     *  获取视频播放地址
     * @param vodIntroInput
     * @return
     */
    @Override
    public String  getVideoPayURL(VodIntroInput vodIntroInput) {

        //基于视频ID获取视频播放地址
        ResultDto<List> resultDto = new ResultDto<List>();

        try {
                if (null == vodIntroInput) {
                    return  null;
                }

                String videoId = vodIntroInput.getVideoId();
                if (StringUtils.isBlank(videoId)){
                    return JSONObject.toJSONString(ResultDto.error("-1", "获取阿里云视频点播服务获取播放地址，videoId参数不能为空"));
                }

                LOGGER.info("基于视频ID从阿里云获取视频播放地址-videoId:{}",videoId);

                Map<String, String> vdUrlMap = new HashMap<String, String>();
                vdUrlMap.put("videoId", vodIntroInput.getVideoId());

                List< Map<String, String> >   vdUrlMapR = vdService.getVideoPayURL(vdUrlMap);
                if (null != vdUrlMapR && !vdUrlMapR.isEmpty()) {

                    LOGGER.info("基于视频ID从阿里云获取视频播放地址-vdURLS:{}" , vdUrlMapR.toString());

                    resultDto.setData(vdUrlMapR);

                    return  JSONObject.toJSONString(resultDto);
                }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.toJSONString(ResultDto.error("-1", "阿里云视频点播服务获取播放地址出现异常"));
    }



    @Override
    public void callBack(String video) throws Exception {

        System.out.println("压缩回调...1");
        
        //解析json信息
        VideoBean videoBean = JSON.parseObject(video, VideoBean.class);

        System.out.println("Id:" + videoBean.getVideoId());
        System.out.println("S1tatus:" + videoBean.getStatus());

        List<StreamInfo> streamInfos = videoBean.getStreamInfos();
        for (StreamInfo s : streamInfos) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Bitrate:" + s.getBitrate());
            System.out.println("Format:" + s.getFormat());
            System.out.println("Height:" + s.getHeight());
            System.out.println("Width:" + s.getWidth());
            System.out.println("Sizi:" + s.getSize());
            System.out.println("Url:" + s.getFileUrl());
        }

        if (videoBean != null) {
        	//更新视频信息
            vdService.callBack(videoBean);
        }
    }

}
