package com.KMA.BookingCare.Mapper;

import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Entity.HospitalEntity;

public class HospitalMapper {
	
	public static HospitalDto convertToDto(HospitalEntity entity) {
		HospitalDto dto = new HospitalDto();
		dto.setId(entity.getId());
		dto.setLocation(entity.getLocation());
		dto.setName(entity.getName());
		dto.setId(entity.getId());
		dto.setImg(entity.getImg());
		dto.setDescription(entity.getDescription());
		dto.setLatitude(entity.getLatitude());
		dto.setLongitude(entity.getLongitude());
		return dto;
	}

}
