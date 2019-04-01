package com.synchronous.application;

public class Fault {

	private String reason;
	private String errorCode;

	public Fault() {

	}

	public Fault(String reason, String errorCode) {
		super();
		this.reason = reason;
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "Fault [reason=" + reason + ", errorCode=" + errorCode + "]";
	}
}
