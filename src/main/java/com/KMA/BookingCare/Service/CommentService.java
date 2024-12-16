package com.KMA.BookingCare.Service;

import java.util.List;

import com.KMA.BookingCare.Dto.CommentDto;
import com.KMA.BookingCare.Entity.CommentEntity;

public interface CommentService {
	CommentDto save(CommentDto dto);
	List<CommentDto> findAllByHandbookId(Long id);
	void delete(Long id);
	Boolean existsByHandbookId(Long id);
}
