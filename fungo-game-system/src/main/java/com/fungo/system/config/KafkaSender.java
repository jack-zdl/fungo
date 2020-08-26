package com.fungo.system.config;

import com.fungo.system.tools.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        kafkaTemplate.send("abc123", gson.toJson(message));
//        kafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback<String, String, Object>() {
//
//            @Override
//            public Object doInOperations(KafkaOperations<String, String> kafkaOperations) {
//                kafkaOperations.send(new ProducerRecord<String, String>("abc123", "001","this is kafka Template"));
//                return null;
//            }
//        });
//                .send(new ProducerRecord<String, String>("abc123", "001","this is kafka Template"));
    }
}
