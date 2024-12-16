package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.HandbookForm;
import com.KMA.BookingCare.Dto.HandbookDto;
import com.KMA.BookingCare.Repository.HandbookRepository;
import com.KMA.BookingCare.Service.HandbookService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController()
//@RequestMapping("")
public class HandbookApi {

	private final Logger log = LoggerFactory.getLogger(HandbookApi.class);
	
	@Autowired
	private HandbookService handbookServiceImpl;

	@Autowired
	private HandbookRepository handbookRepository;

	@PostMapping(value = "/api/handbook")
	public ResponseEntity<?> addHandbookApi(@RequestBody HandbookForm form) {
		log.info("Request to add handbook {}");
		try {
			handbookServiceImpl.saveHandbook(form);
			System.out.println("oke");
			
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping(value = "/api/handbok")
	public ResponseEntity<?> editHandbookApi(@RequestBody HandbookForm form) {
		log.info("Request to edit handbook {}");
		try {
			handbookServiceImpl.saveHandbook(form);
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "/api/handbook")
	public ResponseEntity<Page<HandbookDto>> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable){
		log.info("Request to getAll {}");
		Page<HandbookDto> page = handbookServiceImpl.findAllByStatusPageable(1, pageable);
		return ResponseEntity.ok(page);
	}

	/*
	* hien thi len trang chu
	*  */
	@GetMapping(value = "/api/handbook/get-random")
	public ResponseEntity<List<HandbookDto>> getRandom(){
		log.info("Request to get Random");
		List<HandbookDto> lstCamNangRandom = handbookServiceImpl.findRandomHandbook();
		return ResponseEntity.ok(lstCamNangRandom);
	}

	@GetMapping(value = "/api/handbook/search")
	public ResponseEntity<Page<HandbookDto>> search(@PageableDefault(page = 0, size = 10) Pageable pageable
											, @RequestParam(required = false) String title,
													@RequestParam(required = false) Long specialzedId,
													HttpSession httpSession){
		log.info("Request to search Handbook {}");
		UserDetails userDetails = null;
		boolean isDoctor = false;
		Object result = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (result != null && !result.equals("anonymousUser")) {
			userDetails = (UserDetails) result;
			isDoctor = userDetails.getAuthorities().stream().anyMatch(item -> "ROLE_DOCTOR".equals(item.getAuthority()));
		}

		if(isDoctor) {
			Page<HandbookDto> page= handbookServiceImpl.searchHandbookAndPageableapi(title,specialzedId, userDetails.getUsername(),pageable);
			return  ResponseEntity.ok(page);
		}else{
			Page<HandbookDto> page= handbookServiceImpl.searchHandbookAndPageableapi(title,specialzedId,null,pageable);
			return  ResponseEntity.ok(page);
		}

	}

	@GetMapping(value = "/api/handbook/{id}")
	public ResponseEntity<HandbookDto> getOne(@PathVariable long id){
		log.info("Request to getOneById {}", id);
		HandbookDto dto = handbookServiceImpl.findOneByIdApi(id);
		return ResponseEntity.ok(dto);
	}

	@Hidden
	@DeleteMapping("/api/handbook/{id}")
	public ResponseEntity<?> delete(@PathVariable long id){
		log.info("Request to delete {}", id);
		handbookRepository.deleteById(id);
		return ResponseEntity
				.noContent()
				.build();
	}
	@Hidden
	@DeleteMapping("/api/handbook/deletes")
	public ResponseEntity<?> deleteALl(@RequestBody List<Long> ids){
		log.info("Request to delete multi {}", ids);
		handbookRepository.deleteAllById(ids);
		return ResponseEntity
				.noContent()
				.build();
	}

	@PostMapping(value = {"/admin/managerHandbook/add","/doctor/managerHandbook/add"})
	public ResponseEntity<?> addHandbook(@ModelAttribute HandbookForm form) {
		try {
			handbookServiceImpl.saveHandbook(form);
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("looxi");
		}
		return ResponseEntity.ok("true");
	}

	@PostMapping(value = {"/admin/managerHandbook/uDelete","/doctor/managerHandbook/uDelete"})
	public ResponseEntity<?> uDeleteHandbook(@RequestBody DeleteForm form) {
		try {
			handbookServiceImpl.updateHandbookByStatus(form.getIds(), Constant.del_flg_on);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("true");
	}

	@PostMapping(value = {"/api/handbook/restore"})
	public ResponseEntity<?> restoreHandbook(@RequestBody DeleteForm form) {
		boolean isValid = handbookServiceImpl.isValidSpecicalty(form);
		if(!isValid)
			return ResponseEntity.badRequest().body("Vẫn còn chuyên khoa đang trong trong thái không sẵn sàng, vui lòng kiểm tra lại");
		try {
			handbookServiceImpl.updateHandbookByStatus(form.getIds(), Constant.del_flg_off);
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Có lỗi xảy ra xin vui lòng thử lại");
		}
	}

	@PostMapping(value = {"/api/handbook/delete"})
	public ResponseEntity<?> deleteHandbook(@RequestBody DeleteForm form) {
		try {
			handbookServiceImpl.deleteHandbook(form);
			return ResponseEntity.ok("true");
		} catch (Exception e ) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value = {"/admin/managerHandbook/edit","/doctor/managerHandbook/edit"})
	public ResponseEntity<?> editHandbook(@ModelAttribute HandbookForm form) {
		try {
			handbookServiceImpl.saveHandbook(form);
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("looxi");
		}
		return ResponseEntity.ok("true");
	}

	@GetMapping(value = "/api/handbook/get-list-of-recent")
	public ResponseEntity<?> getListOfRecentHandbook() {
		return ResponseEntity.ok(handbookServiceImpl.getListOfRecentHandbook());
	}

	@GetMapping(value = "/api/handbook/get-featured-handbook")
	public ResponseEntity<?> getFeaturedHandbook() {
		return ResponseEntity.ok(handbookServiceImpl.getFeaturedHandbook());
	}
}
