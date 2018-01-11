package com.kaf.apachekafka.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RAW_EVENT")
public class MessageEntity {
	

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="CUST_ID")
	private String customerId;
	
	@Column(name="SOURCE")
	private String source;
	
	@Column(name="MESSAGE")
	private String message;
	
	@Column(name="TIMESTAMP")
	private String timestamp;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="MOBILE_NO")
	private String mobileNumber;
	
	@Column(name="REALTIME_OLAP")
	private String realTimeOlap = "NP";
	
	@Column(name="FIELD9")
	private String field9;
	
	@Column(name="FIELD10")
	private String field10;
	
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "MessageEntity [customerId=" + customerId + ",source=" + source + ", message="
				+ message + ", timestamp=" + timestamp + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", field9=" + field9 + ", field10=" + field10 + "]";
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}

	public String getField10() {
		return field10;
	}

	public void setField10(String field10) {
		this.field10 = field10;
	}

	public String getRealTimeOlap() {
		return realTimeOlap;
	}

	public void setRealTimeOlap(String realTimeOlap) {
		this.realTimeOlap = realTimeOlap;
	}

}
