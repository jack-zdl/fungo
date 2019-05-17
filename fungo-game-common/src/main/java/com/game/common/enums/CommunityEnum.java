package com.game.common.enums;


/**
 * 社区业务枚举
 *
 *
 * @author mxf
 * @since 2019-03-25
 */
public enum CommunityEnum {

    /**
     * 扣减用户经验值和等级,MQ消息数据：
     * {
     *     "consumerQueue": "msgFungoTopicQueue_community_POST",
     *     "filter": "",
     *     "limit": 10,
     *     "messageBody": "{"body":{"score":3,"mb_id":"00167ecb60374a439431563401285e58"},"type":1}",
     *     "messageDataType": 3,
     *     "page": 1,
     *     "pageNum": 1,
     *     "pageSize": 10,
     *     "sort": 0
     * }
     */
  CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL(1,"扣减用户经验值和等级");








    private int code;
    private String name;

    private CommunityEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
