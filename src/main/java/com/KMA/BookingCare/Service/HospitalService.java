package com.KMA.BookingCare.Service;

import java.text.ParseException;
import java.util.List;

import com.KMA.BookingCare.Dto.SearchFullTextDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Api.form.HandbookForm;
import com.KMA.BookingCare.Api.form.HospitalForm;
import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Entity.HospitalEntity;

public interface HospitalService {
	List<HospitalDto> findAll();
	void saveHospital(HospitalForm form) throws ParseException;
	Page<HospitalDto> findAllByStatusApi(Integer status, Pageable pageable);
	List<HospitalDto> findAllByStatus(Integer status);
	List<HospitalDto> findAllByStatus(Integer status, Pageable pageable);

	Integer getTotalByStatus(Integer status, Pageable pageable);
	List<HospitalDto>  findRandomSpecicalized();
	HospitalEntity findOneById(Long id);

	List<HospitalDto> getFeaturedHospital();

	List<SearchFullTextDto> searchAllByFullText(String query);

	Page<HospitalDto> searchByNameAndStatus(String query, Pageable pageable);

	boolean isExistItemRelationWithSpecialIsUsing(List<String> ids);

	void updateByStatusAndIds(List<String> ids, Integer status);

	void deleteHospitals(List<String> ids);

	List<HospitalDto> getNearbyHospital(Double lat, Double lng);
}
