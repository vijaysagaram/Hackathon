/***************************************************
 Copyright 2017 CG, All rights reserved.
 ***************************************************/

package com.hackthon.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

import com.kaf.apachekafka.storage.MessageEntity;
import com.kaf.apachekafka.storage.RealTimeAnalyticsEntity;

/**
* 
* @author Vijay Sagaram
*/
@Component
public class TextAnalytics {

	public RealTimeAnalyticsEntity analyzeLogData(MessageEntity messageEntity) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> selectedWordsmap = new HashMap<String, String>();
		RealTimeAnalyticsEntity realTimeAnalyticsEntity = null;
		File file = null;
		file = new File("resources/selected-words-single.json");
		String uniLateralWordsJson = readFile(file.getCanonicalPath());
		selectedWordsmap = mapper.readValue(uniLateralWordsJson, new TypeReference<Map<String, String>>() {
		});
			String review =messageEntity.getMessage().toLowerCase();
			StringTokenizer stringTokenizer = new StringTokenizer(review);
			String reviewWord = null;
			while (stringTokenizer.hasMoreTokens()) {
				reviewWord = stringTokenizer.nextToken();
				if (selectedWordsmap.get(reviewWord) != null) {
					realTimeAnalyticsEntity = new RealTimeAnalyticsEntity();
					realTimeAnalyticsEntity.setCustomerId(messageEntity.getCustomerId());
					realTimeAnalyticsEntity.setEmailId(messageEntity.getEmailId());
					realTimeAnalyticsEntity.setId(messageEntity.getId());
					realTimeAnalyticsEntity.setMessage(messageEntity.getMessage());
					realTimeAnalyticsEntity.setMobileNumber(messageEntity.getMobileNumber());
					realTimeAnalyticsEntity.setSource(messageEntity.getSource());
					realTimeAnalyticsEntity.setTimestamp(messageEntity.getTimestamp());
					realTimeAnalyticsEntity.setDestination("personalized offer");
					return realTimeAnalyticsEntity;
					// need to get user details and send to db / kafka producer
				}
			}
			return realTimeAnalyticsEntity;
		}

	private static String readFile(String fileName) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(new File(fileName));
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader in = new BufferedReader(isr);
		String[] article = null;
		String finalString = "";
		String line;
		while ((line = in.readLine()) != null) {
			Pattern p = Pattern.compile("");
			article = p.split(line);
			for (String string : article) {
				finalString = finalString + string;
			}
		}
		in.close();
		return finalString;
	}
}