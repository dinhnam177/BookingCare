package com.KMA.BookingCare.Service;

import java.text.ParseException;
import java.util.List;

import com.KMA.BookingCare.Dto.SearchFullTextDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.KMA.BookingCare.Api.form.SpecializedForm;
import com.KMA.BookingCare.Dto.SpecializedDto;

public interface SpecializedService {
	List<SpecializedDto> findAll();
	List<SpecializedDto> findAllByStatus(Integer status,Pageable pageable);
	Page<SpecializedDto> findAllByStatusApi(Integer status, Pageable pageable);
	void saveOrUpdateSpecialized(SpecializedForm form) throws ParseException;
	List<SpecializedDto> findRandomSpecicalized();

	List<SpecializedDto> getFeaturedSpecialty();

	SpecializedDto findOneById(Long id);

	List<SearchFullTextDto> searchAllByFullText(String query);

	void updateByStatusAndIds(List<String> ids, Integer status);

	boolean isExistItemRelationWithSpecialIsUsing(List<String> ids);

	void delete(List<String> ids);

	Integer getTotalByStatus(Integer status,Pageable pageable);
}
