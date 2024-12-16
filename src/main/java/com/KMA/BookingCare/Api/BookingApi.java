package com.KMA.BookingCare.Api;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.KMA.BookingCare.Api.form.BookingForm;
import com.KMA.BookingCare.Api.form.ChangeDateForm;
import com.KMA.BookingCare.Api.form.HandbookForm;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Service.MedicalExaminationScheduleService;
import com.KMA.BookingCare.Service.WorkTimeService;

//@Controller
@RestController()
@RequestMapping("/api")
public class BookingApi {

	private final Logger log = LoggerFactory.getLogger(BookingApi.class);

	@Autowired
	private MedicalExaminationScheduleService medicalServiceImpl;
	
	@Autowired
	private WorkTimeService workTimeserviceImpl;
	
	@PostMapping(value = "/booking")
	public ResponseEntity<?>  booking(@RequestBody BookingForm form, HttpSession session) throws JsonProcessingException {
		try {
			if(session.getAttribute("userDetails")!=null) {
				MyUser user =(MyUser) session.getAttribute("userDetails");
				form.setUserId(user.getId());
			} else {
				log.info("login by app");
				Object result = SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				UserDetailsImpl user = (UserDetailsImpl) result;
				MyUser userDetails = UserMapper.convertToMyUser(user);
				form.setUserId(userDetails.getId());
			}
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(medicalServiceImpl.save(form));
	}

	@PostMapping(value = "/booking-mobile")
	public ResponseEntity<?>  bookingForMobild(@RequestBody BookingForm form, HttpSession session) throws JsonProcessingException {
		try {
			if(session.getAttribute("userDetails")!=null) {
				MyUser user =(MyUser) session.getAttribute("userDetails");
				form.setUserId(user.getId());
			} else {
				log.info("login by app");
				Object result = SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				UserDetailsImpl user = (UserDetailsImpl) result;
				MyUser userDetails = UserMapper.convertToMyUser(user);
				form.setUserId(userDetails.getId());
			}
		}catch (Exception e){
			log.error(e.getMessage());
		}
		medicalServiceImpl.saveForMobile(form);
		return ResponseEntity.ok(true);
	}
	
	@PostMapping(value = "/changedate")
	public ResponseEntity<?> changeDate(@RequestBody ChangeDateForm form) throws ParseException {
	List<WorkTimeDto> lstDtos = workTimeserviceImpl.findByDateAndDoctorId(form.getDate(), form.getIdDoctor());
	 return new ResponseEntity<Object>(lstDtos, HttpStatus.OK);
	}

	@PostMapping(value = "/update-booking")
	public ResponseEntity<?>  updateBooking(@RequestBody BookingForm form) throws JsonProcessingException {
		medicalServiceImpl.update(form);
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/update-time-booking")
	public ResponseEntity<?>  updateTimeBooking(@RequestBody BookingForm form) throws JsonProcessingException {
		medicalServiceImpl.updateTime(form);
		return ResponseEntity.ok(true);
	}

	@PostMapping(value = "/cancel-booking/{id}")
	public ResponseEntity<?>  cancelBooking(@PathVariable Long id) {
		log.info("Request to cancel medical with id: {}", id);
		medicalServiceImpl.cancelMedical(id);
		return ResponseEntity.ok(true);
	}

}
