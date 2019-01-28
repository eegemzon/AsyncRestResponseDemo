package com.asynchronous.application;

public class Domain {
	private int id;
	private String data;
	private String desc;
	public Domain() {}
	public Domain(int id, String data, String desc) {
		this.id   = id;
		this.data = data;
		this.desc = desc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "Domain [id=" + id + ", data=" + data + ", desc=" + desc + "]";
	}
}