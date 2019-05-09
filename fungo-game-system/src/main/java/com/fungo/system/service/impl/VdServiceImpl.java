package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dto.VideoBean;
import com.fungo.system.entity.BasVideoJob;
import com.fungo.system.service.BasVideoJobService;
import com.fungo.system.service.IVdService;
import com.fungo.system.service.IVideoService;
import com.game.common.dto.StreamInfo;
import com.game.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class VdServiceImpl implements IVdService {
//    @Autowired
//    private CmmPostService postService;

    @Autowired
    private IVideoService vdoService;

    @Autowired
    private BasVideoJobService videoJobService;

//    @Autowired
//    private MooMoodService moodService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VdServiceImpl.class);

    // 视频压缩
    @Override
    @Async
    public void compress(String jobId, String url) {

        System.out.println("开始压缩 步骤0.....................");
        System.out.println(jobId + "..................");
        BasVideoJob job = videoJobService.selectById(jobId);
        if (job != null) {
            System.out.println("开始压缩 步骤1.....................");
            String videoId = vdoService.compress(url);
            job.setVideoId(videoId);
            job.setStatus(1);
            job.setUpdatedAt(new Date());
            videoJobService.updateById(job);
        }
    }

    // 压缩完成 回调
	@Override
    @Transactional
    public void callBack(VideoBean videoBean) throws Exception {
    	
        BasVideoJob videoJob = videoJobService
                .selectOne(new EntityWrapper<BasVideoJob>().eq("video_id", videoBean.getVideoId()));
        if (videoJob != null) {//视频id已存在(已发帖或心情，未压缩)
        	LOGGER.info("阿里云视频回调,数据库无资料,请求-videoId：{}", videoBean.getVideoId());
        	String coverImg = vdoService.getVideoImgInfo(videoJob.getVideoId());
            List<StreamInfo> streamInfos = videoBean.getStreamInfos();
            if (streamInfos.size() > 1) {
                Collections.sort(streamInfos, new BT());
            }
            StreamInfo s = streamInfos.get(0);
            videoJob.setVideoCoverImage(coverImg);
            videoJob.setReVideoUrl(s.getFileUrl());
            videoJob.setStatus(2);
            videoJob.setUpdatedAt(new Date());
//          System.out.println("压缩回调...有数据");
            updateUrlAndState(videoJob, s.getFileUrl(),streamInfos);
        }else {//视频id不存在(未发帖或心情)
        	LOGGER.info("阿里云视频回调,数据库有资料,请求-videoId：{}", videoBean.getVideoId());
//        	System.out.println("压缩回调...无数据");
        	ObjectMapper mapper = new ObjectMapper();
        	 List<StreamInfo> streamInfos = videoBean.getStreamInfos();
             if (streamInfos.size() > 1) {
                 Collections.sort(streamInfos, new BT());
             }
        	videoJob = new BasVideoJob();
        	videoJob.setVideoId(videoBean.getVideoId());
        	videoJob.setStatus(2);
        	videoJob.setVideoUrls(mapper.writeValueAsString(streamInfos));
        	StreamInfo s = streamInfos.get(0);
        	videoJob.setReVideoUrl(s.getFileUrl());
        	videoJob.setCreatedAt(new Date());
        	videoJob.setUpdatedAt(new Date());
        	videoJob.insert();
        }
    }

    // 检查更新
    @Override
    @Transactional
    public void updateVedio(BasVideoJob job) {
        String reUrl = vdoService.getReUrl(job.getVideoId());
        if (!CommonUtil.isNull(reUrl)) {
            job.setReVideoUrl(reUrl);
            job.setStatus(2);
            job.setUpdatedAt(new Date());
//			CmmPost post = postService.selectById(job.getBizId());
//			if (post != null) {
//				post.setVideo(reUrl);
//				post.setState(1);
//				post.setUpdatedAt(new Date());
//				postService.updateById(post);
//				videoJobService.updateById(job);
//			}
//            updateUrlAndState(job, reUrl);
        }
    }

    @Override
    public Map<String, String> getUploadAuth(Map<String, String> param) throws Exception {

        if (null == param || param.isEmpty()) {
            return null;
        }

        try {

            return vdoService.getUploadAuth(param);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    @Override
    public List< Map<String, String> > getVideoPayURL(Map<String, String> param) {
        if (null == param || param.isEmpty()) {
            return null;
        }

        try {

            return vdoService.getVideoPayURL(param);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }


    /**
     * @todo  注释的部分
     * @param videoJob
     * @param video
     * @param streamInfos
     * @throws Exception
     */
    private void updateUrlAndState(BasVideoJob videoJob, String video,List<StreamInfo> streamInfos) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//    	if (videoJob.getBizType() == 1) {
//            CmmPost post = postService.selectById(videoJob.getBizId());
//            if (post != null) {
////            	List<Map<String, Object>> ml = getVideoUrls(streamInfos);
//            	post.setVideoCoverImage(videoJob.getVideoCoverImage());
//                post.setVideo(video);
//                post.setState(1);
//                post.setUpdatedAt(new Date());
//                post.setVideoUrls(mapper.writeValueAsString(streamInfos));
//                postService.updateById(post);
//                videoJobService.updateById(videoJob);
////              videoJobService.deleteById(videoJob);
//            }
//        } else if (videoJob.getBizType() == 2) {
//            MooMood mood = moodService.selectById(videoJob.getBizId());
//            if (mood != null) {
//            	mood.setVideoCoverImage(videoJob.getVideoCoverImage());
//                mood.setVideo(video);
//                mood.setState(0);
//                mood.setVideoUrls(mapper.writeValueAsString(streamInfos));
//                mood.setUpdatedAt(new Date());
//                moodService.updateById(mood);
//                videoJobService.updateById(videoJob);
////              videoJobService.deleteById(videoJob);
//            }
//        }
    }

	private List<Map<String, Object>> getVideoUrls(List<StreamInfo> streamInfos) {
		List<Map<String,Object>> ml = new ArrayList<>();
		for(StreamInfo s :streamInfos) {
			Map<String,Object> map = new HashMap<>();
			map.put("videoURL", s.getFileUrl());
			map.put("vdDef", s.getDefinition());
			ml.add(map);
		}
		return ml;
	}

}

class BT implements Comparator<StreamInfo> {
    @Override
    public int compare(StreamInfo s1, StreamInfo s2) {
        if (s1.getSize() > s2.getSize()) {
            return 1;
        } else {
            return -1;
        }

    }
}
