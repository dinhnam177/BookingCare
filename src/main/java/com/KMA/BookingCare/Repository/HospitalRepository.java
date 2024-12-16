package com.KMA.BookingCare.Repository;

import java.util.List;

import com.KMA.BookingCare.Dto.HandbookDto;
import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Dto.HospitalFeaturedDto;
import com.KMA.BookingCare.Entity.HandbookEntity;
import com.KMA.BookingCare.Repository.CustomRepository.CustomHospitalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.KMA.BookingCare.Entity.HospitalEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long>,CustomHospitalRepository {
	HospitalEntity findOneById(Long id);
	@Query(value = "SELECT new com.KMA.BookingCare.Dto.HospitalDto(h.id, h.name, h.location, h.description, h.img, h.longitude, h.latitude) FROM HospitalEntity h WHERE h.status=:status")
	Page<HospitalDto> findAllByStatusApi(Integer status, Pageable pageable);
	List<HospitalEntity> findAllByStatus(Integer status);

	List<HospitalEntity> findAllByStatus(Integer status,Pageable pageable);

	@Query("SELECT h FROM HospitalEntity AS h WHERE h.status = :status")
	Page<HospitalEntity> findAllByStatusPage(@Param("status") Integer status,Pageable pageable);

	@Query(value = "select * from hospital where status =:status limit 5", nativeQuery = true)
	List<HospitalEntity> findRandomSpecicalized(@Param("status") Integer status);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.HospitalDto(h.id, h.name, h.location, h.description, h.img, h.longitude, h.latitude) FROM HospitalEntity h WHERE h.status= 1 AND h.id in (:ids)")
	List<HospitalDto> findAllByIds(@Param("ids") List<Long> ids);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.HospitalDto(h.id, h.name, h.location, h.description, h.img, h.longitude, h.latitude) FROM HospitalEntity h WHERE h.status= 1 AND h.name not in (:hospitalNames)")
	List<HospitalDto> findAllNotContainHospitalName(@Param("hospitalNames") List<String> hospitalNames);

	@Query(value = "SELECT *" +
			"FROM bookingCare.hospital " +
			"WHERE status = 1 AND MATCH(location, description, name) AGAINST (:query)", nativeQuery = true)
	List<HospitalEntity> searchAllByFullText(@Param("query") String query);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.HospitalDto(h.id, h.name, h.location, h.description, h.img, h.longitude, h.latitude) FROM HospitalEntity h WHERE h.status=:status AND h.name LIKE CONCAT('%',:name,'%')")
	Page<HospitalDto> findAllByNameIsLikeIgnoreCaseAndStatus(@Param("name") String name, @Param("status") Integer status, Pageable pageable);

	@Transactional
	@Modifying
	@Query(value = "UPDATE hospital  SET status = :status WHERE id in :ids", nativeQuery = true)
	void updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
