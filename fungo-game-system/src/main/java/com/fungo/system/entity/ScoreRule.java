package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 规则
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@TableName("t_score_rule")
public class ScoreRule extends Model<ScoreRule> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 所属任务类别id
     */
    @TableField("group_id")
    private String groupId;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否启用
     */
    @TableField("is_active")
    private Integer isActive;
    /**
     * 扩展
     */
    private String extra;
    /**
     * 标题
     */
    private String tip;
    /**
     * 单位内最大获取收益
     */
    private Integer max;
    /**
     * 评分
     */
    private Integer score;
    /**
     * 行为代码
     */
    private String code;
    /**
     * 简介
     */
    private String intro;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;
    /**
     * 任务编码的int型值
     */
    @TableField("code_idt")
    private Integer codeIdt;
    /**
     * 收益频率类型

     0 无 1 天 2 用户生命周期 3 无限制 4 数据 5 周 6 月7 季度 8 半年 9 年
     */
    @TableField("incomie_freg_type")
    private Integer incomieFregType;
    /**
     * 任务对应的功能接口URL
     */
    @TableField("action_url")
    private String actionUrl;
    /**
     * 任务类型
     1 分值
     11 任务 获取经验值
     2 虚拟币
     21 营销活动  获取fungo币
     22  签到获取fungo币
     220 V2.4.6签到版本签到获取fungo币
     23 任务 获取fungo币
     */
    @TableField("task_type")
    private Integer taskType;

    /**
     * 授权等级
     * 		-1 无等级限制
     */
    @TableField("auth_level")
    private Integer authLevel;

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
     * 收益频率介绍
     */
    @TableField("income_freq_intro")
    private String incomeFreqIntro;
    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;
    /**
     * 创建人名称
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 若是虚拟币任务，对应的经验值任务ID
     */
    @TableField("pls_task_id")
    private String plsTaskId;

    /**
     * 分值类规则对应的虚拟币规则
     */
    @TableField(exist = false)
    private ScoreRule coinRule;


    /**
     * 用户权益-任务完成汇总数据封装
     */
    @TableField(exist = false)
    private Map<String, Object> incentTaskedOut;

    @TableField(exist = false)
    private String toLinkUrl;

    @TableField(exist = false)
    private String levelLimit;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public Integer getCodeIdt() {
        return codeIdt;
    }

    public void setCodeIdt(Integer codeIdt) {
        this.codeIdt = codeIdt;
    }

    public Integer getIncomieFregType() {
        return incomieFregType;
    }

    public void setIncomieFregType(Integer incomieFregType) {
        this.incomieFregType = incomieFregType;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
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

    public String getIncomeFreqIntro() {
        return incomeFreqIntro;
    }

    public void setIncomeFreqIntro(String incomeFreqIntro) {
        this.incomeFreqIntro = incomeFreqIntro;
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


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPlsTaskId() {
        return plsTaskId;
    }

    public void setPlsTaskId(String plsTaskId) {
        this.plsTaskId = plsTaskId;
    }

    public ScoreRule getCoinRule() {
        return coinRule;
    }

    public void setCoinRule(ScoreRule coinRule) {
        this.coinRule = coinRule;
    }

    public Map<String, Object> getIncentTaskedOut() {
        return incentTaskedOut;
    }

    public void setIncentTaskedOut(Map<String, Object> incentTaskedOut) {
        this.incentTaskedOut = incentTaskedOut;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getToLinkUrl() {
        return toLinkUrl;
    }

    public void setToLinkUrl(String toLinkUrl) {
        this.toLinkUrl = toLinkUrl;
    }

    public String getLevelLimit() {
        return levelLimit;
    }

    public void setLevelLimit(String levelLimit) {
        this.levelLimit = levelLimit;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }
}
