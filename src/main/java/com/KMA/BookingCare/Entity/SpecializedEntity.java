package com.KMA.BookingCare.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "specialized")
public class SpecializedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String code;
	
	private String description;
	
	private String img;
	
	private Integer status;
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@OneToMany(mappedBy = "specialized")
	private List<UserEntity> lstuser=new ArrayList<UserEntity>();
	
	@OneToMany(mappedBy = "specialized")
	private List<HandbookEntity> lstHandbook=new ArrayList<HandbookEntity>();
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public List<HandbookEntity> getLstHandbook() {
		return lstHandbook;
	}
	public void setLstHandbook(List<HandbookEntity> lstHandbook) {
		this.lstHandbook = lstHandbook;
	}
	public List<UserEntity> getLstuser() {
		return lstuser;
	}
	public void setLstuser(List<UserEntity> lstuser) {
		this.lstuser = lstuser;
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
	
	
}
