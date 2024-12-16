package com.KMA.BookingCare.Dto;

import java.util.Date;


public class MessageDto {


	private Long id;
	private String content;
	private String createdDate;
    private Long senderId;
    private Long receiverId;
    private String Type;
    


	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	




	
	
	
}
