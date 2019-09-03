package com.game.common.dto.mall;


import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 		商城访问日志DTO
 * </p>
 *
 * @author mxf
 * @since 2019-01-18
 */
public class MallLogsDto implements Serializable {

    private Long id;
    /**
     * 用户ID
     */
    private String mbId;
    /**
     * 页面地址
     */
    private String pageUrl;
    /**
     * 接口地址
     */
    private String iUrl;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 访问方ip地址
     */
    private String visitIp;
    /**
     * 1 页面访问
     2 点击商品
     */
    private Integer actionType;
    /**
     * 创建人ID
     */
    private String creatorId;
    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 扩展字段1
     */
    private String ext1;
    /**
     * 扩展字段2
     */
    private String ext2;
    /**
     * 扩展字段3
     */
    private String ext3;

    /**
     * 功能描述: 用户类型 x 1 新用户免费领取  2 老用户免费领取 3 分享免费领取
     * @auther: dl.zhang
     * @date: 2019/8/27 17:04
     */
    private int userType;

    /**
     * 功能描述: 用户渠道类型 x 1 APP首页轮播图、2 APP首页广告位、3 APP圈子活动栏、4 文章详情页内链接、5 APP开屏点击、6 分享页面点击
     * @auther: dl.zhang
     * @date: 2019/8/27 17:04
     */
    private int channelType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getIUrl() {
        return iUrl;
    }

    public void setIUrl(String iUrl) {
        this.iUrl = iUrl;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getVisitIp() {
        return visitIp;
    }

    public void setVisitIp(String visitIp) {
        this.visitIp = visitIp;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    @Override
    public String toString() {
        return "MallLogsDto{" +
                "id=" + id +
                ", mbId='" + mbId + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", iUrl='" + iUrl + '\'' +
                ", goodsId=" + goodsId +
                ", visitIp='" + visitIp + '\'' +
                ", actionType=" + actionType +
                ", creatorId='" + creatorId + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", ext3='" + ext3 + '\'' +
                '}';
    }
}
