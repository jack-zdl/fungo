package com.fungo.community.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadURLStreamRequest;
import com.aliyun.vod.upload.resp.UploadURLStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.fungo.community.function.FGAliVodClientGen;
import com.fungo.community.function.FunGoAliSTSService;
import com.fungo.community.service.IVideoService;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class VideoServiceImpl implements IVideoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoServiceImpl.class);


    private static final String accessKeyId = "LTAIsW4qLBG6M3Vf";
    private static final String accessKeySecret = "EBQbTTZGhPptMw3tGJ1grmG4r6uD7l";


    //阿里vod 存储id

    @Value("${aliyun.vod.vodRegionId}")
    private String vodRegionId;

    //阿里vod 身份id
    @Value("${aliyun.vod.vodAccessKeyId}")
    private String vodAccessKeyId;

    //阿里vod 访问秘钥
    @Value("${aliyun.vod.vodAccessKeySecret}")
    private String vodAccessKeySecret ;

    //阿里云视频点播分类id 默认：视频-pro
    @Value("${aliyun.vod.vodCateId}")
    private String vodCateId;

    @Autowired
    private FunGoAliSTSService funGoAliSTSService;


    @Override
    public Map<String, String> getUploadAuth(Map<String, String> param) throws BusinessException {

        //阿里云vod视频上传有两种方式：
        //1使用上传地址和凭证上传
        //2 使用STS方式上传
        // 这里使用 STS方式获取临时身份方式
        //获取STS 临时身份keyid、keySecret和SecurityToken
        HashMap<String, String> stsTokenMap = funGoAliSTSService.getSTSToken(vodAccessKeyId, vodAccessKeySecret);
        if (null == stsTokenMap || stsTokenMap.isEmpty()) {
            return null;
        }

        String vodAccessKeyIdSTS = stsTokenMap.get("accessKeyIDSTS");
        String vodAccessKeySecretSTS = stsTokenMap.get("accessKeySecretSTS");

        //获取阿里云vod客户端
        DefaultAcsClient aliyunClient = new DefaultAcsClient(DefaultProfile.getProfile(vodRegionId, vodAccessKeyIdSTS, vodAccessKeySecretSTS));


        //2 获取视频id，区别视频是否是首次上传
        String videoId = param.get("videoId");
        param.put("SecurityTokenSTS", stsTokenMap.get("SecurityTokenSTS"));


        if (StringUtils.isBlank(videoId)) {
            //获取视频首次上传的token等凭证
            return this.createUploadVideo(aliyunClient, param);
        } else {
            //若首次视频上传失败，则刷新再次上传的凭证
            return this.refreshUploadVideo(aliyunClient, videoId, stsTokenMap.get("SecurityTokenSTS"));
        }

    }

    @Override
    public List<Map<String, String>> getVideoPayURL(Map<String, String> param) {

        //获取阿里云vod客户端
        DefaultAcsClient aliyunClient = FGAliVodClientGen.getInstance(vodRegionId, vodAccessKeyId, vodAccessKeySecret).getAliyunVodClient();

        //获取视频id，区别视频是否是首次上传
        String videoId = param.get("videoId");


        if (StringUtils.isNotBlank(videoId)) {

            List<Map<String, String>> payURL = this.parserPayURL(aliyunClient, videoId);


            return payURL;
        }
        return null;
    }


    /**
     * 解析阿里云视频播放地址
     * 视频清晰度类型：
     * FD（流畅）
     * LD（标清）
     * SD（高清）
     * HD（超清）
     * OD（原画）
     * 2K（2K）
     * 4K（4K）
     * SQ（普通音质）
     * HQ（高音质）
     *
     * @param client
     * @param videoId
     * @return
     */
    private ArrayList<Map<String, String>> parserPayURL(DefaultAcsClient client, String videoId) {

        try {

            GetPlayInfoRequest request = new GetPlayInfoRequest();

            //系统规定参数。取值：GetPlayInfo
            request.setActionName("GetPlayInfo");
            //视频ID
            request.setVideoId(videoId);


            //调用阿里api
            GetPlayInfoResponse response = client.getAcsResponse(request);
            if (null != response) {

                List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
                if (null != playInfoList && !playInfoList.isEmpty()) {

                    ArrayList<Map<String, String>> payURLList = new ArrayList<Map<String, String>>();

                    //播放地址
                    for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                        //清晰度
                        String Definition = playInfo.getDefinition();
                        String payURL = playInfo.getPlayURL();

                        Map<String, String> urlMap = new HashMap<String, String>();
                        urlMap.put("vdDef", Definition);
                        urlMap.put("videoURL", payURL);

                        payURLList.add(urlMap);
                    }

                    return payURLList;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("解析阿里云视频播放地址:",ex);
            ex.printStackTrace();
        }

        return null;
    }


    /**
     * 获取视频首次上传的token等凭证
     *
     * @param client
     * @return
     */
    private Map<String, String> createUploadVideo(DefaultAcsClient client, Map<String, String> param) {

        Map<String, String> vodUpoadAuthMap = null;


        try {

            CreateUploadVideoRequest request = new CreateUploadVideoRequest();

            //获取视频上传地址和凭证
            String SecurityTokenSTS = param.get("SecurityTokenSTS");

            request.setSecurityToken(SecurityTokenSTS);


            //系统规定参数。取值：CreateUploadVideo
            request.setActionName("CreateUploadVideo");
            /*必选，视频源文件名称（必须带后缀, 支持 ".3gp", ".asf", ".avi", ".dat", ".dv", ".flv", ".f4v", ".gif", ".m2t", ".m3u8",
            ".m4v", ".mj2", ".mjpeg", ".mkv", ".mov", ".mp4", ".mpe", ".mpg", ".mpeg", ".mts", ".ogg", ".qt", ".rm", ".rmvb", ".swf",
            ".ts", ".vob", ".wmv", ".webm"".aac", ".ac3", ".acm", ".amr", ".ape", ".caf", ".flac", ".m4a", ".mp3", ".ra", ".wav", ".wma"）
            */
            request.setFileName(param.get("vdFileName"));
            //必选，视频标题
            request.setTitle(param.get("vdTitle"));

            //可选，分类ID
            String cateId = param.get("cateId");
            Long cateId_l = 0L;
            if (StringUtils.isNotBlank(cateId)) {
                cateId_l = Long.parseLong(cateId);
            }

            if (cateId_l <= 0 && StringUtils.isNotBlank(vodCateId)) {
                cateId_l = Long.parseLong(vodCateId);
            }

            request.setCateId(cateId_l);

            //可选，视频标签，多个用逗号分隔
            request.setTags(param.get("vdTags"));
            //可选，视频描述
            request.setDescription(param.get("vdDesc"));


            //获取响应数据
            CreateUploadVideoResponse response = client.getAcsResponse(request);

            if (null != response) {
                vodUpoadAuthMap = new HashMap<String, String>();

                String vdReqId = response.getRequestId();

                String vdToken = response.getUploadAuth();

                String vdUploadAddr = response.getUploadAddress();

                String videoId = response.getVideoId();


                vodUpoadAuthMap.put("vdReqId", vdReqId);
                vodUpoadAuthMap.put("vdToken", vdToken);
                vodUpoadAuthMap.put("vdUploadAddr", vdUploadAddr);
                vodUpoadAuthMap.put("videoId", videoId);

                //vodUpoadAuthMap.put("vodAccessKeyId", vodAccessKeyId);
                //vodUpoadAuthMap.put("vodAccessKeySecret", vodAccessKeySecret);
            }

        } catch (ServerException e) {
            LOGGER.error("获取视频首次上传的token等凭证:",e);
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            LOGGER.error("获取视频首次上传的token等凭证:",e);
            e.printStackTrace();
            return null;
        }
        return vodUpoadAuthMap;
    }

    /**
     * 若首次视频上传失败，则刷新再次上传的凭证
     *
     * @param client
     * @param videoId
     */
    private Map<String, String> refreshUploadVideo(DefaultAcsClient client, String videoId, String securityTokenSTS) {

        Map<String, String> vodUpoadAuthMap = null;

        RefreshUploadVideoResponse response = null;

        try {

            RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();

            request.setVideoId(videoId);
            response = client.getAcsResponse(request);

            if (null != response) {

                vodUpoadAuthMap = new HashMap<String, String>();

                String vdReqId = response.getRequestId();
                String vdToken = response.getUploadAuth();

                vodUpoadAuthMap.put("vdReqId", vdReqId);
                vodUpoadAuthMap.put("vdToken", vdToken);
                vodUpoadAuthMap.put("videoId", videoId);

                //vodUpoadAuthMap.put("vodAccessKeyId", vodAccessKeyId);
                //vodUpoadAuthMap.put("vodAccessKeySecret", vodAccessKeySecret);


            }

        } catch (ServerException e) {
            LOGGER.error("若首次视频上传失败，则刷新再次上传的凭证:",e);
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            LOGGER.error("若首次视频上传失败，则刷新再次上传的凭证:",e);
            e.printStackTrace();
            return null;
        }

        return vodUpoadAuthMap;
    }


    @Override
    public String compress(String url) {
        int i = url.lastIndexOf(".");
        int j = url.lastIndexOf("/");
        String title = url.substring(j + 1, i);
        String filename = url.substring(j + 1);

        return testUploadURLStream(accessKeyId, accessKeySecret, title, filename, url);

    }

    /**
     * 网络流上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param url
     */
    private String testUploadURLStream(String accessKeyId, String accessKeySecret, String title, String fileName, String url) {
        System.out.println("开始压缩 步骤2.....................");
        UploadURLStreamRequest request = new UploadURLStreamRequest(accessKeyId, accessKeySecret, title, fileName, url);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setIsShowWaterMark(true);
        /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("56f1427e96a592c7a9c5d02589e2ec26");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadURLStreamResponse response = uploader.uploadURLStream(request);
        if (response.isSuccess()) {

        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
        }
        return response.getVideoId();
    }

    //获得压缩视频播放地址
    @Override
    public String getReUrl(String videoId) {
//		return videoId;
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        String url = null;
        try {
            response = getPlayInfo(client, videoId);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            if (playInfoList.size() > 1) {
                Collections.sort(playInfoList, new SizeOrder());
            }
            if (playInfoList.size() > 0) {
                url = playInfoList.get(0).getPlayURL();
            }
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {


            }
            //Base信息
        } catch (Exception e) {
            LOGGER.error("获得压缩视频播放地址-ErrorMessage = " ,e);
        }
        return url.substring(0, url.lastIndexOf("?"));
    }
    
    
//    public List<PlayInfo> getPlayInfos(String videoId) {
////		return videoId;
//        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
//        GetPlayInfoResponse response = new GetPlayInfoResponse();
//        String url = null;
//        List<GetPlayInfoResponse.PlayInfo> playInfoList = new ArrayList<>();
//        try {
//            response = getPlayInfo(client, videoId);
//            playInfoList = response.getPlayInfoList();
//            if (playInfoList.size() > 1) {
//                Collections.sort(playInfoList, new SizeOrder());
//            }
////            if (playInfoList.size() > 0) {
////                url = playInfoList.get(0).getPlayURL();
////            }
//            //播放地址
//            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
//                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
//                System.out.print("PlayInfo.Bitrate = " + playInfo.getBitrate() + "\n");
//                System.out.print("PlayInfo.Format = " + playInfo.getFormat() + "\n");
//
//            }
//            //Base信息
//            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
//        } catch (Exception e) {
//            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
//        }
//        System.out.print("RequestId = " + response.getRequestId() + "\n");
//        return playInfoList;
//        
//    }

    /*获取播放地址函数*/
    public GetPlayInfoResponse getPlayInfo(DefaultAcsClient client, String videoId) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }

    /*获取client*/
    public DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";
