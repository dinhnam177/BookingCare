package com.KMA.BookingCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KMA.BookingCare.Entity.CommentEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	List<CommentEntity> findAllByHandbookId(Long id);

	Boolean existsByHandbookId(Long id);

	@Transactional
	@Modifying
	@Query("DELETE FROM CommentEntity AS c WHERE c.handbook.id in (:ids)")
	void deleteAllByHandbookIds(@Param("ids") List<Long> ids);

	@Modifying
	@Query("DELETE FROM CommentEntity AS c WHERE c.user.id in (:ids)")
	void deleteAllByUser(@Param("ids") List<Long> ids);

	@Modifying
	@Query("DELETE FROM CommentEntity AS c WHERE c.handbook.specialized.id in (:ids)")
	void deleteAllBySpecialzed(@Param("ids") List<Long> ids);



}
