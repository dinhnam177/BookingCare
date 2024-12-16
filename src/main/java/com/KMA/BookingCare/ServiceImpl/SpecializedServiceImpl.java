package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Api.form.SpecializedForm;
import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Entity.SpecializedEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.SpecializedMapper;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Repository.CommentRepository;
import com.KMA.BookingCare.Repository.HandbookRepository;
import com.KMA.BookingCare.Repository.SpecializedRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.SpecializedService;
import com.KMA.BookingCare.Service.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SpecializedServiceImpl implements SpecializedService{
	
	@Autowired
	private SpecializedRepository specializedRepository;

	@Autowired
	private Cloudinary cloudinary;

	@Autowired
	private HandbookRepository handbookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserService userService;


	@Override
	public List<SpecializedDto> findAll() {
		List<SpecializedEntity> lstentity = specializedRepository.findAll();
		List<SpecializedDto> lstDto = new ArrayList<SpecializedDto>();
		for(SpecializedEntity entity : lstentity) {
			SpecializedDto dto = SpecializedMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<SpecializedDto> findAllByStatus(Integer status, Pageable pageable) {
		List<SpecializedEntity> lstEntity = specializedRepository.findAllByStatus(status,pageable);
		List<SpecializedDto> lstDto = new ArrayList<>();
		for(SpecializedEntity entity: lstEntity) {
			SpecializedDto dto = SpecializedMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public Page<SpecializedDto> findAllByStatusApi(Integer status, Pageable pageable) {
		Page<SpecializedDto> page = specializedRepository.findAllByStatusApi(1,pageable);
		return page;
	}

	@Override
	public void saveOrUpdateSpecialized(SpecializedForm form) throws ParseException {
	UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();
	MyUser userDetails = UserMapper.convertToMyUser(user);
		SpecializedEntity entity= new SpecializedEntity();
		if(form.getId()!=null) {
			entity.setId(form.getId());
			if(Strings.isBlank(form.getImg().getOriginalFilename())) {
				entity.setImg(form.getImgOld());
			}else {
				try {
					Map result= cloudinary.uploader().upload(form.getImg().getBytes(),
							ObjectUtils.asMap("resource_type","auto"));
					String urlImg=(String) result.get("secure_url");
					entity.setImg(urlImg);
				} catch (Exception e) {
					System.out.println("ERROR:upload img specialized fail!!!");
				}
			}
		}else {
			try {
				Map result= cloudinary.uploader().upload(form.getImg().getBytes(),
						ObjectUtils.asMap("resource_type","auto"));
				String urlImg=(String) result.get("secure_url");
				entity.setImg(urlImg);
			} catch (Exception e) {
				System.out.println("ERROR:upload img specialized fail!!!");
			}
		}

		entity.setName(form.getName());
		entity.setCode(form.getCode());
		entity.setDescription(form.getDescription());
		entity.setStatus(1);
		entity = specializedRepository.save(entity);
	}

	@Override
	public List<SpecializedDto> findRandomSpecicalized() {
		List<SpecializedEntity> lstEntity = specializedRepository.findRandomSpecicalized(1);
		List<SpecializedDto> lstDto = new ArrayList<SpecializedDto>();
		for(SpecializedEntity entity: lstEntity) {
			SpecializedDto dto = SpecializedMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<SpecializedDto> getFeaturedSpecialty() {
		List<SpecialtyFeaturedDto> specialtyFeaturedDtos = specializedRepository.getFeaturedSpecialty();
		List<Long> ids = specialtyFeaturedDtos.stream().map(SpecialtyFeaturedDto::getId).collect(Collectors.toList());
		return specializedRepository.findAllByIds(ids);
	}

	@Override
	public SpecializedDto findOneById(Long id) {
		SpecializedEntity specializedEntity = specializedRepository.findOneById(id);
		SpecializedDto specializedDto = SpecializedMapper.convertToDto(specializedEntity);
		return specializedDto;
	}

	@Override
	public List<SearchFullTextDto> searchAllByFullText(String query) {
		List<SpecializedEntity> specializedEntities = specializedRepository.searchAllByFullText(query);
		return specializedEntities.stream()
				.map(e -> SearchFullTextDto.builder()
						.id(e.getId())
						.title(e.getName())
						.description(e.getDescription())
						.img(e.getImg())
						.tableName("SPECIALZED")
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public void updateByStatusAndIds(List<String> ids, Integer status) {
		specializedRepository.updateByStatusAndIds(status, ids.stream()
				.map(Long::parseLong)
				.collect(Collectors.toList()));
	}

	@Override
	public boolean isExistItemRelationWithSpecialIsUsing(List<String> ids) {
		List<Long> specialziedIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
		Long totalHandBook = handbookRepository.existsBySpecial(specialziedIds);
		Long totalUser = userRepository.existsBySpecial(specialziedIds);
		return (!Objects.equals(0L, totalUser) || !Objects.equals(0L, totalHandBook));
	}

	@Override
	public void delete(List<String> ids) {
		List<Long> specialedIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
		List<Long> handbookIds = handbookRepository.findAllBySpecializedIds(specialedIds)
				.stream()
				.map(HandbookDto::getId)
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(handbookIds)) {
			commentRepository.deleteAllByHandbookIds(handbookIds);
		}
		handbookRepository.deleteAllBySpecialized(specialedIds);
		List<UserEntity> userEntities = userRepository.findAllBySpecializedIds(specialedIds);
		List<String> userIds = userEntities.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
		userService.deleteUser(userIds);
		specializedRepository.deleteAllById(specialedIds);
	}

	@Override
	public Integer getTotalByStatus(Integer status, Pageable pageable) {
		Page<SpecializedEntity> lstEntity = specializedRepository.findAllByStatusPage(status,pageable);
		return lstEntity.getTotalPages();
	}


}
