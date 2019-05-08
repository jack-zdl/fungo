package com.fungo.system.dto;

import io.swagger.annotations.ApiModelProperty;

public class StreamInfo {

	private String Status;
	private int Bitrate;
	private String Definition;
	@ApiModelProperty(value="视频流长度,单位秒",example="")
	private int Duration;
	private boolean Encrypt;
	private String FileUrl;
	private String Format;
	private int Fps;
	private int Height;
	@ApiModelProperty(value="视频流大小,单位Byte",example="")
	private long Size;
	private int Width;

	public String getStatus() {
		return Status;
	}

	public int getBitrate() {
		return Bitrate;
	}

	public String getDefinition() {
		return Definition;
	}

	public int getDuration() {
		return Duration;
	}

	public boolean isEncrypt() {
		return Encrypt;
	}

	public String getFileUrl() {
		return FileUrl;
	}

	public String getFormat() {
		return Format;
	}

	public int getFps() {
		return Fps;
	}

	public int getHeight() {
		return Height;
	}

	public long getSize() {
		return Size;
	}

	public int getWidth() {
		return Width;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public void setBitrate(int Bitrate) {
		this.Bitrate = Bitrate;
	}

	public void setDefinition(String Definition) {
		this.Definition = Definition;
	}

	public void setDuration(int Duration) {
		this.Duration = Duration;
	}

	public void setEncrypt(boolean Encrypt) {
		this.Encrypt = Encrypt;
	}

	public void setFileUrl(String FileUrl) {
		this.FileUrl = FileUrl;
	}

	public void setFormat(String Format) {
		this.Format = Format;
	}

	public void setFps(int fps) {
		this.Fps = fps;
	}

	public void setHeight(int height) {
		this.Height = height;
	}

	public void setSize(long size) {
		this.Size = size;
	}

	public void setWidth(int width) {
		this.Width = width;
	}

	

}
