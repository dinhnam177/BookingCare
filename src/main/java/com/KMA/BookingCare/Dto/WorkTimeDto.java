package com.KMA.BookingCare.Dto;

import java.util.Set;

import lombok.Data;


public class WorkTimeDto {
	
private Long id;
	
	private String name;
	
	private String time;
	
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	
	
	
	
	

}
