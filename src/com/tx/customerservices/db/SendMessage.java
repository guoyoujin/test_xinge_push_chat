package com.tx.customerservices.db;

import java.io.Serializable;

public class SendMessage implements Serializable{
	private String message;
	private String name;
	private int doctor_id;
	private int question_id;
	private int patient_id;
	private int type_message;
	private int kuaizheng_id;
	private int order_id;
	
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getKuaizheng_id() {
		return kuaizheng_id;
	}
	public void setKuaizheng_id(int kuaizheng_id) {
		this.kuaizheng_id = kuaizheng_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDoctor_id() {
		return doctor_id;
	}
	public void setDoctor_id(int doctor_id) {
		this.doctor_id = doctor_id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}
	public int getType_message() {
		return type_message;
	}
	public void setType_message(int type_message) {
		this.type_message = type_message;
	}
	@Override
	public String toString() {
		return "SendMessage [message=" + message + ", name=" + name
				+ ", doctor_id=" + doctor_id + ", question_id=" + question_id
				+ ", patient_id=" + patient_id + ", type_message="
				+ type_message + ", kuaizheng_id=" + kuaizheng_id
				+ ", order_id=" + order_id + "]";
	}
	public SendMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
