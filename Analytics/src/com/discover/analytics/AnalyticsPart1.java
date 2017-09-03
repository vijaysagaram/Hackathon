/***************************************************
 Copyright 2017 CG, All rights reserved.
 ***************************************************/

package com.discover.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.discover.dbconn.DBConnection;
import com.discover.pojo.CustomerReviewDetails;

/**
* 
* @author Vijay Sagaram
*/
public class AnalyticsPart1 {

	public static void main(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<CustomerReviewDetails> reviewsList = DBConnection.getCustomerReviewDetails();
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
		for (CustomerReviewDetails cRDetails : reviewsList) {
			String review = cRDetails.getMessage().toLowerCase();
			String reviewCopy = review;
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
			System.out.println(reviewCopy + "\n and its final probability is: " + reviewFinalProb);
			cRDetails.setProbability(reviewFinalProb);
		} // end of reviewsList loop
		DBConnection.saveAlanytic1Data(reviewsList);
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
		String reviewWord;
		for (int i = 0; i < reviewWordsArray.length; i++) {
			reviewWord = reviewWordsArray[i];
			int len = reviewWord.length();
			String lastChar = reviewWord.substring(len - 1);
			if (lastChar.matches("[,.:!]"))
				reviewWord = reviewWord.substring(0, len - 1);

			String wordArr[] = reviewWord.split("\\.");
			boolean checked = false;
			boolean hasStopWords = false;
			if (wordArr.length > 1) {
				for (String str : wordArr) {
					if (stopWordsmap.get(str) == null) {
						cleanedReview = cleanedReview + " " + str;
						checked = true;
					} else {
						hasStopWords = true;
					}
				}
			}
			review.replaceAll(reviewWord, "");
			if (stopWordsmap.get(reviewWord) == null && !checked && !hasStopWords) {
				cleanedReview = cleanedReview + " " + reviewWord;
			}
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
