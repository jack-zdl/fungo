package com.fungo.system.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaAppender extends ConsoleAppender<ILoggingEvent> {
//
//    private KafkaTemplate kafkaTemplate;
//
//    @Override
//    public void start() {
//        super.start();
//        Map<String, Object> props = new HashMap();
//        props.put("bootstrap.servers", "ip:端口号");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", StringSerializer.class);
//        props.put("value.serializer", StringSerializer.class);
//        kafkaTemplate = new KafkaTemplate(new DefaultKafkaProducerFactory(props));
//        kafkaTemplate.send("test", "连接到Kafka。。。。。。。");// 先连接一遍，如果去掉可能报   Failed to update metadata after 60000 ms
//    }
//
//
//    @Override
//    protected void append(ILoggingEvent eventObject) {
//        kafkaTemplate.send("test", eventObject.getFormattedMessage());
//    }
}
