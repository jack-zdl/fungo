package com.game.common.dto.index;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.common.dto.game.GameOut;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ApiModel(value = "卡片信息对象", description = "卡片信息对象")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class CardIndexBean {
    @ApiModelProperty(value = "卡片名称", example = "")
    private String cardName;

    @ApiModelProperty(value = "数据记录数", example = "")
    private int size;

    @ApiModelProperty(value = "样式(1:banner,2:活动位，3：本周精选，4：精选文章，5：安利墙，6：精选文章（视频），7：大家都在玩，8：社区置顶文章)", example = "")
    private int cardType;

    @ApiModelProperty(value = "排序", example = "")
    private int order;

    @ApiModelProperty(value = "数据列表", example = "")
    private ArrayList<CardDataBean> dataList;

    @ApiModelProperty(value = "数据url", example = "")
    private String dataUrl;

    @ApiModelProperty(value = "标签扩展数据", example = "")
    private ArrayList<String> tag = new ArrayList<String>();

    @ApiModelProperty(value = "扩展数据", example = "")
    private Map<String, Object> extendData = new HashMap<String, Object>();

    @ApiModelProperty(value = "右侧按钮事件", example = "")
    private boolean uprightFlag = false;

    @ApiModelProperty(value = "右侧按钮事件", example = "")
    private ActionBean uprightAction = new ActionBean();


    /**
     * 首页
     *  安利墙
     *  游戏攻略
     *  精华文章区
     *  关联的游戏数据
     * @return
     */
    private ArrayList<GameOut> gameDatas = new ArrayList<GameOut>();


    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ArrayList<CardDataBean> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<CardDataBean> dataList) {
        this.dataList = dataList;
    }

    public boolean isUprightFlag() {
        return uprightFlag;
    }

    public void setUprightFlag(boolean uprightFlag) {
        this.uprightFlag = uprightFlag;
    }

    public ActionBean getUprightAction() {
        return uprightAction;
    }

    public void setUprightAction(ActionBean uprightAction) {
        this.uprightAction = uprightAction;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public Map<String, Object> getExtendData() {
        return extendData;
    }

    public void setExtendData(Map<String, Object> extendData) {
        this.extendData = extendData;
    }


}
