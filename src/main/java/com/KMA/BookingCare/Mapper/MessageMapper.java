package com.KMA.BookingCare.Mapper;

import java.text.SimpleDateFormat;

import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Entity.MessageEntity;

public class MessageMapper {
	public static MessageDto convertToDto(MessageEntity entity,SimpleDateFormat sp) {
		MessageDto dto = new MessageDto();
		dto.setId(entity.getId());
		dto.setCreatedDate(sp.format(entity.getCreatedDate()));
		dto.setReceiverId(entity.getReceiverId());
		dto.setSenderId(entity.getSenderId());
		dto.setContent(entity.getContent());
		return dto;
	}

}
