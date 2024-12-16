package com.KMA.BookingCare.Entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

// class thể hiện tương tác hay thể hiện những người đã nahwsn tin tới hệ thống
@Entity
@Table(name = "interactive")
public class InteractiveEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date createdDate;
	
//	 @OneToOne(cascade = CascadeType.ALL)
//	    @JoinColumn(name = "user_id", referencedColumnName = "id")
//	private UserEntity user;
	 
//	 @ManyToOne
//		@JoinColumn(name = "user_id")
	 private Long userId;
	 
	 private String lastMessage;
	 
	 private Integer status;
	 
//	 @OneToOne(cascade = CascadeType.ALL)
////	    @JoinColumn(name = "you_id", referencedColumnName = "id")
//	 @ManyToOne
//		@JoinColumn(name = "you_id")
	private Long youId;
	 
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getYouId() {
		return youId;
	}

	public void setYouId(Long youId) {
		this.youId = youId;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


}
