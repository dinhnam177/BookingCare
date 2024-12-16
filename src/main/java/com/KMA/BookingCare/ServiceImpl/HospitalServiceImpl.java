package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Api.form.HospitalForm;
import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Dto.HospitalFeaturedDto;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Dto.SearchFullTextDto;
import com.KMA.BookingCare.Entity.HospitalEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.HospitalMapper;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Repository.HospitalRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.HospitalService;
import com.KMA.BookingCare.Service.UserService;
import com.KMA.BookingCare.common.Constant;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService{
	
	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private Cloudinary cloudinary;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Override
	public List<HospitalDto> findAll() {
		List<HospitalEntity> lstEntity= hospitalRepository.findAll();
		List<HospitalDto> lstDto = new ArrayList<HospitalDto>();
		for(HospitalEntity entity: lstEntity) {
			HospitalDto dto = new HospitalDto();
			dto.setDescription(entity.getDescription());
			dto.setId(entity.getId());
			dto.setLocation(entity.getLocation());
			dto.setName(entity.getName());
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public void saveHospital(HospitalForm form)  throws ParseException {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		MyUser userDetails = UserMapper.convertToMyUser(user);
		HospitalEntity entity= new HospitalEntity();
		if(form.getId()!=null) {
			entity.setId(form.getId());
			if(form.getImg().getOriginalFilename()==null||form.getImg().getOriginalFilename().equals("")) {
				entity.setImg(form.getImgOld());
			}else {
				try {
					Map result= cloudinary.uploader().upload(form.getImg().getBytes(),
							ObjectUtils.asMap("resource_type","auto"));
					String urlImg=(String) result.get("secure_url");
					entity.setImg(urlImg);
				} catch (Exception e) {
					System.out.println("upload img fail");
				}
			}
		}else {
			try {
				Map result= cloudinary.uploader().upload(form.getImg().getBytes(),
						ObjectUtils.asMap("resource_type","auto"));
				String urlImg=(String) result.get("secure_url");
				entity.setImg(urlImg);
			} catch (Exception e) {
				System.out.println("upload img fail");
			}
		}
		
		entity.setName(form.getName());
		entity.setLocation(form.getLocation());
		entity.setDescription(form.getDescription());
		entity.setLatitude(form.getLatitude());
		entity.setLongitude(form.getLongitude());
		entity.setStatus(1);
		entity = hospitalRepository.save(entity);
	}

	@Override
	public List<HospitalDto> findAllByStatus(Integer status) {
		List<HospitalEntity> lstEntity = hospitalRepository.findAllByStatus(1);
		List<HospitalDto> lstDto = new ArrayList<HospitalDto>();
		for(HospitalEntity entity: lstEntity) {
			HospitalDto dto = HospitalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<HospitalDto> findRandomSpecicalized() {
		List<HospitalEntity> lstEntity = hospitalRepository.findRandomSpecicalized(1);
		List<HospitalDto> lstDto = new ArrayList<HospitalDto>();
		for(HospitalEntity entity: lstEntity) {
			HospitalDto dto = HospitalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public HospitalEntity findOneById(Long id) {
		HospitalEntity hospitalEntity = hospitalRepository.findOneById(id);
		hospitalEntity.setLstuser(Collections.emptyList());
		return hospitalEntity;
	}

	@Override
	public List<HospitalDto> getFeaturedHospital() {
		List<HospitalFeaturedDto> hospitalFeaturedDtos = hospitalRepository.getFeaturedHospital();
		List<Long> hospitalIds = hospitalFeaturedDtos.stream().map(HospitalFeaturedDto::getId).collect(Collectors.toList());
		List<HospitalDto> results = hospitalRepository.findAllByIds(hospitalIds);
		return results;
	}

	@Override
	public List<SearchFullTextDto> searchAllByFullText(String query) {
		List<HospitalEntity> hospitalEntities = hospitalRepository.searchAllByFullText(query);
		return hospitalEntities.stream()
				.map(e -> SearchFullTextDto.builder()
						.id(e.getId())
						.title(e.getName())
						.description(e.getDescription())
						.img(e.getImg())
						.tableName("HOSPITAL")
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public Page<HospitalDto> searchByNameAndStatus(String query, Pageable pageable) {
		Page<HospitalDto> page = hospitalRepository.findAllByNameIsLikeIgnoreCaseAndStatus(query,1, pageable);
		return page;
	}

	@Override
	public boolean isExistItemRelationWithSpecialIsUsing(List<String> ids) {
		Long totalUser = userRepository.existsByHospital(ids.stream().map(Long::parseLong).collect(Collectors.toList()));
		return !Objects.equals(0L, totalUser);
	}

	@Override
	public void updateByStatusAndIds(List<String> ids, Integer status) {
		hospitalRepository.updateStatusByIds(ids.stream()
						.map(Long::parseLong)
						.collect(Collectors.toList()),
				status);
	}

	@Override
	public void deleteHospitals(List<String> ids) {
		List<Long> hospitalIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
		List<UserEntity> userEntities = userRepository.findAllByHospitalIds(hospitalIds);
		List<String> userIds = userEntities.stream().map(e -> String.valueOf(e.getId())).distinct().collect(Collectors.toList());
		userService.deleteUser(userIds);
		hospitalRepository.deleteAllById(hospitalIds);
	}

	@Override
	public List<HospitalDto> getNearbyHospital(Double lat, Double lng) {
		Page<HospitalDto> page = hospitalRepository.findAllByStatusApi(Constant.del_flg_off, Pageable.ofSize(Integer.MAX_VALUE));
		List<HospitalDto> hospitalDtos = page.getContent();
		for(HospitalDto dto : hospitalDtos) {
			Double distance = getDistance(lat, lng,dto.getLongitude(), dto.getLatitude());
			dto.setDistance(distance);
		}
		return hospitalDtos.stream()
				.sorted(Comparator.comparing(HospitalDto::getDistance))
				.limit(5)
				.collect(Collectors.toList());
	}

	@Override
	public List<HospitalDto> findAllByStatus(Integer status, Pageable pageable) {
		List<HospitalEntity> lstEntity = hospitalRepository.findAllByStatus(status,pageable);
		List<HospitalDto> lstDto = new ArrayList<>();
		for(HospitalEntity entity: lstEntity) {
			HospitalDto dto = HospitalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public Integer getTotalByStatus(Integer status, Pageable pageable) {
		Page<HospitalEntity> page = hospitalRepository.findAllByStatusPage(status,pageable);
		return page.getTotalPages();
	}

	@Override
	public Page<HospitalDto> findAllByStatusApi(Integer status, Pageable pageable) {
		Page<HospitalDto> page = hospitalRepository.findAllByStatusApi(1,pageable);
		return page;
	}

	private Double getDistance(double latParam, double lngParam, Double lng, Double lat) {

		lng = lng == null ? 0.0 : lng;
		lat = lat == null ? 0.0 : lat;

		final double PI = 3.141592;
		double degree = PI / 180;

		double distance = Math.pow((lngParam * degree - lng * degree) *
				Math.cos(((latParam * 3600 + lat * 3600) / 2 / 3600) * degree), 2)
				+ Math.pow(latParam * degree - lat * degree, 2) ;
		return distance;
	}


}
