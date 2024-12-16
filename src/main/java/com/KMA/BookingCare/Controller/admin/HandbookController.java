package com.KMA.BookingCare.Controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.KMA.BookingCare.Api.form.searchHandbookForm;
import com.KMA.BookingCare.Dto.HandbookDto;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Dto.SpecializedDto;
import com.KMA.BookingCare.Service.HandbookService;
import com.KMA.BookingCare.Service.InteractiveService;
import com.KMA.BookingCare.Service.SpecializedService;

@Controller
public class HandbookController {
	
	@Autowired
	private SpecializedService specializedServiceImpl;
	
	@Autowired
	private HandbookService handbookSeviceImpl;
	
	@Autowired
	private InteractiveService interactiveServiceImpl;
	
	@GetMapping(value = {"/admin/managerHandbook","/doctor/managerHandbook"})
	public String managerUerPage( Model model,HttpSession session,@RequestParam(required = false,name = "page",defaultValue = "1") Integer page,
			@ModelAttribute searchHandbookForm form) {
		Pageable pageable = PageRequest.of(page-1, 3);
		List<SpecializedDto> lstSpecialized= specializedServiceImpl.findAll();
		MyUser user = (MyUser) session.getAttribute("userDetails");
		List<HandbookDto> lstHandbook= new ArrayList<HandbookDto>();
		Integer totalPage = 0;
		if(user.getRoles().contains("ROLE_DOCTOR")) {
			 lstHandbook= handbookSeviceImpl.searchHandbookAndPageable(form, user.getId(),pageable);
			 totalPage = handbookSeviceImpl.getTotalHandbookAndPageable(form, user.getId(),pageable);
		}else {
			 lstHandbook = handbookSeviceImpl.searchHandbookAndPageable(form, null,pageable);
			 totalPage = handbookSeviceImpl.getTotalHandbookAndPageable(form, null,pageable);
		}

		model.addAttribute("lstSpecialized", lstSpecialized);
		model.addAttribute("lstHandbook",lstHandbook);
		model.addAttribute("formSearch",form);
		Integer curentPage=page;
		model.addAttribute("curentPage", curentPage);
		model.addAttribute("totalPage", totalPage);
		return "admin/views/managerHandbook";
	}

	@GetMapping(value = {"/admin/managerUDeleteHandbook","/doctor/managerUDeleteHandbook"})
	public String managerHandbookUDelkete( Model model,HttpSession session,
										   @RequestParam(required = false,name = "page",defaultValue = "1") Integer page,
								  			@ModelAttribute searchHandbookForm form) {
		Pageable pageable = PageRequest.of(page - 1, 3);
		List<SpecializedDto> lstSpecialized= specializedServiceImpl.findAll();
		MyUser user = (MyUser) session.getAttribute("userDetails");
		List<HandbookDto> lstHandbook;
		Integer totalPage = 0;
		if(user.getRoles().contains("ROLE_DOCTOR")) {
			lstHandbook= handbookSeviceImpl.searchHandbookUDeleteAndPageable(form, user.getId(),pageable);
			totalPage = handbookSeviceImpl.getTotalHandbookUDeleteAndPageable(form, user.getId(),pageable);
		}else {
			lstHandbook= handbookSeviceImpl.searchHandbookUDeleteAndPageable(form, null,pageable);
			totalPage = handbookSeviceImpl.getTotalHandbookUDeleteAndPageable(form, null,pageable);
		}
		model.addAttribute("lstSpecialized", lstSpecialized);
		model.addAttribute("lstHandbook",lstHandbook);
		model.addAttribute("formSearch",form);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", totalPage);
		return "admin/views/managerHandbookUDelete";
	}

}
