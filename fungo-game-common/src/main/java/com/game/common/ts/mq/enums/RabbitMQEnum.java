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

        MQ_QUEUE_TOPIC_NAME_ORDER(1, "mStoreOrderTopicQueue"),

        MQ_QUEUE_DIRECT_NAME_ORDER(2, "mStoreOrderDirectQueue");

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
        QUEUE_ROUTE_KEY_ORDER(1, "mstoreOrder.#");

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
