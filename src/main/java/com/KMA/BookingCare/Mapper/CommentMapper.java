package com.KMA.BookingCare.Mapper;

import com.KMA.BookingCare.Dto.CommentDto;
import com.KMA.BookingCare.Entity.CommentEntity;

public class CommentMapper {
	
	public static CommentDto convertToDto(CommentEntity entity) {
		CommentDto dto = new CommentDto();
		dto.setId(entity.getId());
		dto.setContent(entity.getContent());
		dto.setIdHandbook(entity.getHandbook().getId());
		dto.setIdUser(entity.getUser().getId());
		dto.setUserName(entity.getUser().getFullName());
		dto.setFullName(entity.getUser().getFullName());
		dto.setImg(entity.getUser().getImg());
		return dto;
	}

}
