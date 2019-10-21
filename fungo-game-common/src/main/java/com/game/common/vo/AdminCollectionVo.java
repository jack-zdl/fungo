package com.game.common.vo;


import com.game.common.bean.CollectionItemBean;

import java.util.Date;
import java.util.List;

public class AdminCollectionVo {

    /**
     * 合集id
     */
    private String id;
     /**
     * 游戏集合名称
     */
    private String name;
    /**
     * 合集封面
     */
    private String coverPicture;
    /**
     * 合集介绍
     */
    private String detail;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 游戏有效时间开始
     */
    private Date startTime;
    /**
     * 游戏有效时间结束
     */
    private Date endTime;
    /**
     * 修改人
     */
    private String updatedBy;
    /**
     * 是否上线 -1:下线 0:上线
     */
    private String isOnline;
    /**
     * 游戏信息列表
     */
    private List<CollectionItemBean> list;

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<CollectionItemBean> getList() {
        return list;
    }

    public void setList(List<CollectionItemBean> list) {
        this.list = list;
    }
}
