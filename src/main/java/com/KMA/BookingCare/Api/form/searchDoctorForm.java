package com.KMA.BookingCare.Api.form;

public class searchDoctorForm {
	private String name;
	private Long specializedId;
	private Long hospitalId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSpecializedId() {
		return specializedId;
	}
	public void setSpecializedId(Long specializedId) {
		this.specializedId = specializedId;
	}
	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	

}
