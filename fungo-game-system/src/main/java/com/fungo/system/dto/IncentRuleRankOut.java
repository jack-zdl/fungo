package com.fungo.system.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Map;


/**
 * <p>
 *      用户级别、身份、荣誉规则数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class IncentRuleRankOut extends DignityRule implements Serializable {

    /**
     * 主键
     */
    @JsonIgnore
    private Long id;
    /**
     * 属于的规则种类ID
     */
    private String rankGroupId;
    /**
     * 级别代码
     */
    @JsonIgnore
    private String rankCode;
    /**
     * 规则编码(int型)
     */
    @JsonIgnore
    private Integer rankIdt;
    /**
     * 级别名称
     */
    @JsonIgnore
    private String rankName;
    /**
     * 级别介绍
     */
    private String rankIntro;
    /**
     * 级别logo图标集,json格式
     * 图标的状态( 1 标准、2 点亮、3 置灰)，
     * 图标的顺序(自然序号)，
     */
    @JsonIgnore
    private String rankImgs;
    /**
     * 是否启用
     * 1启用
     * 2停用
     */
    @JsonIgnore
    private Integer isActive;
    /**
     * 权益类型
     * 1 级别
     * 2 身份
     * 3 荣誉
     * 4 特权服务
     */
    private Integer rankType;
    /**
     * 授权率
     * 如：98.9%
     */
    private Float grantRate;

    /**
     * 优先级排序
     */
    private Integer sort;


    /**
     *  荣誉标识：
     *          1 FunGo身份证
     *          2  会心一击
     *          3 拓荒者
     *          4 神之手
     *          5 Fun之意志
     *          6 专属活动
     *          7 其他
     */
    private Integer rankFlag;

    /**
     * 已经获取荣誉对象
     */
    private Map<String, Object> accreditHonor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRankGroupId() {
        return rankGroupId;
    }

    public void setRankGroupId(String rankGroupId) {
        this.rankGroupId = rankGroupId;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    public Integer getRankIdt() {
        return rankIdt;
    }

    public void setRankIdt(Integer rankIdt) {
        this.rankIdt = rankIdt;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankIntro() {
        return rankIntro;
    }

    public void setRankIntro(String rankIntro) {
        this.rankIntro = rankIntro;
    }

    public String getRankImgs() {
        return rankImgs;
    }

    public void setRankImgs(String rankImgs) {
        this.rankImgs = rankImgs;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getRankType() {
        return rankType;
    }

    public void setRankType(Integer rankType) {
        this.rankType = rankType;
    }

    public Float getGrantRate() {
        return grantRate;
    }

    public void setGrantRate(Float grantRate) {
        this.grantRate = grantRate;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getRankFlag() {
        return rankFlag;
    }

    public void setRankFlag(Integer rankFlag) {
        this.rankFlag = rankFlag;
    }

    public Map<String, Object>  getAccreditHonor() {
        return accreditHonor;
    }

    public void setAccreditHonor(Map<String, Object>  accreditHonor) {
        this.accreditHonor = accreditHonor;
    }

    @Override
    public String toString() {
        return "IncentRuleRankOut{" +
                "id=" + id +
                ", rankGroupId='" + rankGroupId + '\'' +
                ", rankCode='" + rankCode + '\'' +
                ", rankIdt=" + rankIdt +
                ", rankName='" + rankName + '\'' +
                ", rankIntro='" + rankIntro + '\'' +
                ", rankImgs='" + rankImgs + '\'' +
                ", isActive=" + isActive +
                ", rankType=" + rankType +
                ", grantRate=" + grantRate +
                ", sort=" + sort +
                ", rankFlag=" + rankFlag +
                ", accreditHonor=" + accreditHonor +
                '}';
    }
}
