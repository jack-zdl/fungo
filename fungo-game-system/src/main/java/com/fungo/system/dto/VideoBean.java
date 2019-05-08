package com.fungo.system.dto;

import java.util.List;

public class VideoBean {
	
	
	 private String EventTime;
	 private String EventType;
	 private String VideoId;
	 private String Status;
	 
	 private List<StreamInfo> StreamInfos;

	public String getEventTime() {
		return EventTime;
	}

	public String getEventType() {
		return EventType;
	}

	public String getVideoId() {
		return VideoId;
	}

	public String getStatus() {
		return Status;
	}

	public List<StreamInfo> getStreamInfos() {
		return StreamInfos;
	}

	public void setEventTime(String EventTime) {
		this.EventTime = EventTime;
	}

	public void setEventType(String EventType) {
		this.EventType = EventType;
	}

	public void setVideoId(String VideoId) {
		this.VideoId = VideoId;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public void setStreamInfos(List<StreamInfo> StreamInfos) {
		this.StreamInfos = StreamInfos;
	}


	 
	 
}
