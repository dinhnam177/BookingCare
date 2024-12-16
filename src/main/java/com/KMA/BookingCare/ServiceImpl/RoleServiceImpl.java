package com.KMA.BookingCare.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Dto.Role;
import com.KMA.BookingCare.Entity.RoleEntity;
import com.KMA.BookingCare.Repository.RoleRepository;
import com.KMA.BookingCare.Service.RoleService;
@Service
public class RoleServiceImpl implements RoleService {

		@Autowired
		private RoleRepository roleRepository;
	@Override
	public List<Role> findAll() {
		List<RoleEntity> lstEntity= roleRepository.findAll();
		List<Role> lstDto= new ArrayList<Role>();
		for(RoleEntity entity: lstEntity) {
			Role dto= new Role();
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			lstDto.add(dto);
		}
		return lstDto;
	}

}
