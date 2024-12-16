package com.KMA.BookingCare.Dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.KMA.BookingCare.Entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicalExaminationScheduleDto {

	private Long id;
	private String date;
	private Long workTimeID;
	private User doctor;
	private String nameScheduler;//
	private String phoneScheduer;//
	private String namePatient;//
	private String sex;//
	private String phonePatient;//
	private String location;
	private String reason;
	private String yearOfBirth;//
	private Integer status;
	private String wordTimeTime;
	private String type;
	private String hospitalName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdDate;

	private Integer statusPayment;

	private PaymentDto payment;

	private Long examinationPrice;

	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWordTimeTime() {
		return wordTimeTime;
	}
	public void setWordTimeTime(String wordTimeTime) {
		this.wordTimeTime = wordTimeTime;
	}
	public Long getId() {
		
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getWorkTimeID() {
		return workTimeID;
	}
	public void setWorkTimeID(Long workTimeID) {
		this.workTimeID = workTimeID;
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
	public User getDoctor() {
		return doctor;
	}
	public void setDoctor(User doctor) {
		this.doctor = doctor;
	}
	
}
