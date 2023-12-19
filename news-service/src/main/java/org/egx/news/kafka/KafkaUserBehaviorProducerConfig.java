package org.egx.news.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaUserBehaviorProducerConfig {
    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    public Map<String, Object> produceConfigs(){
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
    @Bean
    public ProducerFactory<String, String> producerUserBehaviorFactory(){
        return new DefaultKafkaProducerFactory<>(produceConfigs());
    }
    @Bean
    public KafkaTemplate<String, String> kafkaUserBehaviorTemplate(){
        return new KafkaTemplate<>(producerUserBehaviorFactory());
    }

}
