package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingForm {

	private Long id;
	private String nameScheduler;
	private String phoneScheduer;
	private String namePatient;
	private String sex;
	private String phonePatient;
	private String location;
	private String reason;
	private Long idDoctor;
	private Long idWorktime;
	private String date;
	private String yearOfBirth;
	private String type;
	private Long userId;

	private Integer statusPayment;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getYearOfBirth() {
		return yearOfBirth;
	}
	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getIdDoctor() {
		return idDoctor;
	}
	public void setIdDoctor(Long idDoctor) {
		this.idDoctor = idDoctor;
	}
	public Long getIdWorktime() {
		return idWorktime;
	}
	public void setIdWorktime(Long idWorktime) {
		this.idWorktime = idWorktime;
	}
	public String getNameScheduler() {
		return nameScheduler;
	}
	public void setNameScheduler(String nameScheduler) {
		this.nameScheduler = nameScheduler;
	}
	public String getPhoneScheduer() {
		return phoneScheduer;
	}
	public void setPhoneScheduer(String phoneScheduer) {
		this.phoneScheduer = phoneScheduer;
	}
	public String getNamePatient() {
		return namePatient;
	}
	public void setNamePatient(String namePatient) {
		this.namePatient = namePatient;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhonePatient() {
		return phonePatient;
	}
	public void setPhonePatient(String phonePatient) {
		this.phonePatient = phonePatient;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
