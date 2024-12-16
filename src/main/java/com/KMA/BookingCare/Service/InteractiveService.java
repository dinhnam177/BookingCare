package com.KMA.BookingCare.Service;

import java.util.List;

import com.KMA.BookingCare.Dto.InteractiveDto;
import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Dto.MyUser;

public interface InteractiveService {

	void saveOrUpdate(MessageDto chatMessage);

	List<InteractiveDto> findAll(MyUser userDetails);

}
