package com.game.common.buriedpoint.model;

import com.game.common.util.map.MapKeyMapping;

import java.util.List;

/**
 *  发布文章埋点数据实体
 */
public class BuriedPointPostModel extends BuriedPointModel{
    /**
     * 标题
     */
    private String title;
    /**
     * 圈子列表
     */
    private List<String> channel;
    /**
     * 是否带有图片
     */
    @MapKeyMapping("has_pic")
    private Boolean hasPic;
    /**
     * 是否带有视频
     */
    @MapKeyMapping("has_video")
    private Boolean hasVideo;

    /**
     * 是否带有视频
     */
    @MapKeyMapping("has_game")
    private Boolean hasGame;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getChannel() {
        return channel;
    }

    public void setChannel(List<String> channel) {
        this.channel = channel;
    }

    public Boolean getHasPic() {
        return hasPic;
    }

    public void setHasPic(Boolean hasPic) {
        this.hasPic = hasPic;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Boolean getHasGame() {
        return hasGame;
    }

    public void setHasGame(Boolean hasGame) {
        this.hasGame = hasGame;
    }
}
