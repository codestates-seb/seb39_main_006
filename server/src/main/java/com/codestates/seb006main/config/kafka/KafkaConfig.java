package com.codestates.seb006main.config.kafka;

import com.codestates.seb006main.chat.dto.ChatDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.consumer.group-id}")
    private String GROUP_ID;
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVER;

    //Sender Config
    @Bean
    public ProducerFactory<String, ChatDto.Message> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ChatDto.Message> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        return new HashMap<>() {{
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        }};
    }

    //Receiver config
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatDto.Message> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatDto.Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ChatDto.Message> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(ChatDto.Message.class));
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        return new HashMap<>() {{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        }};

    }


}
