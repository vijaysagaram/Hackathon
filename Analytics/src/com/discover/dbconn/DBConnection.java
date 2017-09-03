/***************************************************
 Copyright 2017 CG, All rights reserved.
 ***************************************************/

package com.discover.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.discover.pojo.CustomerReviewDetails;

/**
 * Creates Connection to database and saves raw and analyzed data
 * 
 * @author Vijay Sagaram
 *
 */
public class DBConnection {

	public static ArrayList<CustomerReviewDetails> getCustomerReviewDetails() {
		ArrayList<CustomerReviewDetails> list = new ArrayList<>();
		Connection connection = getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery("select * from CUSTOMERREVIEW");
			CustomerReviewDetails cRDetails = null;
			while (resultSet.next()) {
				cRDetails = new CustomerReviewDetails();
				cRDetails.setCustId(String.valueOf(resultSet.getLong(1)));
				cRDetails.setSource(resultSet.getString(2));
				cRDetails.setMessage(resultSet.getString(3));
				list.add(cRDetails);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return list;
	}

	public static void saveAlanyticData(ArrayList<CustomerReviewDetails> reviewsList){
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("update CUSTOMER_ANALYTICS_2 set Probability=? where custid=?");
			for (CustomerReviewDetails cRDetails : reviewsList) {
				double probability = cRDetails.getProbability();
				String custId = cRDetails.getCustId();
				preparedStatement.setDouble(1,probability);
				preparedStatement.setString(2,custId);
				preparedStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void saveAlanytic1Data(ArrayList<CustomerReviewDetails> reviewsList){
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("update CUSTOMER_ANALYTICS_1 set Probability=? where custid=?");
			for (CustomerReviewDetails cRDetails : reviewsList) {
				double probability = cRDetails.getProbability();
				String custId = cRDetails.getCustId();
				preparedStatement.setDouble(1,probability);
				preparedStatement.setString(2,custId);
				preparedStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	private static Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1523:orcl", "SYS as SYSDBA",
					"svijayk");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

}