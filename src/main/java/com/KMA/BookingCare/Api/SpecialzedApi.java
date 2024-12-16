package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.SpecializedForm;
import com.KMA.BookingCare.Dto.SpecializedDto;
import com.KMA.BookingCare.Repository.SpecializedRepository;
import com.KMA.BookingCare.Service.SpecializedService;
import com.KMA.BookingCare.common.Constant;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SpecialzedApi {

	private final Logger log = LoggerFactory.getLogger(SpecialzedApi.class);
	
	@Autowired
	private SpecializedService specializedServiceImpl;

	@Autowired
	private SpecializedRepository specializedRepository;

	@Hidden
	@PostMapping(value = "/specialized")
	public ResponseEntity<?> addHospital(@ModelAttribute SpecializedForm form) {
		log.info("Request to add specialed {}");
		try {
			specializedServiceImpl.saveOrUpdateSpecialized(form);

		} catch (Exception e ) {
			log.info(e.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping(value = "/specialized/update")
	public ResponseEntity<?> editHospital(@ModelAttribute SpecializedForm form) {
		log.info("Request to update specialed");
		try {
			specializedServiceImpl.saveOrUpdateSpecialized(form);
		} catch (Exception e ) {
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "/specicalized")
	public ResponseEntity<Page<SpecializedDto>> getAll(@PageableDefault(page = 0, size = 6) Pageable pageable){
		log.info("Request to getAll {}");
		Page<SpecializedDto> lstChuyenKhoa = specializedServiceImpl.findAllByStatusApi(1, pageable);
		return  ResponseEntity.ok(lstChuyenKhoa);
	}

	/*
	 * hien thi len trang chu
	 *  */
	@GetMapping(value = "/specicalized/get-random")
	public ResponseEntity<List<SpecializedDto>> getRandom(){
		log.info("Request to get Random specicalized");
		List<SpecializedDto> lstChuyenKhoa = specializedServiceImpl.findRandomSpecicalized();
		return ResponseEntity.ok(lstChuyenKhoa);
	}

	@Hidden
	@DeleteMapping(value = "/specicalized/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		log.info("Request to delete {}", id);
		specializedRepository.deleteById(id);
		return  ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/specicalized/delete")
	public ResponseEntity<?> deletes(@RequestBody DeleteForm form){
		log.info("Request to deletes by ids {}", form.getIds());
		specializedServiceImpl.delete(form.getIds());
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = {"/specicalized/restore"})
	public ResponseEntity<?> restoreHandbook(@RequestBody DeleteForm form) {
		try {
			specializedServiceImpl.updateByStatusAndIds(form.getIds(), Constant.del_flg_off);
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value = {"/specicalized/uDelete"})
	public ResponseEntity<?> uDeleteSpecialzed(@RequestBody DeleteForm form) {
		boolean isExist = specializedServiceImpl.isExistItemRelationWithSpecialIsUsing(form.getIds());
		if (isExist) {
			return ResponseEntity.badRequest().body("Không thể xoá do vẫn còn bài viết hoặc người dùng đang đuợc sử dụng");
		}
		try {
			specializedServiceImpl.updateByStatusAndIds(form.getIds(), Constant.del_flg_on);
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(value = "/specicalized/get-featured-specialty")
	public ResponseEntity<?> getFeaturedSpecialty() {
		return ResponseEntity.ok(specializedServiceImpl.getFeaturedSpecialty());
	}

	@GetMapping(value = "/specicalized/{id}")
	public ResponseEntity<?> getOne(@PathVariable Long id){
		log.info("Request to get one Specialty by id {}", id);
		return  ResponseEntity.ok(specializedServiceImpl.findOneById(id));
	}
}
