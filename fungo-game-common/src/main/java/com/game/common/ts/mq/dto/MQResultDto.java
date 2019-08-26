package com.game.common.ts.mq.dto;

import lombok.Data;

/**
 * <p>提供给mq封装类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/17
 */
@Data
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
        CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL(1, "扣减用户经验值和等级"),

        /**
         * 添加用户动作行为数据
         * {
         *     "consumerQueue": "msgFungoTopicQueue_system_user",
         *     "filter": "",
         *     "limit": 10,
         *     "messageBody": "{"body":{"createdAt":1558073385076,"filter":"","limit":10,"memberId":"01d1c44a2eda4fd3a1fe82e81abab9cf","page":1,"pageNum":1,"pageSize":10,"sort":0,"state":0,"targetId":"01d1c44a2eda4fd3a1fe82e81abab9cf","targetType":1,"type":11,"updatedAt":1558073385076},"type":2}",
         *     "messageDataType": 3,
         *     "page": 1,
         *     "pageNum": 1,
         *     "pageSize": 10,
         *     "routingKey": "msgFungoTopicSystemUser.ACTION_ADD",
         *     "sort": 0
         * }
         */
        CMT_ACTION_MQ_TYPE_ACTION_ADD(2, "添加用户动作行为数据"),

        /**
         * 社区文章|心情用户执行任务:
         *
         * {
         *     "consumerQueue": "msgFungoTopicQueue_system_user",
         *     "filter": "",
         *     "limit": 10,
         *     "messageBody": "{"body":{"mbId":"01d1c44a2eda4fd3a1fe82e81abab9cf","requestId":"06f2f38999204b48a6b24429b8014455","taskGroupFlag":8485,"taskType":11,"typeCodeIdt":84870},"type":2}",
         *     "messageDataType": 3,
         *     "page": 1,
         *     "pageNum": 1,
         *     "pageSize": 10,
         *     "routingKey": "msgFungoTopicSystemUser.cmtPostMQDoTask",
         *     "sort": 0
         * }
         */
        CMT_POST_MOOD_MQ_TYPE_DO_TASK(3, "社区文章|心情用户执行任务"),


        /**
         * 社区文章|心情用户 消息通知:
         * {
         *     "consumerQueue": "msgFungoTopicQueue_system_user",
         *     "filter": "",
         *     "limit": 10,
         *     "messageBody": "{"body":{"appVersion":"2.4.8","replyToId":"","target_type":5,"target_id":"964a243b631045f984bc7561560e9179","information":"{\"user_avatar\":null,\"post_title\":\"这是带图的咯iOS9我是下午玉米\",\"post_content\":\"一样用肉在人做最空后哥哥\",\"post_id\":\"cc671601f1604bb99e59774e0804e916\",\"user_id\":\"f0a694da934c4c0e90aa821fa417342e\",\"user_name\":\"2019041851\",\"user_level\":2,\"comment_content\":\"不急\",\"type\":3}","eventType":2,"memberId":"004d12e674574cf4913ab27ca6ea697b"},"type":4}",
         *     "messageDataType": 3,
         *     "page": 1,
         *     "pageNum": 1,
         *     "pageSize": 10,
         *     "routingKey": "msgFungoTopicSystemUser.cmtPostMQDoTask",
         *     "sort": 0
         * }
         */
        CMT_POST_MOOD_MQ_TYPE_ADD_NOTICE(4,"社区文章|心情用户消息通知"),


        /**
         * 社区 添加 游戏评论消息通知
         */
        CMT_POST_MOOD_MQ_TYPE_GAME_EVALUATION_ADD(5,"社区 添加游戏评论消息通知"),


        /**
         * 社区  修改 游戏评论消息通知
         */
        CMT_POST_MOOD_MQ_TYPE_GAME_EVALUATION_UPDATE(6,"社区 修改游戏评论消息通知")


        ;


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


    public enum SystemMQDataType {
        //默认
        SYSTEM_DATA_TYPE_GAMEINSERT(1, "com.game.common.dto.GameDto"),
        SYSTEM_DATA_TYPE_GAMEUPDATE(2, "com.game.common.dto.GameDto"),
        SYSTEM_DATA_TYPE_GAMERELEASELOG(3, "com.game.common.dto.game.GameReleaseLogDto"),
        SYSTEM_DATA_TYPE_COUNTER(4, "java.util.HashMap"),
        SYSTEM_DATA_TYPE_COMMUNITYINSERT(5, "com.game.common.dto.community.CmmCommunityDto"),
        SYSTEM_MQ_DATA_TYPE_ADDGAMETAG(6,"java.util.HashMap"), // addGameTag方法
        SYSTEM_MQ_DATA_TYPE_COMMUNITY_UPDATE(7,"java.util.HashMap"), // 社区计数器
        SYSTEM_MQ_DATA_TYPE_GAMES_UPDATE(8,"java.util.HashMap"),
        SYSTEM_DATA_TYPE_GAMES_DOWNLOAD(9, "java.util.HashMap"),
        SYSTEM_DATA_TYPE_POST_UPDATE(10, "com.game.common.dto.community.CmmPostDto"),
        SYSTEM_DATA_TYPE_MOOD_UPDATE(11, "com.game.common.dto.community.MooMoodDto");
//        SYSTEM_DATA_TYPE_GAME_SURVEY_UPDATE(12, "java.util.ArrayList");

        private int code;
        private String name;

        SystemMQDataType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        //---------
    }


    public enum GameMQDataType {
        //默认
        GAME_DATA_TYPE_BASACTIONINSERT(11, "com.game.common.dto.action.BasActionDto"),
        GAME_DATA_TYPE_SELECTONEANDUPDATEALLCOLUMNBYID(12, "java.util.HashMap"),
        GAME_DATA_TYPE_BASNOTICEUPDATEBYID(13, "com.game.common.bean.advice.BasNoticeDto"),
        GAME_DATA_TYPE_BASNOTICEINSERT(14, "com.game.common.bean.advice.BasNoticeDto"),
        GAME_DATA_TYPE_PUSH(15, "java.util.HashMap"),
        GAME_DATA_TYPE_EXTASK(16, "com.game.common.dto.system.TaskDto");


        private int code;
        private String name;

        GameMQDataType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        //---------
    }

    /**
     * 功能描述: 管控台发送消息 3xx 开头
     * @auther: dl.zhang
     * @date: 2019/7/31 10:03
     */
    public enum AdminMQDataType {
        //默认
        ADMIN_DATA_TYPE_BASNOTICEUPDATEBYID(300, "java.util.HashMap");

        private int code;
        private String name;

        AdminMQDataType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        //---------
    }

    //--------
}

