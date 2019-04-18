package com.game.common.bean.vd;


import com.fungo.api.vd.VideoBean;
import com.game.common.entity.BasVideoJob;

import java.util.List;
import java.util.Map;


public interface IVdService {

	public void compress(String jobId,String url);

	public void callBack(VideoBean videoBean) throws Exception;
	
	public void updateVedio(BasVideoJob job);



	/**
	 *  获取上传授权凭证
	 * @param param
	 * @return
	 */
	public Map<String, String> getUploadAuth(Map<String, String> param) throws Exception ;



	/**
	 *  获取视频播放地址
	 * @param param
	 * @return
	 */
	public List< Map<String, String> > getVideoPayURL(Map<String, String> param);
}
