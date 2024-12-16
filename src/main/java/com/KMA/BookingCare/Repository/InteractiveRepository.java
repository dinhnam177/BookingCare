package com.KMA.BookingCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KMA.BookingCare.Entity.InteractiveEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InteractiveRepository extends JpaRepository<InteractiveEntity, Long> {
	
	InteractiveEntity findOneByUserIdAndYouId(Long userId,Long youId);

	List<InteractiveEntity> findAllByYouIdOrderByCreatedDateDesc(Long youId);

	@Modifying
	@Query("DELETE FROm InteractiveEntity AS i WHERE i.userId in (:ids) OR i.youId in (:ids)")
	void deleteAllByUserIdOrYouId(@Param("ids") List<Long> ids);

}
