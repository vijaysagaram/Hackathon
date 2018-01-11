package com.kaf.apachekafka.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kaf.apachekafka.services.KafkaProducer;
import com.kaf.apachekafka.storage.MessageEntity;
import com.kaf.apachekafka.storage.MessageRepository;
import com.kaf.apachekafka.storage.ViewEntity;

@RestController
@RequestMapping(value = "/kafka")
public class WebRestController {

	@Autowired
	KafkaProducer producer;

	@Autowired
	MessageRepository messageRepository;

	@PostMapping(value = "/producer/email")
	public String emailProducer(@RequestBody MessageEntity message) {
		producer.sendMessageFromProducer(message);
		return "Produced Data Successfully";
	}

	@PostMapping(value = "/producer/sms")
	public String smsProducer(@RequestBody MessageEntity message) {
		producer.sendMessageFromProducer(message);
		return "Produced Data Successfully";
	}

	@PostMapping(value = "/producer/ivr")
	public String ivrProducer(@RequestBody MessageEntity message) {
		producer.sendMessageFromProducer(message);
		return "Produced Data Successfully";
	}

	@PostMapping(value = "/producer/logs")
	public String logsProducer(@RequestBody MessageEntity message) {
		producer.sendMessageFromProducer(message);
		return "Log data produced Successfully";
	}

	@GetMapping(value = "/analytics/results")
	public ModelAndView getAllResults() {
		ModelAndView mav = new ModelAndView("analytics");
		List<MessageEntity> results = messageRepository.findAll();
		List<ViewEntity> logEntities = new ArrayList<>();
		List<ViewEntity> emailEntities = new ArrayList<>();
		List<ViewEntity> smsEntities = new ArrayList<>();

		results.forEach(result -> {
			
			ViewEntity viewEntity = new ViewEntity();
			viewEntity.setCustomerId(result.getCustomerId());
			viewEntity.setSource(result.getSource());
			viewEntity.setMessage(result.getMessage());
			viewEntity.setMobileNumber(result.getMobileNumber());
			viewEntity.setEmailId(result.getEmailId());
			viewEntity.setAnalytics(null != result.getRealTimeOlap() && result.getRealTimeOlap().equalsIgnoreCase("NP")
					? "Offline Analytics" : "Real Time Analytics");
			if (null != result.getMessage()) {
				if ("check-bounce".equalsIgnoreCase(result.getMessage()))
					viewEntity.setDestination("Personalized Offer Creation Team");
				else if ("card-utilization".equalsIgnoreCase(result.getMessage()))
					viewEntity.setDestination("Rewards Team");
				else 
					viewEntity.setDestination("OutBound Call Team");
			}
			if (result.getSource().equalsIgnoreCase("email")) {
				emailEntities.add(viewEntity);
			} else if (result.getSource().equalsIgnoreCase("logs")) {
				logEntities.add(viewEntity);
			} else {
				smsEntities.add(viewEntity);
			}
			
		});
		mav.addObject("logEntities", logEntities);
		mav.addObject("emailEntities", emailEntities);
		mav.addObject("smsEntities", smsEntities);

		return mav;
	}
	
	@DeleteMapping(value = "/data/delete")
	public String delete() {
		messageRepository.deleteAll();
		return "Deleted Successfully";
	}
}
