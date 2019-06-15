package com.game.common.vo;

import com.game.common.api.InputPageDto;
import com.game.common.enums.BaseEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
//@Getter
//@Setter
public class CmmCircleVo extends InputPageDto {
    
    /**
     * 功能描述: <p>查询类型</p>
     * <p>
     *     0 全部查询
     *     1 最近浏览的圈子
     *     2 我关注的圈子
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/11 16:14
     */
    private String sorttype;

    /**
     * 功能描述:关键字查询
     * @auther: dl.zhang
     * @date: 2019/6/11 16:16
     */
    private String keyword;

    private String id;

    private String circleName;

    private String circleIcon;

    private String intro;

    private Integer type;

    private Integer state;

    private Integer memberNum;

    private Integer hotValue;

    private Integer sort;

    public String getSorttype() {
        return sorttype;
    }

    public void setSorttype(String sorttype) {
        this.sorttype = sorttype;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleIcon() {
        return circleIcon;
    }

    public void setCircleIcon(String circleIcon) {
        this.circleIcon = circleIcon;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    @Override
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public enum SorttypeEnum implements BaseEnum<SorttypeEnum,String> {
        ALL("0","全部查询"),
        BROWSE("1","最近浏览的圈子"),
        FOLLOW("2","关注圈子");
        String key;
        String value;

        SorttypeEnum(String s, String s1) {
            this.key = s;
            this.value = s1;
        }
        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

}
