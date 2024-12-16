package com.KMA.BookingCare.Api.form;

public class notificationForm {
	private Long doctorId;
	private Long  userId;
	private String wkTime;
	private String date;
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getWkTime() {
		return wkTime;
	}
	public void setWkTime(String wkTime) {
		this.wkTime = wkTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
