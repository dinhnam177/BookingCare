package com.KMA.BookingCare.Repository;

import java.util.List;
import java.util.Optional;

import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Entity.SpecializedEntity;
import com.KMA.BookingCare.Repository.CustomRepository.CustomUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import com.KMA.BookingCare.Entity.HandbookEntity;
import com.KMA.BookingCare.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>, CustomUserRepository {
	UserEntity findByUsername(String username);

	UserEntity findByUsernameAndStatus(String username, Integer status);

	Optional<UserEntity> findById(Long id);

	List<UserEntity> findAllByStatus(Integer status, Pageable pageable);

	UserEntity findOneById(Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE user  SET status = :status WHERE id in :ids", nativeQuery = true)
	Integer updateStatus(@Param("ids") List<String> ids, @Param("status") Integer status);

	List<UserEntity> findAllBySpecializedIdAndStatus(Long id, Integer status);

	@Query(value = "SELECT u FROM UserEntity u where u.specialized.id= :specialized and u.status =:status ")
	Page<User> findAllBySpecializedApi(@Param("specialized") Long specialized, @Param("status") Integer status, Pageable pageable);

	//Lấy danh sách tất cả những bác sĩ không có lịch khám trong ngày theo chuyên khoa đã chọn
	@Query(value = "SELECT * FROM user u where u.id not in (SELECT  doctor_id FROM medical_examination_schedule where date= :date and status =:statusMedical) and specialized_id= :specialized and status =:status ;", nativeQuery = true)
	List<UserEntity> findAllBySpecialized(@Param("date") String date, @Param("specialized") Long specialized, @Param("status") Integer status, @Param("statusMedical") Integer statusMedical);

	//Lấy ra tất cả những bác sĩ có lịch khám trong ngày
	@Query(value = "SELECT * FROM user as u where u.id in(SELECT doctor_id FROM medical_examination_schedule where date =:date and status =:statusMedical) and specialized_id= :specialized and status =:status ;", nativeQuery = true)
	List<UserEntity> findAllByMedical(@Param("date") String date, @Param("specialized") Long specialized, @Param("status") Integer status, @Param("statusMedical") Integer statusMedical);

	@Query(value = "SELECT * FROM user u ,user_role r where u.id =r.user_id and u.status =1 and r.role_id=2 ;", nativeQuery = true)
	List<UserEntity> findAllDoctor();

	@Query(value = "SELECT * FROM user u ,user_role r where u.id =r.user_id and u.status =1 and r.role_id=2 AND u.id NOT IN (:ids)", nativeQuery = true)
	List<UserEntity> findAllDoctorNotContainIds(@Param("ids") List<Long> ids);

	@Query(value = "SELECT * FROM user u ,user_role r where u.id =r.user_id and u.status =1 and r.role_id=2 AND 1=:sql", nativeQuery = true)
	List<UserEntity> searchAllDoctor(@Param("sql") String sql);

	@Query(value = "SELECT peer_id FROM user u WHERE u.id=:id", nativeQuery = true)
	String findPeerIdById(Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE user  SET peer_id = :peerId WHERE id = :id", nativeQuery = true)
	Integer updatePeerId(@Param("peerId") String peerId, @Param("id") Long id);

	@Query(value = "SELECT * FROM user u,user_role r where u.id=r.user_id and r.role_id=2 ORDER BY RAND() LIMIT 7;", nativeQuery = true)
	List<UserEntity> findRandomDoctor();

	//Lấy danh sách tất cả những bác sĩ không có lịch khám trong ngày theo bệnh viện đã chọn
	@Query(value = "SELECT * FROM user u where u.hospital_id= :hospitalId AND u.id not in (SELECT  doctor_id FROM medical_examination_schedule where date= :date and status =:statusMedical)  and status =:status ;", nativeQuery = true)
	List<UserEntity> findAllByHospital(@Param("date") String date, @Param("hospitalId") Long hospitalId, @Param("status") Integer status, @Param("statusMedical") Integer statusMedical);

	//Lấy ra tất cả những bác sĩ có lịch khám trong ngày theo bệnh viện đã chọn
	@Query(value = "SELECT * FROM user as u where u.id in(SELECT doctor_id FROM medical_examination_schedule where date =:date and status =:statusMedical) AND u.hospital_id= :hospitalId and status =:status ;", nativeQuery = true)
	List<UserEntity> findAllByMedicalAndHospital(@Param("date") String date, @Param("hospitalId") Long hospitalId, @Param("status") Integer status, @Param("statusMedical") Integer statusMedical);

	@Query(value = "SELECT u.description,u.id,u.email,u.full_name,u.img,u.location,u.phone_number,u.sex,u.username,"
			+ "u.password,u.status,u.year_of_birth,u.hospital_id,u.specialized_id,u.short_description,u.peer_id, u.examination_price " +
			" FROM user u ,user_role ur WHERE u.id = ur.user_id AND ur.role_id in (:roleIds) AND u.status = :status AND u.full_name LIKE CONCAT('%',:fullName,'%')" +
			" AND  ( (:specializedId IS NOT NULL AND specialized_id =:specializedId) || :specializedId IS NULL)" +
			" AND  ( (:hospitalId IS NOT NULL AND hospital_id =:hospitalId) || :hospitalId IS NULL)", nativeQuery = true)
	List<UserEntity> searchHandbookAndPageable(@Param("fullName") String fullName,
											   @Param("specializedId") Long specializedId,
											   @Param("hospitalId") Long hospitalId, Pageable page,
											   @Param("roleIds") List<Integer> roleIds,
											   @Param("status") Integer status);

	@Query(value = "SELECT count(*)" +
			" FROM user u ,user_role ur WHERE u.id= ur.user_id AND ur.role_id in (:roleIds) AND u.status = :status AND u.full_name   LIKE CONCAT('%',:fullName,'%')" +
			" AND  ( (:specializedId IS NOT NULL AND specialized_id =:specializedId) || :specializedId IS NULL)" +
			" AND  ( (:hospitalId IS NOT NULL AND hospital_id =:hospitalId) || :hospitalId IS NULL)", nativeQuery = true)
	Integer searchToTalHandbookAndPageable(@Param("fullName") String fullName,
										   @Param("specializedId") Long specializedId,
										   @Param("hospitalId") Long hospitalId,
										   @Param("roleIds") List<Integer> roleIds,
										   @Param("status") Integer status);


	Boolean existsByUsername(String username);

	@Query(value = "SELECT u FROM UserEntity u where u.hospital.id= :hospitalId and u.status =:status ")
	Page<User> findAllDoctorByHospitalAndStatus(@Param("hospitalId") Long hospitalId,
												@Param("status")Integer status, Pageable pageable);

	@Query(value = "SELECT u FROM UserEntity  u inner join u.roles ur WHERE ur.id=2")
	Page<User>  findAllDoctor(Pageable pageable);

	@Query(value = "select email from user u where u.id in (\n" +
			"select user_id from medical_examination_schedule where id in :ids " +
			")", nativeQuery = true)
	List<String> getEmailByIds(@Param("ids") Long[] ids);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.User(u.id, u.fullName, u.img, u.shortDescription) FROM UserEntity  u inner join u.roles ur WHERE ur.id=2")
	List<User> getDoctorOnline(Pageable pageable);

	@Query(value = "SELECT new com.KMA.BookingCare.Dto.User(u.id, u.fullName, u.img, u.specialized.name, u.shortDescription) FROM UserEntity u INNER JOIN u.roles ur WHERE ur.id=2 AND u.id in (:ids)")
	List<User> findAllDoctorByIds(@Param("ids") List<Long> ids);

	@Query(value = "SELECT u FROM UserEntity u INNER JOIN u.workTimeEntity w WHERE u.specialized.id = :specialtyId and w.id =:workTimeId")
	List<User> findAllBySpecialtyIdAndWorkTimeId(@Param("specialtyId") Long specialtyId,
												 @Param("workTimeId") Long workTimeId);

	@Query(value = "SELECT * " +
			"FROM bookingCare.user as u " +
			"inner join user_role as ur " +
			"ON ur.user_id = u.id " +
			"WHERE u.status = 1 AND ur.role_id = 2 AND MATCH(full_name, description, short_description) AGAINST (:query) ", nativeQuery = true)
	List<UserEntity> searchAllByFullText(@Param("query") String query);

	@Query(value = "SELECT u " +
			" FROM UserEntity u INNER JOIN u.roles ur WHERE ur.id = 2 AND u.status = 1 AND u.fullName LIKE CONCAT('%',:fullName,'%') " +
			" AND ( (0L != :specializedId AND u.specialized.id = :specializedId) OR (0L = :specializedId)) " +
			" AND ( (0L != :hospitalId AND u.hospital.id = :hospitalId) OR (0L = :hospitalId))")
	Page<User> searchDoctorAndPageable(@Param("fullName") String fullName,@Param("specializedId") Long specializedId,@Param("hospitalId") Long hospitalId,Pageable page);

	@Query(value = "SELECT count(u) FROM UserEntity AS u WHERE u.specialized.id in (:ids) AND u.status = 1")
	Long existsBySpecial(@Param("ids") List<Long> ids);

	@Query(value = "SELECT count(u) FROM UserEntity AS u WHERE u.hospital.id in (:ids) AND u.status = 1")
	Long existsByHospital(@Param("ids") List<Long> ids);

	@Query(value = "SELECT u FROM UserEntity AS u WHERE u.specialized.id in (:ids)")
	List<UserEntity> findAllBySpecializedIds(@Param("ids") List<Long> ids);

	@Query("SELECT u FROM UserEntity AS u WHERE u.hospital.id in (:ids)")
	List<UserEntity> findAllByHospitalIds(@Param("ids") List<Long> ids);

	UserEntity findByUsernameAndEmail(String userName, String email);

	@Transactional
	@Modifying
	@Query(value = "UPDATE user SET password = :password WHERE username = :username", nativeQuery = true)
	void updatePasswordByUsername(@Param("password") String password, @Param("username") String username);

	@Query(value = "SELECT count(u) FROM UserEntity AS u WHERE u.id in (:ids) AND (u.hospital.status = :status OR u.specialized.status = :status)")
	Long getTotalUserBySpecialStatusOrHospitalStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
