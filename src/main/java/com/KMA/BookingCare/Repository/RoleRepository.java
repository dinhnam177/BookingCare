package com.KMA.BookingCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KMA.BookingCare.Entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
	
	List<RoleEntity> findByName(String name);

}
