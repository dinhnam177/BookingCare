package com.KMA.BookingCare.Service;

import java.util.List;
import java.util.Optional;

import com.KMA.BookingCare.Api.form.BookingForm;
import com.KMA.BookingCare.Api.form.ChangeTimeCloseForm;
import com.KMA.BookingCare.Api.form.UploadMedicalRecordsForm;
import com.KMA.BookingCare.Dto.MedicalExaminationScheduleDto;
import com.KMA.BookingCare.Entity.MedicalExaminationScheduleEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

public interface MedicalExaminationScheduleService {
	Long save(BookingForm form) throws JsonProcessingException;

	Long saveForMobile(BookingForm form) throws JsonProcessingException;
	List<MedicalExaminationScheduleDto> findAllByStatus(Integer status);

	List<MedicalExaminationScheduleDto> findAllByStatusPage(Integer status, Pageable pageable);
	Integer getToTalByStatusPage(Integer status, Pageable pageable);
	List<MedicalExaminationScheduleDto> findAllByDoctorIdAndStatus(Long doctorID,Integer Status);
	List<MedicalExaminationScheduleDto> findAllByDoctorIdAndStatusPage(Long doctorID, Integer Status, Pageable pageable);

	Integer getTotalByDoctorIdAndStatusPage(Long doctorID, Integer Status, Pageable pageable);
	void updateMedicalByStatus(Integer status,List<String> ids);
	List<MedicalExaminationScheduleDto> findAllByUserIdAndStatus(Long userId, Integer status);
	boolean changTimeClose(ChangeTimeCloseForm changeTimeCloseForm) throws JsonProcessingException;

	void handleSendMedicalRecords(UploadMedicalRecordsForm form);

	Optional<MedicalExaminationScheduleEntity> findOneById(Long id);

	void update(MedicalExaminationScheduleEntity entity);

	void update(BookingForm form) throws JsonProcessingException;

	void updateTime(BookingForm form) throws JsonProcessingException;

	Optional<MedicalExaminationScheduleEntity> findOneByIdAndUserId(Long medicalId, Long userId);

	void cancelMedical(Long medicalId);

	void completePayment(List<String> ids);

	boolean isMedicalCompletePayment(List<String> ids);

	boolean isAllMedicalCompletePayment(List<String> ids);

	boolean isValidTimeChangeTimeClose(ChangeTimeCloseForm changeTimeCloseForm);
	boolean isValidDateChangeTimeClose(ChangeTimeCloseForm changeTimeCloseForm);
}
