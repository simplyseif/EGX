package org.egx.notifications.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egx.clients.io.ScrapedNews;
import org.egx.clients.io.ScrapedStock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfig(){
        Map<String,Object> props= new HashMap<String,Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }
    // stock configuration
    @Bean
    public ConsumerFactory<String, ScrapedStock> stockConsumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, ScrapedStock>> stockKafkaListenerContainerFactory(ConsumerFactory<String,ScrapedStock> stockConsumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,ScrapedStock> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stockConsumerFactory);
        return factory;
    }
    //news configuration
    @Bean
    public ConsumerFactory<String, ScrapedNews> newsConsumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, ScrapedNews>> newsKafkaListenerContainerFactory(ConsumerFactory<String,ScrapedNews> newsConsumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,ScrapedNews> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(newsConsumerFactory);
        return factory;
    }
}
