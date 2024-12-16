package com.KMA.BookingCare.Service;

import java.util.List;

import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Entity.MessageEntity;

public interface MessageService {
		MessageDto save(MessageDto dto);
		List<MessageDto> findBySenderOrReceiver(Long sender, Long receiver);
		List<MessageDto> findAllMessageBySelectUser(Long idSelect);
}
