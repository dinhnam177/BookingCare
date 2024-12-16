package com.KMA.BookingCare.Service;

import java.text.ParseException;
import java.util.List;

import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.WorkTimeEntity;

public interface WorkTimeService {
	List<WorkTimeDto> findAll();
	List<WorkTimeEntity> findByListId(List<Long> id);
	List<WorkTimeDto> findByDateAndDoctorId(String date, Long id) throws ParseException;

	WorkTimeDto findOneById(Long id);

	List<WorkTimeDto> findAllByDoctorId(Long id);
}
