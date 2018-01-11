package com.kaf.apachekafka.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REALTIME_ANALYTICS")
public class RealTimeAnalyticsEntity{

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="CUST_ID")
	private String customerId;
	
	@Column(name="MOBILE_NO")
	private String mobileNumber;
	
	@Column(name="SOURCE")
	private String source;
	
	@Column(name="MESSAGE")
	private String message;
	
	@Column(name="TIMESTAMP")
	private String timestamp;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="PROBABILITY")
	private String probability;

	@Column(name="DESTINATION")
	private String destination;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "MessageEntity [customerId=" + customerId + ", source=" + source + ", message="
				+ message + ", timestamp=" + timestamp + ", emailId=" + emailId+ "]";
	}

	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
