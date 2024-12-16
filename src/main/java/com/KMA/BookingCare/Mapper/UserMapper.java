package com.KMA.BookingCare.Mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Dto.Role;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.RoleEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Entity.WorkTimeEntity;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.KMA.BookingCare.common.Constant;
import com.KMA.BookingCare.common.GetUtils;
import org.apache.logging.log4j.util.Strings;

public class UserMapper {
	public static User convertToDto(UserEntity entity) {
		User dto = new User();
		dto.setId(entity.getId());
		dto.setImg(Strings.isBlank(entity.getImg()) ? Constant.default_avatar : entity.getImg());
		dto.setName(entity.getFullName());
		dto.setDescription(entity.getDescription());
		dto.setShortDescription(entity.getShortDescription());
		dto.setSex(entity.getSex());
		dto.setPhone(entity.getPhoneNumber());
		dto.setLocation(entity.getLocation());
		if(entity.getHospital()!=null) {
			dto.setHospitalId(entity.getHospital().getId());
		}
		dto.setEmail(entity.getEmail());
		dto.setUsername(entity.getUsername());
		dto.setYearOfBirth(entity.getYearOfBirth());
		if(entity.getSpecialized()!=null) {
			dto.setSpecializedId(entity.getSpecialized().getId());
			dto.setSpecializedName(entity.getSpecialized().getName());
		}
		if(entity.getHospital()!=null) {
			dto.setHospitalName(entity.getHospital().getName());
			dto.setHospitalLocation(entity.getHospital().getLocation());
		}
		if(entity.getWorkTimeEntity()!=null) {
			Set<WorkTimeEntity> wkEntityLst= entity.getWorkTimeEntity();
			Set<WorkTimeDto> wkDtoLst= new HashSet<WorkTimeDto>();
			for(WorkTimeEntity wkEntity: wkEntityLst) {
				WorkTimeDto wkDto = WorkTimeMapper.convertToDto(wkEntity);
				wkDtoLst.add(wkDto);
			}
			List<WorkTimeDto> targetList = new ArrayList<>(wkDtoLst);
			dto.setLstWorkTime( targetList);
		}
		if (entity.getRoles() != null) {
			Set<RoleEntity> roleEntities = entity.getRoles();
			Set<Role> roles = roleEntities.stream().map(item -> Role.builder()
					.id(item.getId())
					.name(item.getName())
					.build())
					.collect(Collectors.toSet());
			dto.setRoles(roles);
			dto.setRole(GetUtils.getRole(roles));
		}
		dto.setExaminationPrice(entity.getExaminationPrice());
		return dto;
	}

	public static MyUser convertToMyUser(UserDetailsImpl user) {
		MyUser myUser= new MyUser(user.getUsername(), user.getPassword(), true, true, true, true, user.getAuthorities());
		myUser.setFullName(user.getFullName());
		myUser.setId(user.getId());
		myUser.setImg(Strings.isBlank(user.getImg()) ? Constant.default_avatar : user.getImg());
		List<String> lstRole= new ArrayList<String>();
		user.getAuthorities().forEach(p-> {
			lstRole.add(p.getAuthority());
		});
		myUser.setRoles(lstRole);
		return myUser;
	}

}
