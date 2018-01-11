package com.kaf.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.kaf.apachekafka.storage.MessageEntity;
import com.kaf.apachekafka.storage.RealTimeAnalyticsEntity;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	public ConsumerFactory<String, MessageEntity> messageConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "message");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<>(MessageEntity.class));
	}

	@Bean(name = "messageKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, MessageEntity> messageKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MessageEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(messageConsumerFactory());
		return factory;
	}

	public ConsumerFactory<String, RealTimeAnalyticsEntity> realTimeMessageConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "realTimeAnalytics");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<>(RealTimeAnalyticsEntity.class));
	}

	@Bean(name = "realTimeMessageKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, RealTimeAnalyticsEntity> realTimeMessageKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RealTimeAnalyticsEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(realTimeMessageConsumerFactory());
		return factory;
	}
	
}
