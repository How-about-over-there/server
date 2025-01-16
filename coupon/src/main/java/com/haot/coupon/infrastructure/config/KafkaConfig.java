package com.haot.coupon.infrastructure.config;

import com.haot.coupon.application.dto.UnlimitedCouponDto;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> errorProducerFactory() {
        Map<String, Object> configProducer = new HashMap<>();
        configProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProducer);
    }

    @Bean
    public KafkaTemplate<String, String> errorKafkaTemplate() {
        return new KafkaTemplate<>(errorProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> couponProducerFactory() {
        Map<String, Object> configCouponProducer = new HashMap<>();
        configCouponProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configCouponProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configCouponProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configCouponProducer);
    }

    @Bean
    public KafkaTemplate<String, Object> KafkaTemplate() {
        return new KafkaTemplate<>(couponProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        HashMap<String, Object> configConsumer = new HashMap<>();
        configConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configConsumer.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);  // 자동 커밋 비활성화
        configConsumer.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); // 한 번에 처리할 최대 메시지 개수 설정
        configConsumer.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); // Poll 간 최대 시간 설정

        return new DefaultKafkaConsumerFactory<>(configConsumer);
    }

    // 쿠폰 issue Consumer
    @Bean
    public ConsumerFactory<String, UnlimitedCouponDto> issueConsumerFactory() {
        HashMap<String, Object> configConsumer = new HashMap<>();
        configConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configConsumer.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);  // 자동 커밋 비활성화
        configConsumer.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); // 한 번에 처리할 최대 메시지 개수 설정
        configConsumer.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); // Poll 간 최대 시간 설정

        // JsonDeserializer에 대상 클래스 설정 -> 신뢰할 수 있는 패키지 설정
        configConsumer.put(JsonDeserializer.TRUSTED_PACKAGES, "com.haot.coupon.application.dto");

        return new DefaultKafkaConsumerFactory<>(configConsumer,
                new StringDeserializer(),
                new JsonDeserializer<>(UnlimitedCouponDto.class));
    }

    // 무제한 쿠폰 factory
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UnlimitedCouponDto>>
    parallelKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UnlimitedCouponDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(issueConsumerFactory());
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(3);

        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }


    @Bean
    public NewTopic unlimitedIssueCouponTopic() {
        return TopicBuilder.name("coupon-issue-unlimited")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
