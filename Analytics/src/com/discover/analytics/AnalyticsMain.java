/***************************************************
 Copyright 2017 CG, All rights reserved.
 ***************************************************/

package com.discover.analytics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.discover.dbconn.DBConnection;
import com.discover.pojo.CustomerReviewDetails;

/**
 * Analyzes user feedback data, processes the data and save to database
 * 
 * @author Vijay Sagaram
 */
public class AnalyticsMain {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<CustomerReviewDetails> reviewsList = DBConnection.getCustomerReviewDetails();
		Map<String, String> biLateralWordsmap = new HashMap<String, String>();
		File file = null;
		file = new File("resources/words-double-prob.json");
		String biLateralWordsJson = readFile(file.getCanonicalPath());
		// converts JSON string to Map
		biLateralWordsmap = mapper.readValue(biLateralWordsJson, new TypeReference<Map<String, String>>() {
		});

		Map<String, String> uniLateralWordsmap = new HashMap<String, String>();
		file = new File("resources/words-single-prob.json");
		String uniLateralWordsJson = readFile(file.getCanonicalPath());
		uniLateralWordsmap = mapper.readValue(uniLateralWordsJson, new TypeReference<Map<String, String>>() {
		});

		double reviewFinalProb = 0;
		int reviewsWordCount = 0;
		String newWords = "{\n\"";
		for (CustomerReviewDetails cRDetails : reviewsList) {
			String review = cRDetails.getMessage().toLowerCase();
			review = review.replaceAll("[^a-zA-Z'\\s]", " ");
			reviewFinalProb = 0;
			reviewsWordCount = 0;
			String splWordsArr[] = searchForSpecialWords(review);
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
				} else if (string != null && !string.equals("")) {
					newWords = newWords + "\":\"\",\n\"" + string;
				}
			}
			float specialWordProbabilitySum = 0;
			Map<String, String> map = readSpecialWordsFile();
			if (splWordsArr != null) {
				for (String string : splWordsArr) {
					String prob = null;
					if ((prob = map.get(string)) != null) {
						float wordProb = Float.parseFloat(prob);
						specialWordProbabilitySum = specialWordProbabilitySum + wordProb;
						reviewsWordCount++;
					}
				}
			}
			if (reviewFinalProb < 0) {
				reviewFinalProb = reviewFinalProb + specialWordProbabilitySum;
			}
			reviewFinalProb = reviewFinalProb / reviewsWordCount;
			cRDetails.setProbability(reviewFinalProb);
			System.out.println(reviewFinalProb);
		} // end of reviewsList loop
		DBConnection.saveAlanyticData(reviewsList);
		saveNewWords(newWords + "\":\"\"\n}");
		System.out.println(start - System.currentTimeMillis());
	}

	/**
	 * save new words in each review to a file (to analyze further)
	 * 
	 * @param string
	 */
	private static void saveNewWords(String string) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = null;
			file = new File("resources/newWords.json");
			fw = new FileWriter(file.getCanonicalPath());
			bw = new BufferedWriter(fw);
			bw.write(string);
			bw.flush();
		} catch (Exception e) {
		}
	}

	/**
	 * special words are those which enhances the probability of review being
	 * +ve or -ve
	 * 
	 * @param review
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] searchForSpecialWords(String review) throws FileNotFoundException, IOException {
		Map<String, String> splWordsmap = readSpecialWordsFile();
		String cleanedReview[] = new String[10];
		int p = 0;
		for (String key : splWordsmap.keySet()) {
			if (review.contains(key)) {
				cleanedReview[p] = key;
				p++;
			}
		}
		return cleanedReview;
	}

	private static Map<String, String> readSpecialWordsFile()
			throws FileNotFoundException, IOException, JsonParseException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> splWordsmap = new HashMap<String, String>();
		File file = null;
		file = new File("resources/special-words-double-prob.json");
		String splWordsJson = readFile(file.getCanonicalPath());
		splWordsmap = mapper.readValue(splWordsJson, new TypeReference<Map<String, String>>() {
		});
		return splWordsmap;
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
		File file = null;
		file = new File("resources/stopWords.json");
		String stopWordsJson = readFile(file.getCanonicalPath());
		stopWordsmap = mapper.readValue(stopWordsJson, new TypeReference<Map<String, String>>() {
		});

		String reviewWordsArray[] = review.split(" ");
		String cleanedReview = "";
		String reviewWord;
		for (int i = 0; i < reviewWordsArray.length; i++) {
			reviewWord = reviewWordsArray[i];
			review.replaceAll(reviewWord, "");
			if (stopWordsmap.get(reviewWord) == null) {
				cleanedReview = cleanedReview + " " + reviewWord;
			}
		}
		return cleanedReview;
	}

	/**
	 * reads file(json) and convert to string format
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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