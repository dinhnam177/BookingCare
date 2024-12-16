package com.KMA.BookingCare.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Dto.CommentDto;
import com.KMA.BookingCare.Entity.CommentEntity;
import com.KMA.BookingCare.Entity.HandbookEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.CommentMapper;
import com.KMA.BookingCare.Repository.CommentRepository;
import com.KMA.BookingCare.Repository.HandbookRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.CommentService;
@Service
public class CommentServiceImpl implements CommentService{

	private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
	
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HandbookRepository  handbookRepository;

	@Override
	public CommentDto save(CommentDto dto) {
		CommentEntity entity = new CommentEntity();
		entity.setContent(dto.getContent());
		UserEntity userEntity = userRepository.findOneById(dto.getIdUser());
		HandbookEntity handbookEntity = handbookRepository.findOneById(dto.getIdHandbook());
		entity.setUser(userEntity);
		entity.setHandbook(handbookEntity);
		entity= commentRepository.save(entity);
		dto= CommentMapper.convertToDto(entity);
		return dto;
	}

	@Override
	public List<CommentDto> findAllByHandbookId(Long id) {
		List<CommentEntity> lstEntities = commentRepository.findAllByHandbookId(id);
		List<CommentDto> lstDto = new ArrayList<CommentDto>();
		for(CommentEntity entity : lstEntities) {
			CommentDto dto = CommentMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public void delete(Long id) {
		commentRepository.deleteById(id);
		
	}

	@Override
	public Boolean existsByHandbookId(Long id) {
		log.debug("Request to check existsByHandbookId {}",id);
		return commentRepository.existsByHandbookId(id);
	}

}
