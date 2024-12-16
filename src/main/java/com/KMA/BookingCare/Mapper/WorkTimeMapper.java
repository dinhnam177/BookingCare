package com.KMA.BookingCare.Mapper;

import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.WorkTimeEntity;

public class WorkTimeMapper {
	public static WorkTimeDto convertToDto(WorkTimeEntity entity) {
		WorkTimeDto dto = new WorkTimeDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setTime(entity.getTime());
		return dto;
	}

}
