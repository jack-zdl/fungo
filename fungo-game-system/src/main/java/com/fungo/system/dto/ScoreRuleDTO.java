package com.fungo.system.dto;

import com.fungo.system.entity.ScoreRule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Map;

@Setter
@Getter
@ToString
public class ScoreRuleDTO  {


    private String id;

    /**
     * 所属任务类别id
     */
    private String groupId;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Integer isActive;

    /**
     * 标题
     */
    private String tip;

    /**
     * 单位内最大获取收益  完成多少个才可以算完成任务
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
     * 任务编码的int型值
     */
    private Integer codeIdt;

//    /**
//     * 收益频率类型
//
//     0 无 1 天 2 用户生命周期 3 无限制 4 数据 5 周 6 月7 季度 8 半年 9 年
//     */
//    private Integer incomieFregType;
    /**
     * 任务对应的功能接口URL
     */
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
    private Integer taskType;

    /**
     * 授权等级
     * 		-1 无等级限制
     */
    private Integer authLevel;

//    /**
//     * 扩展字段1
//     */
//    private String ext1;
//    /**
//     * 扩展字段2
//     */
//    private String ext2;
//    /**
//     * 扩展字段3
//     */
//    private String ext3;
//    /**
//     * 收益频率介绍
//     */
//    private String incomeFreqIntro;
//    /**
//     * 创建人ID
//     */
//    private String creatorId;
//    /**
//     * 创建人名称
//     */
//    private String creatorName;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 若是虚拟币任务，对应的经验值任务ID
     */
    private String plsTaskId;

    /**
     * 分值类规则对应的虚拟币规则
     */
    private ScoreRule coinRule;


    /**
     * 用户权益-任务完成汇总数据封装
     */
    private Map<String, Object> incentTaskedOut;

    private String toLinkUrl;

    private String levelLimit;

    private String toActionUrl;

}
