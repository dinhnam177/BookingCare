package com.KMA.BookingCare.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.KMA.BookingCare.Api.form.*;
import com.KMA.BookingCare.Dto.SearchFullTextDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.KMA.BookingCare.Dto.Role;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Entity.UserEntity;

public interface UserService {
	public void add(User user, String roleName);
	public void update(User user);
	public void delete(Long id);
	public User findOneById(Long id);
	public User findByUsername(String username) ;
	public List<User> getAll();
	public void saveDoctor(UserForm form) throws IllegalStateException, IOException;
	public List<User> findAll();
	public List<User> findAllByStatus(Integer status,Pageable pageable);
	public void updateUserByStatus(List<String> ids, Integer status);
	public List<User> findAllBySpecializedIdAndStatus(Long id, Integer status);
	List<User> findAllBySpecialized( String date, Long specialized, Integer status);
	List<User> findAllByMedical( String date, Long specialized, Integer status);
	List<User> findAllDoctorOfSpecialized( Long specialized, Integer status);
	Page<User> findAllDoctorOfSpecializedApi(Long specialized, Integer status, Pageable pageable);
	List<User> findAllDoctorOfHospital( Long hospitalId, Integer status);
	Page<User> findAllDoctorOfHospitalApi( Long hospitalId, Integer status, Pageable pageable);
	void updateClient(UpdateCientForm form);
	List<User> findAllDoctor();
	List<User> searchDoctor( searchDoctorForm form);

	List<User> searchDoctorAndPageable( searchDoctorForm form, String roleUser, Pageable page, Integer status);

	Integer searchTotalPageDoctorAndPageable( searchDoctorForm form, String roleUser, Pageable page, Integer status);

	Page<User> findAllDoctor(Pageable pageable);
	
	
	//tìm bác sĩ và lịch khám trống của nó
	User findOneDoctorAndWorktime(Long id, String date) throws ParseException;
	
	String findPeerIdById(Long id);
	Integer updatePeerId(String peerId, Long id);
	
	//findRandomDOctor
	List<User> findRandomDoctor();

	List<User> getDoctorOnline();

	List<User> getFeaturedDoctor();

	List<User> findAllDoctorBySpecialIdAndWorkTimeIdAndDate(Long specialtyId, Long WorkTimeId, String date);

	List<SearchFullTextDto> searchAllByFullText(String query);

	Page<User> searchDoctorForClient(Long hospitalId, Long specialtyId, String doctorName, Pageable pageable);

	boolean isExistItemRelationWithSpecialIsUsing(List<String> ids);

	void deleteUser(List<String> ids);

	boolean isExistUserNameAndEmail(String userName, String email);

	String createUrlResetPassword(String userName);

	void sendMessageResetPassword(String userName) throws JsonProcessingException;

	void changPassword(String userName, String password);

	boolean verifyUserName(String userName, String key);

	boolean isValidSpecialtyAndHospital(DeleteForm form);

	boolean isValidWorkTime(Long workTimeId);
}
