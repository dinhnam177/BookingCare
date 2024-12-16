package com.KMA.BookingCare.Mapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.KMA.BookingCare.Dto.InteractiveDto;
import com.KMA.BookingCare.Entity.InteractiveEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Repository.UserRepository;

public class InteractiveMapper {
	
	
	
	public static InteractiveDto convertToDto(InteractiveEntity entity, UserEntity userEntity) {
		InteractiveDto dto = new InteractiveDto();
		dto.setId(entity.getId());
		dto.setFullName(userEntity.getFullName());
		dto.setImg(userEntity.getImg());
		dto.setLastMessage(entity.getLastMessage());
		dto.setStatus(entity.getStatus());
		dto.setUserId(userEntity.getId());
		return dto;
	}

}
