package com.kaf.apachekafka.storage;

public class ViewEntity {

	@Override
	public String toString() {
		return "ViewEntity [customerId=" + customerId + ", source=" + source + ", message=" + message + ", emailId="
				+ emailId + ", mobileNumber=" + mobileNumber + ", analytics=" + analytics + ", destination="
				+ destination + "]";
	}

	private String customerId;
	private String source;
	private String message;
	private String emailId;
	private String mobileNumber;
	private String analytics;
	private String destination;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAnalytics() {
		return analytics;
	}

	public void setAnalytics(String analytics) {
		this.analytics = analytics;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
