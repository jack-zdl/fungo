package com.game.common.dto.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="评价输入",description="评价输入")
public class EvaluationInput {
    @ApiModelProperty(value="评论内容",example="")
	private String content;
    @ApiModelProperty(value="用户id",example="")
	private String user_id;
    @ApiModelProperty(value="游戏id",example="")
	private String target_id;
    @ApiModelProperty(value="是否推荐",example="")
	private boolean is_recommend;
    @ApiModelProperty(value="游戏平台",example="")
	private String phone_model;
    @ApiModelProperty(value="图片列表",example="")
	private List<String> images=new ArrayList<String>();
    @ApiModelProperty(value="打星评分(v2.4)",example="")
    private Integer rating;
    @ApiModelProperty(value="特征1评分(v2.4)画面",example="")
    private int trait1;
    @ApiModelProperty(value="特征2评分(v2.4)音乐",example="")
    private int trait2;
    @ApiModelProperty(value="特征3评分(v2.4)氪金",example="")
    private int trait3;
    @ApiModelProperty(value="特征4评分(v2.4)剧情",example="")
    private int trait4;
    @ApiModelProperty(value="特征5评分(v2.4)玩法",example="")
    private int trait5;
    @ApiModelProperty(value="特征6评分(v2.4)",example="")
    private int trait6;
    @ApiModelProperty(value="特征7评分(v2.4)",example="")
    private int trait7;
    @ApiModelProperty(value="特征8评分(v2.4)",example="")
    private int trait8;
    @ApiModelProperty(value="特征9评分(v2.4)",example="")
    private int trait9;
    @ApiModelProperty(value="特征10评分(v2.4)",example="")
    private int trait10;
    
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public boolean isIs_recommend() {
		return is_recommend;
	}
	public void setIs_recommend(boolean is_recommend) {
		this.is_recommend = is_recommend;
	}
	public String getPhone_model() {
		return phone_model;
	}
	public void setPhone_model(String phone_model) {
		this.phone_model = phone_model;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}

	public int getTrait1() {
		return trait1;
	}
	public void setTrait1(int trait1) {
		this.trait1 = trait1;
	}
	public int getTrait2() {
		return trait2;
	}
	public void setTrait2(int trait2) {
		this.trait2 = trait2;
	}
	public int getTrait3() {
		return trait3;
	}
	public void setTrait3(int trait3) {
		this.trait3 = trait3;
	}
	public int getTrait4() {
		return trait4;
	}
	public void setTrait4(int trait4) {
		this.trait4 = trait4;
	}
	public int getTrait5() {
		return trait5;
	}
	public void setTrait5(int trait5) {
		this.trait5 = trait5;
	}
	public int getTrait6() {
		return trait6;
	}
	public void setTrait6(int trait6) {
		this.trait6 = trait6;
	}
	public int getTrait7() {
		return trait7;
	}
	public void setTrait7(int trait7) {
		this.trait7 = trait7;
	}
	public int getTrait8() {
		return trait8;
	}
	public void setTrait8(int trait8) {
		this.trait8 = trait8;
	}
	public int getTrait9() {
		return trait9;
	}
	public void setTrait9(int trait9) {
		this.trait9 = trait9;
	}
	public int getTrait10() {
		return trait10;
	}
	public void setTrait10(int trait10) {
		this.trait10 = trait10;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
}


