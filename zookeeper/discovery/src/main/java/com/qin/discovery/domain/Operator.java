package com.qin.discovery.domain;

import java.io.Serializable;

public class Operator implements Serializable {
	
	private static final long serialVersionUID = -8058487976097187377L;
	
	private Integer operatorId;
	private String userName;
	private String fullName;

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
