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
import java.util.regex.Pattern;

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
public class RealTimeAnalytics {

	public RealTimeAnalyticsEntity analyzeData(MessageEntity messageEntity) throws Exception {
		RealTimeAnalyticsEntity realTimeAnalyticsEntity = new RealTimeAnalyticsEntity();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> biLateralWordsmap = new HashMap<String, String>();
		File file = null;
		file = new File("resources/words-double-prob-al1.json");
		String biLateralWordsJson = readFile(file.getCanonicalPath());
		// converts JSON string to Map
		biLateralWordsmap = mapper.readValue(biLateralWordsJson, new TypeReference<Map<String, String>>() {
		});
		
		Map<String, String> uniLateralWordsmap = new HashMap<String, String>();
		file = new File("resources/words-single-prob-al1.json");
		String uniLateralWordsJson = readFile(file.getCanonicalPath());
		uniLateralWordsmap = mapper.readValue(uniLateralWordsJson, new TypeReference<Map<String, String>>() {
		});

		double reviewFinalProb = 0;
		int reviewsWordCount = 0;
		//for (CustomerReviewDetails cRDetails : reviewsList) {
			String review = messageEntity.getMessage().toLowerCase();
			review = review.replaceAll("[^a-zA-Z'\\s]", " ");
			reviewFinalProb = 0;
			reviewsWordCount = 0;
			String cleanedReview = removeStopWords(review);
			String[] stringArray = cleanedReview.split(" ");
			String[] outputArray = new String[stringArray.length - 1];
			for (int i = 0; i < stringArray.length - 1; i++) {
				outputArray[i] = stringArray[i] + " " + stringArray[i + 1];
			}
			for (String string : outputArray) {
				String prob = null;
				if ((prob = biLateralWordsmap.get(string)) != null) {
					cleanedReview = cleanedReview.replaceAll(string, "");
					double wordProb = Double.parseDouble(prob);
					reviewFinalProb = reviewFinalProb + wordProb;
					reviewsWordCount++;
				}
			}
			outputArray = cleanedReview.split(" ");
			for (String string : outputArray) {
				String prob = null;
				if ((prob = uniLateralWordsmap.get(string)) != null) {
					double wordProb = Double.parseDouble(prob);
					reviewFinalProb = reviewFinalProb + wordProb;
					reviewsWordCount++;
				}
			}
			if(reviewsWordCount!=0)
				reviewFinalProb = reviewFinalProb / reviewsWordCount;
			
			realTimeAnalyticsEntity.setCustomerId(messageEntity.getCustomerId());
			realTimeAnalyticsEntity.setEmailId(messageEntity.getEmailId());
			realTimeAnalyticsEntity.setId(messageEntity.getId());
			realTimeAnalyticsEntity.setMessage(messageEntity.getMessage());
			realTimeAnalyticsEntity.setMobileNumber(messageEntity.getMobileNumber());
			realTimeAnalyticsEntity.setSource(messageEntity.getSource());
			realTimeAnalyticsEntity.setTimestamp(messageEntity.getTimestamp());
			realTimeAnalyticsEntity.setDestination("out bound call");
			realTimeAnalyticsEntity.setProbability(String.valueOf(reviewFinalProb));
			return realTimeAnalyticsEntity;
		//} // end of reviewsList loop
	}

	/**
	 * removes stopwords from review
	 * 
	 * @param review
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String removeStopWords(String review) throws FileNotFoundException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> stopWordsmap = new HashMap<String, String>();
		File file = new File("resources/stopWords.json");
		String stopWordsJson = readFile(file.getCanonicalPath());
		stopWordsmap = mapper.readValue(stopWordsJson, new TypeReference<Map<String, String>>() {
		});

		String reviewWordsArray[] = review.split(" ");
		String cleanedReview = "";
		String reviewWord = null;
		for (int i = 0; i < reviewWordsArray.length; i++) {
			reviewWord = reviewWordsArray[i];
			}
			//review.replaceAll(reviewWord, "");
			if (stopWordsmap.get(reviewWord) == null) {
				cleanedReview = cleanedReview + " " + reviewWord;
			}
		return cleanedReview;
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
