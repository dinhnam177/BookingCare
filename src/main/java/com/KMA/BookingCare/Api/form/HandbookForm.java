package com.KMA.BookingCare.Api.form;

import org.springframework.web.multipart.MultipartFile;

public class HandbookForm {
	private Long id;
	private String title;
	private Long specializedId;
	private String description;
	private String content;
	private String createdDate;
	private String createdBy;
	private MultipartFile img;
	
	
	public MultipartFile getImg() {
		return img;
	}
	public void setImg(MultipartFile img) {
		this.img = img;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getSpecializedId() {
		return specializedId;
	}
	public void setSpecializedId(Long specializedId) {
		this.specializedId = specializedId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
