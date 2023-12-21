package org.egx.news.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.egx.clients.io.UserBehaviorEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaUserBehaviorProducerConfig {
    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    public Map<String, Object> produceConfigs(){
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }
    @Bean
    public ProducerFactory<String, UserBehaviorEvent> producerUserBehaviorFactory(){
        return new DefaultKafkaProducerFactory<>(produceConfigs());
    }
    @Bean
    public KafkaTemplate<String, UserBehaviorEvent> kafkaUserBehaviorTemplate(){
        return new KafkaTemplate<>(producerUserBehaviorFactory());
    }

}
