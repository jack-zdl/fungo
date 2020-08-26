package com.fungo.system.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaReceiver {

//    @KafkaListener( topics = {"abc123"})
    @KafkaListeners(value = {
            @KafkaListener(topics = {"abc123"})
    })
    @SendTo("abc1234")
    public String listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();

            log.info("record =" + record);
            log.info("message =" + message);
        }
        return record.value()+"mashibing edu";
    }
}
