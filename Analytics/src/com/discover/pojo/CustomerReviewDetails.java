/***************************************************
 Copyright 2017 CG, All rights reserved.
 ***************************************************/

package com.discover.pojo;

/**
 * Class for customer and feedback details
 *
 * @author Vijay Sagaram
 * 
 */
public class CustomerReviewDetails {

	private String custId;
	private String source;
	private String message;
	private double probability;

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CustomerReviewDetails [custId=" + custId + ", source=" + source + ", message=" + message + "]";
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}