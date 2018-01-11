package com.kaf.apachekafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kaf.apachekafka.storage.MessageEntity;

@Service
public class KafkaProducer {
	private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

	@Autowired
	@Qualifier("messageKafkaTemplate")
	private KafkaTemplate<String, MessageEntity> messageKafkaTemplate;

	@Value("${kafka.topic}")
	private String kafkaTopicName;

	public void sendMessageFromProducer(MessageEntity message) {
		log.info("sending data='{}'", message);
		messageKafkaTemplate.send(kafkaTopicName, message);
	}
}
