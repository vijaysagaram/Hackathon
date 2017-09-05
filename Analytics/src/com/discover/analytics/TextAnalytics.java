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
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.discover.dbconn.DBConnection;
import com.discover.pojo.CustomerReviewDetails;

/**
* 
* @author Vijay Sagaram
*/
public class TextAnalytics {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<CustomerReviewDetails> reviewsList = DBConnection.getCustomerReviewDetails();
		Map<String, String> uniLateralWordsmap = new HashMap<String, String>();
		File file = null;
		file = new File("resources/selected-words-single.json");
		String uniLateralWordsJson = readFile(file.getCanonicalPath());
		uniLateralWordsmap = mapper.readValue(uniLateralWordsJson, new TypeReference<Map<String, String>>() {
		});
		for (CustomerReviewDetails cRDetails : reviewsList) {
			String review = cRDetails.getMessage().toLowerCase();
			StringTokenizer stringTokenizer = new StringTokenizer(review);
			String reviewWord = null;
			while (stringTokenizer.hasMoreTokens()) {
				reviewWord = stringTokenizer.nextToken();
				if (uniLateralWordsmap.get(reviewWord) != null) {
					System.out.println("found");
					// need to get user details and send to db / kafka producer
				}
			}
		}
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