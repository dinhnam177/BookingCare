package com.KMA.BookingCare.Dto;

import java.sql.Timestamp;
import java.util.Date;

public class HandbookDto {
	
	
	private Long id;
	
	private String title;
	
	private String description;
	
	private String content;
	
	private String createdBy;
	
	private String createdDate;
	
	private String modifiedDate;
	
	private String modifiedBy;
	
	private String specialized;
	
	private Long specializedId;
	
	private String img;
	
	private String createdByName;
	
	private Long createdById;
	
	
	
	
	
	
	
	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Long getSpecializedId() {
		return specializedId;
	}

	public void setSpecializedId(Long specializedId) {
		this.specializedId = specializedId;
	}

	public String getSpecialized() {
		return specialized;
	}

	public void setSpecialized(String specialized) {
		this.specialized = specialized;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public HandbookDto(Long id, String title, String description, String content,
					   String createdBy,
					   String createdDate,
					   String modifiedDate,
					   String modifiedBy,
					   String specialized,
					   Long specializedId,
					   String img,
					   String createdByName,
					   Long createdById) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.specialized = specialized;
		this.specializedId = specializedId;
		this.img = img;
		this.createdByName = createdByName;
		this.createdById = createdById;
	}

	public HandbookDto(Long id, String title, String description, String content,
					   String createdBy,
					   Date createdDate,
					   Date modifiedDate,
					   String modifiedBy,
					   String specialized,
					   Long specializedId,
					   String img,
					   String createdByName,
					   Long createdById) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.createdBy = createdBy;
		this.createdDate = createdDate.toString();
		this.modifiedDate = modifiedDate.toString();
		this.modifiedBy = modifiedBy;
		this.specialized = specialized;
		this.specializedId = specializedId;
		this.img = img;
		this.createdByName = createdByName;
		this.createdById = createdById;
	}

	public HandbookDto() {
	}

	public HandbookDto(Long id, String title, String img) {
		this.id = id;
		this.title = title;
		this.img = img;
	}

	public HandbookDto(Long id, String title, String description, String img) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.img = img;
	}
}
