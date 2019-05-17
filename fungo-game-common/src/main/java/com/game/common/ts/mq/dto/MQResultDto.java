package com.game.common.ts.mq.dto;

/**
 * <p>提供给mq封装类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/17
 */
public class MQResultDto {

    private int type;

    private Object body;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }


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
        CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL(1, "扣减用户经验值和等级");


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

    //---------
}