package com.fungo.games.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;


/**
*  t_game_list
* @author ysx
*/
@TableName("t_game_list")
public class GameList implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private String id;


    @TableField("game_id")
    private String gameId;

    /**
    * 第三方游戏id
    */
    @TableField("third_game_id")
    private String thirdGameId;

    /**
    * 榜单类型
    */
    @TableField("sort_type")
    private Integer sortType;

    /**
    * 排序
    */
    private Integer sort;

    /**
    * 0：隐藏 1:展示
    */
    private Integer status;

    /**
    * create_time
    */
    @TableField("create_time")
    private Date createTime;

    /**
    * update_time
    */
    @TableField("update_time")
    private Date updateTime;


    public GameList() {
    }

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

    public String getThirdGameId() {
        return thirdGameId;
    }

    public void setThirdGameId(String thirdGameId) {
        this.thirdGameId = thirdGameId;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}