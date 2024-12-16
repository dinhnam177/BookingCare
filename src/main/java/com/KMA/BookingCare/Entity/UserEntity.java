package com.KMA.BookingCare.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fullName")
	private String fullName;
	
	private String sex;
	
	@Column(name = "phoneNumber")
	private String phoneNumber;
	
	private String location;
	
	private String description;
	
	private String shortDescription;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String img;

	private String yearOfBirth;
	
	private Integer status;
	
	private String peerId;

	private Long examinationPrice;
	
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",
	joinColumns = @JoinColumn(name="user_id"),
	inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<RoleEntity> roles;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(name = "user_work_time",
	joinColumns = @JoinColumn(name="user_id"),
	inverseJoinColumns = @JoinColumn(name="work_time_id"))
	private Set<WorkTimeEntity> workTimeEntity;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "specializedId")
	private SpecializedEntity specialized;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hospitalId")
	private HospitalEntity hospital;
	
	@OneToMany(mappedBy = "user")
	private List<HandbookEntity> lstHandbook=new ArrayList<>();

	@OneToMany(mappedBy = "doctor")
	private List<MedicalExaminationScheduleEntity> medical=new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<MedicalExaminationScheduleEntity> medicalUser=new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<CommentEntity> comment =new ArrayList<>();

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<HolidayEntity> holidayEntities = new ArrayList<>();


}
