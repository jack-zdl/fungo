package com.game.common.ts.mq.enums;


/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:RabbitMQ 交换机和队列名称定义<br />
 * @Date: Create in 2018/5/8
 */
public class RabbitMQEnum {

    public enum Exchange {
        EXCHANGE_FANOUT(1, "msgFungo_Fanout"),
        EXCHANGE_TOPIC(2, "msgFungo_Topic"),
        EXCHANGE_DIRECT(3, "msgFungo_Direct");

        private int code;
        private String name;

        Exchange(int code, String name) {
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


    public enum MQQueueName {

        MQ_QUEUE_TOPIC_NAME_DEFAULT(1, "msgFungoTopicQueue"),

        MQ_QUEUE_DIRECT_NAME_DEFAULT(2, "msgFungoDirectQueue"),

        //3 社区-文章队列
        MQ_QUEUE_TOPIC_NAME_COMMUNITY_POST(3, "msgFungoTopicQueue_community_POST"),

        //4 社区-心情队列
        MQ_QUEUE_TOPIC_NAME_COMMUNITY_MOOD(4, "msgFungoTopicQueue_community_MOOD");

        private int code;
        private String name;

        MQQueueName(int code, String name) {
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


    public enum QueueRouteKey {
        QUEUE_ROUTE_KEY_DEFAULT(1, "msgFungo.#");

        private int code;
        private String name;

        QueueRouteKey(int code, String name) {
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
