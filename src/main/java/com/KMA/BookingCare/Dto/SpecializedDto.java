package com.KMA.BookingCare.Dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


public class SpecializedDto {
	
	private Long id;
	
	private String name;
	
	private String code;
	private String description;
	
	private String img;
	
	
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public SpecializedDto(Long id, String name, String code, String description, String img) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.img = img;
	}

	public SpecializedDto() {
	}
}
