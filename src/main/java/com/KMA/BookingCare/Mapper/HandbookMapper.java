package com.KMA.BookingCare.Mapper;

import java.text.SimpleDateFormat;

import com.KMA.BookingCare.Dto.HandbookDto;
import com.KMA.BookingCare.Entity.HandbookEntity;

public class HandbookMapper {
	public static HandbookDto covertToDto(HandbookEntity entity) {
		HandbookDto dto = new HandbookDto();
		SimpleDateFormat datFormate = new SimpleDateFormat("dd/MM/yyyy");
		dto.setId(entity.getId());
		dto.setContent(entity.getContent());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedDate(datFormate.format(entity.getCreatedDate()));
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setModifiedDate(datFormate.format(entity.getCreatedDate()));
		dto.setModifiedBy(datFormate.format(entity.getCreatedDate()));
		dto.setSpecialized(entity.getSpecialized().getName());
		dto.setSpecializedId(entity.getSpecialized().getId());
		dto.setImg(entity.getImg());
		dto.setCreatedById(entity.getUser().getId());
		dto.setCreatedByName(entity.getUser().getFullName());

		return dto;
	}

}
