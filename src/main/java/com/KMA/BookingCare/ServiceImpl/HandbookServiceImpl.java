package com.KMA.BookingCare.ServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Repository.CommentRepository;
import com.KMA.BookingCare.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Api.form.HandbookForm;
import com.KMA.BookingCare.Api.form.searchHandbookForm;
import com.KMA.BookingCare.Entity.HandbookEntity;
import com.KMA.BookingCare.Entity.SpecializedEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.HandbookMapper;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Repository.HandbookRepository;
import com.KMA.BookingCare.Repository.SpecializedRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.HandbookService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class HandbookServiceImpl implements HandbookService{
	
	@Autowired
	private HandbookRepository handbookRepository;
	@Autowired
	private SpecializedRepository specializedRepository;
	
	@Autowired
	private UserRepository UserRepository;
	
	@Autowired
	private Cloudinary cloudinary;

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void saveHandbook(HandbookForm form) throws ParseException {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		MyUser userDetails = UserMapper.convertToMyUser(user);
		HandbookEntity entity= new HandbookEntity();
		if(form.getId()!=null) {
			SimpleDateFormat datFormate = new SimpleDateFormat("dd/MM/yyyy");
			entity.setId(form.getId());
			entity.setCreatedDate(datFormate.parse(form.getCreatedDate()));
			entity.setCreatedBy(form.getCreatedBy());
		}else {
			entity.setCreatedBy(userDetails.getUsername());
			entity.setCreatedDate(new Date());
		}
		try {
			Map result= cloudinary.uploader().upload(form.getImg().getBytes(),
					ObjectUtils.asMap("resource_type","auto"));
			String urlImg=(String) result.get("secure_url");
			entity.setImg(urlImg);
		} catch (Exception e) {
			System.out.println("upload img fail");
		}
		UserEntity userEntity = UserRepository.findOneById(userDetails.getId());
		entity.setUser(userEntity);
		entity.setContent(form.getContent());
		entity.setDescription(form.getDescription());
		SpecializedEntity specializedEntity= specializedRepository.findOneById(form.getSpecializedId());
		entity.setSpecialized(specializedEntity);
		entity.setStatus(1);
		entity.setTitle(form.getTitle());
		entity.setModifiedBy(userDetails.getUsername());
		entity.setModifiedDate(new Date());
		entity = handbookRepository.save(entity);
	}

	@Override
	public List<HandbookDto> findAllByStatus(Integer status) {
		List<HandbookEntity> lstEntity= handbookRepository.findAllByStatus(status);
		List<HandbookDto> lstDto = new ArrayList<HandbookDto>();
		for(HandbookEntity entity:lstEntity) {
			HandbookDto dto =HandbookMapper.covertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}
	
	@Override
	public Page<HandbookDto> findAllByStatusPageable(Integer status, Pageable pageable) {
		Page<HandbookDto> dtos= handbookRepository.findAllByStatus(status,pageable);
		return dtos;
	}

	@Override
	public void updateHandbookByStatus(List<String> ids, Integer status) {
		handbookRepository.updateByStatus(ids, status);
	}

	@Override
	public void update(HandbookForm form) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandbookDto findOneById(Long id) {
		HandbookEntity entity= handbookRepository.findOneById(id);
		HandbookDto dto= HandbookMapper.covertToDto(entity);
		return dto;
	}

	@Override
	public HandbookDto findOneByIdApi(Long id) {
		HandbookDto dto= handbookRepository.findOneByIdApi(id);
		return dto;
	}

	@Override
	public List<HandbookDto> findAllByStatusAndUserId(Integer status, Long id,Pageable pageable) {
		List<HandbookEntity> lstEntity= handbookRepository.findAllByStatusAndUserId(status,id,pageable);
		List<HandbookDto> lstDto = new ArrayList<HandbookDto>();
		for(HandbookEntity entity:lstEntity) {
			HandbookDto dto =HandbookMapper.covertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<HandbookDto> searchHandbook(searchHandbookForm form) {
		List<HandbookEntity> lstEntity = new ArrayList<HandbookEntity>();
		if(form.getTitle()!=null&&!form.getTitle().equals("")) {
			if(form.getSpecializedId()!=null&&!form.getSpecializedId().equals("")) {
			 lstEntity = handbookRepository.findAllByTitleAndSpecializedId(form.getTitle(), form.getSpecializedId());
			}else {
			lstEntity = handbookRepository.findAllByTitleAndStatus(form.getTitle());
			}
		}else {
			if(form.getSpecializedId()!=null&&!form.getSpecializedId().equals("")) {
			 lstEntity = handbookRepository.findAllBySpecializedIdAndStatus(form.getSpecializedId(), 1);
			}else {
			 lstEntity = handbookRepository.findAllByStatus(1);
			}
		}
		List<HandbookDto> lstDto = new ArrayList<HandbookDto>();
		for(HandbookEntity entity  : lstEntity) {
			HandbookDto dto = HandbookMapper.covertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<HandbookDto> findRandomHandbook() {
		List<HandbookEntity> lstEntity= handbookRepository.findRandomHandbook();
		List<HandbookDto> lstDto = new ArrayList<HandbookDto>();
		for(HandbookEntity entity:lstEntity) {
			HandbookDto dto =HandbookMapper.covertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<HandbookDto> searchHandbookAndPageable(searchHandbookForm form,Long userId, Pageable page) {
		if(form.getTitle()==null||form.getTitle().equals("")) {
			form.setTitle("");
		}
		List<HandbookEntity> lstEntities= handbookRepository.searchHandbookAndPageable(form.getTitle(),
				form.getSpecializedId(),
				userId,
				Constant.del_flg_off,
				page);
		List<HandbookDto> lstDto = new ArrayList<>();
		for(HandbookEntity entity: lstEntities) {
			HandbookDto dto = HandbookMapper.covertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public Integer getTotalHandbookAndPageable(searchHandbookForm form, Long userId, Pageable page) {
		if(form.getTitle()==null||form.getTitle().equals("")) {
			form.setTitle("");
		}
		Integer totalElement = handbookRepository.getTotalHandbookAndPageable(form.getTitle(),
				form.getSpecializedId(),
				userId,
				Constant.del_flg_off);
		Integer remainder = totalElement % page.getPageSize();
		return remainder > 0 ? (totalElement / page.getPageSize()) + 1 : totalElement / page.getPageSize();
	}

	@Override
	public List<HandbookDto> searchHandbookUDeleteAndPageable(searchHandbookForm form, Long userId, Pageable page) {
		if(form.getTitle()==null||form.getTitle().equals("")) {
			form.setTitle("");
		}
		List<HandbookEntity> lstEntities= handbookRepository.searchHandbookAndPageable(form.getTitle(),
				form.getSpecializedId(),
				userId,
				Constant.del_flg_on,
				page);
		List<HandbookDto> lstDto = new ArrayList<>();
		for(HandbookEntity entity: lstEntities) {
			HandbookDto dto = HandbookMapper.covertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	public Integer getTotalHandbookUDeleteAndPageable(searchHandbookForm form, Long userId, Pageable page) {
		if(form.getTitle()==null||form.getTitle().equals("")) {
			form.setTitle("");
		}
		Integer totalElement = handbookRepository.getTotalHandbookAndPageable(form.getTitle(),
				form.getSpecializedId(),
				userId,
				Constant.del_flg_on);
		Integer remainder = totalElement % page.getPageSize();
		return remainder > 0 ? (totalElement / page.getPageSize()) + 1 : totalElement / page.getPageSize();
	}

	@Override
	public Page<HandbookDto> searchHandbookAndPageableapi(String title, Long specialzed,String userId, Pageable pageable) {
		Page<HandbookDto> page= handbookRepository.searchHandbookAndPageableApi(title,specialzed,userId,pageable);
		return page;
	}

	@Override
	public List<HandbookEntity> getAll() {
		List<HandbookEntity> handbookEntities = handbookRepository.findAll();
		return handbookEntities;
	}

	@Override
	public List<HandbookDto> getListOfRecentHandbook() {
		Pageable pageable = PageRequest.of(0,
				4,
				Sort.sort(HandbookEntity.class).by(HandbookEntity::getCreatedDate).descending());
		return handbookRepository.getListOfRecentHandbook(pageable);
	}

	@Override
	public List<HandbookDto> getFeaturedHandbook() {
		List<HandbookFeaturedDto> handbookFeaturedDtoList = handbookRepository.getFeaturedHandbook();
		List<Long> ids = handbookFeaturedDtoList.stream().map(HandbookFeaturedDto::getId).collect(Collectors.toList());
		return handbookRepository.getAllByIds(ids);
	}

	@Override
	public List<SearchFullTextDto> searchAllByFullText(String query) {
		List<HandbookEntity> handbookEntities = handbookRepository.searchAllByFullText(query);
		return handbookEntities.stream()
				.map(e -> SearchFullTextDto
						.builder()
						.id(e.getId())
						.title(e.getTitle())
						.description(e.getDescription())
						.img(e.getImg())
						.tableName("HANDBOOK")
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public void deleteHandbook(DeleteForm form) {
		List<Long> ids = form.getIds().stream().map(Long::parseLong).collect(Collectors.toList());
		commentRepository.deleteAllByHandbookIds(ids);
		handbookRepository.deleteAllById(ids);
	}

	@Override
	public boolean isValidSpecicalty(DeleteForm form) {
		List<Long> ids = form.getIds().stream().map(Long::parseLong).collect(Collectors.toList());
		Long total = handbookRepository.getTotalByStatusOfSpecialty(ids, Constant.del_flg_on);
		return total == 0L;
	}
}
