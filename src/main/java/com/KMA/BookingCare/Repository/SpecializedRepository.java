package com.KMA.BookingCare.Repository;

import java.util.List;

import com.KMA.BookingCare.Dto.SpecializedDto;
import com.KMA.BookingCare.Entity.HandbookEntity;
import com.KMA.BookingCare.Repository.CustomRepository.CustomSpecialtyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.KMA.BookingCare.Entity.HospitalEntity;
import com.KMA.BookingCare.Entity.SpecializedEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpecializedRepository extends JpaRepository<SpecializedEntity, Long>, CustomSpecialtyRepository {
	SpecializedEntity findOneById(Long Id);
	List<SpecializedEntity> findAllByStatus(Integer status,Pageable pageable);

	@Query(value = "SELECT s FROM SpecializedEntity AS s WHERE s.status = :status")
	Page<SpecializedEntity> findAllByStatusPage(@Param("status") Integer status,Pageable pageable);

	@Query(value = "select * from specialized where status =:status limit 5 ", nativeQuery = true)
	List<SpecializedEntity> findRandomSpecicalized(@Param("status") Integer status);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.SpecializedDto(s.id, s.name,s.code, s.description, s.img) FROM SpecializedEntity s WHERE s.status = :status ")
	Page<SpecializedDto> findAllByStatusApi(Integer status, Pageable pageable);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.SpecializedDto(s.id, s.name,s.code, s.description, s.img) FROM SpecializedEntity s WHERE s.status = 1 and s.id in (:ids) ")
	List<SpecializedDto> findAllByIds(@Param("ids") List<Long> ids);

	@Query(value = "SELECT *" +
			"FROM bookingCare.specialized " +
			"WHERE status = 1 AND MATCH(name, description) AGAINST (:query)", nativeQuery = true)
	List<SpecializedEntity> searchAllByFullText(@Param("query") String query);

	@Transactional
	@Modifying
	@Query(value = "UPDATE specialized  SET status = :status WHERE id in :ids", nativeQuery = true)
	void updateByStatusAndIds(@Param("status") Integer status, @Param("ids") List<Long> ids);
}
