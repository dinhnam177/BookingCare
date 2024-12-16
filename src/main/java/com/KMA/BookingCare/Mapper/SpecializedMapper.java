package com.KMA.BookingCare.Mapper;

import com.KMA.BookingCare.Dto.SpecializedDto;
import com.KMA.BookingCare.Entity.SpecializedEntity;
public class SpecializedMapper {
	public static SpecializedDto convertToDto(SpecializedEntity entity) {
		SpecializedDto dto = new SpecializedDto();
		dto.setId(entity.getId());
		dto.setCode(entity.getCode());
		dto.setDescription(entity.getDescription());
		dto.setName(entity.getName());
		dto.setImg(entity.getImg());
		return dto;
	}

}