//	    String regionId = "cn-hangzhou";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /*删除视频函数*/
    public DeleteVideoResponse deleteVideo(DefaultAcsClient client, String videoId) throws Exception {
        DeleteVideoRequest request = new DeleteVideoRequest();
        //多个用逗号分隔，最多支持20个
        request.setVideoIds(videoId);
        return client.getAcsResponse(request);
    }

    @Override
    public void del(String videoId) {
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        DeleteVideoResponse response = new DeleteVideoResponse();
        try {
            response = deleteVideo(client, videoId);
        } catch (Exception e) {
            LOGGER.error("ErrorMessage = " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        LOGGER.info("删除成功");
        LOGGER.info("RequestId = " + response.getRequestId() + "\n");
    }

    /**
     * 获取视频信息
     * @param client 发送请求客户端
     * @return GetVideoInfoResponse 获取视频信息响应数据
     * @throws Exception
     */
    public GetVideoInfoResponse getVideoInfo(DefaultAcsClient client, String videoId) throws Exception {
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }

    @Override
    public String getVideoImgInfo(String videoId) {
        DefaultAcsClient client = initVodClient(vodAccessKeyId, vodAccessKeySecret);
//    	DefaultAcsClient client = initVodClient("LTAILu78tcMMnOwk", "uk03aCbczR2HcMRSzNOYp4zonjCPi2");
        GetVideoInfoResponse response = new GetVideoInfoResponse();
        try {

            LOGGER.info("getVideoImgInfo-....");

            response = getVideoInfo(client, videoId);

            LOGGER.info("getVideoImgInfo-Title = " + response.getVideo().getTitle() + "\n");
            LOGGER.info("getVideoImgInfo-Description = " + response.getVideo().getDescription() + "\n");
            LOGGER.info("getVideoImgInfo-CoverUrl = " + response.getVideo().getCoverURL());
        } catch (Exception e) {
            LOGGER.error("getVideoImgInfo-ErrorMessage: " ,e);
            e.printStackTrace();
        }
//       System.out.print("RequestId = " + response.getRequestId() + "\n");
        String coverURL = response.getVideo().getCoverURL();

        return coverURL.substring(0, coverURL.lastIndexOf("?"));
    }

    public static void main(String[] args) {
        VideoServiceImpl v = new VideoServiceImpl();
        System.out.println(v.getVideoImgInfo("52109942a72246b09a6046e30e5dbca8"));

    }


}

class SizeOrder implements Comparator<GetPlayInfoResponse.PlayInfo> {
    @Override
    public int compare(GetPlayInfoResponse.PlayInfo p1, GetPlayInfoResponse.PlayInfo p2) {
        if (p1.getSize() > p2.getSize()) {
            return 1;
        } else {
            return -1;
        }

    }
}
