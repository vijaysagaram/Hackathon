package com.kaf.apachekafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hackthon.analytics.RealTimeAnalytics;
import com.hackthon.analytics.TextAnalytics;
import com.kaf.apachekafka.storage.MessageEntity;
import com.kaf.apachekafka.storage.MessageRepository;
import com.kaf.apachekafka.storage.RealTimeAnalyticsEntity;
import com.kaf.apachekafka.storage.RealTimeAnalyticsRepository;

@Component
public class RealTimeAnalyticsKafkaConsumer {
	private static final Logger log = LoggerFactory.getLogger(RealTimeAnalyticsKafkaConsumer.class);
		
	@Autowired
	private RealTimeAnalyticsRepository realTimeAnalyticsRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private RealTimeAnalytics realTimeAnalytics;
	
	@Autowired
	private TextAnalytics textAnalytics;
	
	@KafkaListener(topics = "${kafka.topic}", containerFactory = "messageKafkaListenerContainerFactory", id = "message")
    public void messageListener(MessageEntity messageEntity) {
		messageRepository.saveAndFlush(messageEntity);
		log.info("Recieved message: " + messageEntity);
		RealTimeAnalyticsEntity realTimeAnalyticsEntity = null;
		if(messageEntity.getMessage()!=null && messageEntity.getMessage().contains("unsatisfied"))	{
		try {
			realTimeAnalyticsEntity = realTimeAnalytics.analyzeData(messageEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(realTimeAnalyticsEntity.getProbability()!=null) {
			realTimeAnalyticsRepository.saveAndFlush(realTimeAnalyticsEntity);
			messageEntity.setRealTimeOlap("REALTIME");
			messageRepository.saveAndFlush(messageEntity);
		}
		
		log.info("realTime data saved successfully to database");
		}else if("check-bounce".contains(messageEntity.getMessage())){
			
			log.info("Recieved message: " + messageEntity);
			try {
				realTimeAnalyticsEntity = textAnalytics.analyzeLogData(messageEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			realTimeAnalyticsRepository.saveAndFlush(realTimeAnalyticsEntity);	
			messageEntity.setRealTimeOlap("REALTIME");
			messageRepository.saveAndFlush(messageEntity);
			log.info("log data saved successfully to database");
		}
    }
	
}
