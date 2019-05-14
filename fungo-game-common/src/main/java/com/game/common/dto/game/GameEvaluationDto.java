package com.game.common.dto.game;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.game.common.api.InputPageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏评价
 * </p>
 *
 * @author lzh
 * @since 2018-12-08
 */
public class GameEvaluationDto extends InputPageDto implements Serializable {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 游戏ID
     */

	private String gameId;
    /**
     * 用户Id
     */
	private String memberId;
    /**
     * 内容
     */
	private String content;
    /**
     * 图片s
     */
	private String images;
    /**
     * 手机型号
     */
	private String phoneModel;
    /**
     * 状态
	 * 0 正常
     */
	private Integer state;
    /**
     * 回复数
     */
	private Integer replyNum;
    /**
     * 点赞数
     */
	private Integer likeNum;
    /**
     * 是否推荐 2.4作废
     */
	private String isRecommend;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;
    /**
     * 属性 0:普通 1:热门 2:精华
     */
	private Integer type;
    /**
     * 标记
     */
	private Integer sort;
    /**
     * 评分 2.4
     */
	private Integer rating;
    /**
     * 游戏特征1
     */
	private Integer trait1;
    /**
     * 游戏特征2
     */
	private Integer trait2;
    /**
     * 游戏特征3
     */
	private Integer trait3;
    /**
     * 游戏特征4
     */
	private Integer trait4;
    /**
     * 游戏特征5
     */
	private Integer trait5;
    /**
     * 游戏特征6
     */
	private Integer trait6;
    /**
     * 游戏特征7
     */
	private Integer trait7;
    /**
     * 游戏特征8
     */
	private Integer trait8;
    /**
     * 游戏特征9
     */
	private Integer trait9;
    /**
     * 游戏特征10
     */
	private Integer trait10;
    /**
     * 游戏名
     */
	private String gameName;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getTrait1() {
		return trait1;
	}

	public void setTrait1(Integer trait1) {
		this.trait1 = trait1;
	}

	public Integer getTrait2() {
		return trait2;
	}

	public void setTrait2(Integer trait2) {
		this.trait2 = trait2;
	}

	public Integer getTrait3() {
		return trait3;
	}

	public void setTrait3(Integer trait3) {
		this.trait3 = trait3;
	}

	public Integer getTrait4() {
		return trait4;
	}

	public void setTrait4(Integer trait4) {
		this.trait4 = trait4;
	}

	public Integer getTrait5() {
		return trait5;
	}

	public void setTrait5(Integer trait5) {
		this.trait5 = trait5;
	}

	public Integer getTrait6() {
		return trait6;
	}

	public void setTrait6(Integer trait6) {
		this.trait6 = trait6;
	}

	public Integer getTrait7() {
		return trait7;
	}

	public void setTrait7(Integer trait7) {
		this.trait7 = trait7;
	}

	public Integer getTrait8() {
		return trait8;
	}

	public void setTrait8(Integer trait8) {
		this.trait8 = trait8;
	}

	public Integer getTrait9() {
		return trait9;
	}

	public void setTrait9(Integer trait9) {
		this.trait9 = trait9;
	}

	public Integer getTrait10() {
		return trait10;
	}

	public void setTrait10(Integer trait10) {
		this.trait10 = trait10;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

}
