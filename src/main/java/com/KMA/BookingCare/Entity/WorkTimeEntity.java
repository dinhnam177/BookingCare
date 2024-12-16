package com.KMA.BookingCare.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "workTime")
@Getter
@Setter
public class WorkTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "time")
	private String time;
	
	@ManyToMany(mappedBy = "workTimeEntity")
	private Set<UserEntity> userEntities;

	@ManyToMany(mappedBy = "workTimes")
	private Set<HolidayEntity> holidays;
}
