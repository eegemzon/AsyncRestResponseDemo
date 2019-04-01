package com.asynchronous.application;

public class ExternalResponse<T> {

	private boolean isDefaultValue;
	private T data;

	public ExternalResponse() {}
	
	public ExternalResponse(boolean isDefaultValue, T data) {
		this.isDefaultValue = isDefaultValue;
		this.data = data;
	}

	public boolean isDefaultValue() {
		return isDefaultValue;
	}

	public void setDefaultValue(boolean isDefaultValue) {
		this.isDefaultValue = isDefaultValue;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
