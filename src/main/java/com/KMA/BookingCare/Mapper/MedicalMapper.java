package com.KMA.BookingCare.Mapper;

import java.util.Set;

import com.KMA.BookingCare.Dto.MedicalExaminationScheduleDto;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Entity.MedicalExaminationScheduleEntity;
import com.KMA.BookingCare.Entity.WorkTimeEntity;

public class MedicalMapper {
	public static MedicalExaminationScheduleDto convertToDto(MedicalExaminationScheduleEntity entity) {
		MedicalExaminationScheduleDto dto = new MedicalExaminationScheduleDto();
		dto.setId(entity.getId());
		dto.setDate(entity.getDate());
		dto.setLocation(entity.getLocation());
		dto.setNamePatient(entity.getNamePatient());
		dto.setPhonePatient(entity.getPhonePatient());
		if(entity.getNameScheduler()!=null&&!entity.getNameScheduler().equals("")) {
			dto.setNameScheduler(entity.getNameScheduler());
			dto.setPhoneScheduer(entity.getPhoneScheduer());
		}
		dto.setType(entity.getType());
		dto.setSex(entity.getSex());
		dto.setReason(entity.getReason());
		dto.setStatus(entity.getStatus());
		dto.setWorkTimeID(entity.getWorkTimeID());
		dto.setYearOfBirth(entity.getYearOfBirth());
		User userDto = UserMapper.convertToDto(entity.getDoctor());
		dto.setDoctor(userDto);
		Set<WorkTimeEntity> lstWkEntity= entity.getDoctor().getWorkTimeEntity();
		for(WorkTimeEntity wkEntity:lstWkEntity) {
			if(wkEntity.getId()==entity.getWorkTimeID()) {
				dto.setWordTimeTime(wkEntity.getTime());
			}
		}
		if (entity.getPayment() != null) {
			dto.setPayment(PaymentMapper.convertToDTO(entity.getPayment()));
		}
		dto.setStatusPayment(entity.getStatusPayment());
		dto.setHospitalName(entity.getHospitalName());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setExaminationPrice(entity.getExaminationPrice());

		return dto;
	}

}
