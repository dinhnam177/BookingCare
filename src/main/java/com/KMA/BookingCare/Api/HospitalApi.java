package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.HospitalForm;
import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Entity.HospitalEntity;
import com.KMA.BookingCare.Repository.HospitalRepository;
import com.KMA.BookingCare.Service.HospitalService;
import com.KMA.BookingCare.common.Constant;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("")
public class HospitalApi {

	private final Logger log = LoggerFactory.getLogger(HospitalApi.class);
	
	@Autowired
	private HospitalService hospitalServiceImpl;

	@Autowired
	private HospitalRepository hospitalRepository;


	@PostMapping(value = "/api/hospital")
	public ResponseEntity<?> addHospital(@RequestBody HospitalForm form) {
		log.info("Request to save hospital {}");
		try {
			hospitalServiceImpl.saveHospital(form);
		} catch (Exception e ) {
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping(value = "/api/hospital/{id}")
	public ResponseEntity<?> editHospital(@RequestBody HospitalForm form, @PathVariable long id) {
		log.info("Request to update hospital {}", id);
		try {
			hospitalServiceImpl.saveHospital(form);
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return ResponseEntity.ok("true");
	}

	@GetMapping(value = "/api/hospital/{id}")
	public ResponseEntity<HospitalEntity> findOne(@PathVariable Long id){
		log.info("Request to get one by Id {}", id);
		HospitalEntity hospitalEntity = hospitalServiceImpl.findOneById(id);
		return  ResponseEntity.ok(hospitalEntity);
	}

	@GetMapping(value = "/api/hospital")
	public ResponseEntity<Page<HospitalDto>> findAll(@PageableDefault(page = 0, size = 10) Pageable pageable){
		log.info("Request to find all {}");
		Page<HospitalDto> page = hospitalServiceImpl.findAllByStatusApi(1,pageable);
		return  ResponseEntity.ok(page);

	}

	
	@GetMapping(value = "/api/hospital/get-all-by-status")
	public ResponseEntity<Page<HospitalDto>> getAllByStatus(@PageableDefault(page = 0, size = 10) Pageable pageable){
		log.info("Request to get All By Status {}");
		Page<HospitalDto> page = hospitalServiceImpl.findAllByStatusApi(1,pageable);
		return ResponseEntity.ok(page);
	}

	@Hidden
	@DeleteMapping("/api/hospital/deletes")
	public ResponseEntity<?> deleteAll(@RequestBody List<Long> ids){
		log.info("Request to delete all by ids {}", ids);
		hospitalRepository.deleteAllById(ids);
//		hospitalSearchRepository.deleteAllById(ids);
		return ResponseEntity.noContent().build();
	}

	@Hidden
	@DeleteMapping("/api/hospital/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		log.info("Request to delete {}", id);
		hospitalRepository.deleteById(id);
//		hospitalSearchRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/*
	 * hien thi len trang chu
	 *  */
	@GetMapping(value = "/api/hospital/get-random")
	public ResponseEntity<List<HospitalDto>> getRandom(){
		log.info("Request to get Random specicalized");
		List<HospitalDto> lstBenhVien = hospitalServiceImpl.findRandomSpecicalized();
		return ResponseEntity.ok(lstBenhVien);
	}

	//old

	@PostMapping(value = "/admin/api/managerHospital/add")
	public ResponseEntity<?> addHospitalOld(@ModelAttribute HospitalForm form) {
		try {
			hospitalServiceImpl.saveHospital(form);
		} catch (Exception e ) {
			e.printStackTrace();
			System.out.println("looxi");
		}
		return ResponseEntity.ok("true");
	}

	@PostMapping(value = "/api/hospital/delete")
	public ResponseEntity<?> deleteHospital(@RequestBody DeleteForm form) {
		try {
			hospitalServiceImpl.deleteHospitals(form.getIds());
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}

	}

	@PostMapping(value = {"/api/hospital/uDelete"})
	public ResponseEntity<?> uDeleteSpecialzed(@RequestBody DeleteForm form) {
		log.info("Request to uDelete hospital with ids: {}",form.getIds());
		boolean isExist = hospitalServiceImpl.isExistItemRelationWithSpecialIsUsing(form.getIds());
		if (isExist) {
			return ResponseEntity.badRequest().body("Không thể xoá do vẫn còn người dùng đang đuợc sử dụng");
		}
		try {
			hospitalServiceImpl.updateByStatusAndIds(form.getIds(), Constant.del_flg_on);
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value = {"/api/hospital/restore"})
	public ResponseEntity<?> restoreHandbook(@RequestBody DeleteForm form) {
		log.info("Request to restore hospital with ids: {}",form.getIds());
		try {
			hospitalServiceImpl.updateByStatusAndIds(form.getIds(), Constant.del_flg_off);
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value = "/admin/api/managerHospital/edit")
	public ResponseEntity<?> editHospital(@ModelAttribute HospitalForm form) {
		try {
			hospitalServiceImpl.saveHospital(form);
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok("true");
	}

	@GetMapping(value = "/api/hospital/get-featured-hospital")
	public ResponseEntity<?> getFeaturedHospital() {
		return ResponseEntity.ok(hospitalServiceImpl.getFeaturedHospital());
	}

	@GetMapping(value = "/api/hospital/search-by-name")
	public ResponseEntity<?> searchByName(@RequestParam("query") String query,
										  @PageableDefault(page = 0, size = 10) Pageable pageable) {
		log.info("Request to search hospital by name: {}", query);

		return ResponseEntity.ok(hospitalServiceImpl.searchByNameAndStatus(query, pageable));
	}

	@GetMapping(value = "/api/hospital/get-nearby-hospital")
	public ResponseEntity<?> getNearbyHospital(
			@Param("lat") Double lat,
			@Param("lng") Double lng
	) {
		log.info("lat: {} \n lng: {}", lat, lng);
		List<HospitalDto> hospitalDtos = hospitalServiceImpl.getNearbyHospital(lat, lng);
		return ResponseEntity.ok(hospitalDtos);
	}
}
