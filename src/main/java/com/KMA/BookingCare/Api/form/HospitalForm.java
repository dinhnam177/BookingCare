package com.KMA.BookingCare.Api.form;

import org.springframework.web.multipart.MultipartFile;

public class HospitalForm {

	private Long id;

	private MultipartFile img;

	private String location;

	private String description;

	private String name;

	private String imgOld;

	private Double longitude;

	private Double latitude;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getImgOld() {
		return imgOld;
	}

	public void setImgOld(String imgOld) {
		this.imgOld = imgOld;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MultipartFile getImg() {
		return img;
	}

	public void setImg(MultipartFile img) {
		this.img = img;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
