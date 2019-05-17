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

    public enum  SystemMQDataType{
        //默认
        SYSTEM_DATA_TYPE_GAMEINSERT(1, "com.game.common.dto.GameDto"),
        SYSTEM_DATA_TYPE_GAMEUPDATE(2, "com.game.common.dto.GameDto"),
        SYSTEM_DATA_TYPE_GAMERELEASELOG(3, "com.game.common.dto.game.GameReleaseLogDto"),
        SYSTEM_DATA_TYPE_counter(4, "   java.util.HashMap"),
        SYSTEM_DATA_TYPE_COMMUNITYINSERT(5, "com.game.common.dto.community.CmmCommunityDto");

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

    }
}
