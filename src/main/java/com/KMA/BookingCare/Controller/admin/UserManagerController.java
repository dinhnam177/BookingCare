package com.KMA.BookingCare.Controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.KMA.BookingCare.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.KMA.BookingCare.Api.form.UserForm;
import com.KMA.BookingCare.Api.form.searchDoctorForm;
import com.KMA.BookingCare.Service.HospitalService;
import com.KMA.BookingCare.Service.RoleService;
import com.KMA.BookingCare.Service.SpecializedService;
import com.KMA.BookingCare.Service.UserService;
import com.KMA.BookingCare.Service.WorkTimeService;

import io.netty.handler.codec.http.HttpResponse;

@Controller
public class UserManagerController {

    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private HospitalService hospitalServiceImpl;
    @Autowired
    private SpecializedService specializedServiceImpl;
    @Autowired
    private WorkTimeService workTimeServiceImpl;
    @Autowired
    private RoleService roleSeviceImpl;

    @GetMapping(value = "/admin/managerUser")
    public String managerUerPage(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") Integer page,
                                 @ModelAttribute searchDoctorForm form) {
        Pageable pageable = PageRequest.of(page - 1, 3);
        List<User> lstUser = userServiceImpl.searchDoctorAndPageable(form, "ADMIN", pageable, Constant.del_flg_off);
        Integer totalPage = userServiceImpl.searchTotalPageDoctorAndPageable(form, "ADMIN", pageable, Constant.del_flg_off);
        List<HospitalDto> lstHospital = hospitalServiceImpl.findAll();
        List<SpecializedDto> lstSpecialized = specializedServiceImpl.findAll();
        List<WorkTimeDto> lstWorkTime = workTimeServiceImpl.findAll();
        List<Role> lstRole = roleSeviceImpl.findAll();
        model.addAttribute("lstRole", lstRole);
        model.addAttribute("lstWorkTime", lstWorkTime);
        model.addAttribute("lstHospital", lstHospital);
        model.addAttribute("lstSpecialized", lstSpecialized);
        model.addAttribute("lstUser", lstUser);
        model.addAttribute("formSearch", form);
        model.addAttribute("curentPage", page);
        model.addAttribute("totalPage", totalPage);
        return "admin/views/managerUser";
    }

    @GetMapping(value = "/admin/managerUDeleteUser")
    public String managerUDeleteUerPage(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") Integer page,
                                 @ModelAttribute searchDoctorForm form) {
        Pageable pageable = PageRequest.of(page - 1, 3);
        List<User> lstUser = userServiceImpl.searchDoctorAndPageable(form, "ADMIN", pageable, Constant.del_flg_on);
        Integer totalPage = userServiceImpl.searchTotalPageDoctorAndPageable(form, "ADMIN", pageable, Constant.del_flg_on);
        List<HospitalDto> lstHospital = hospitalServiceImpl.findAll();
        List<SpecializedDto> lstSpecialized = specializedServiceImpl.findAll();
        List<WorkTimeDto> lstWorkTime = workTimeServiceImpl.findAll();
        List<Role> lstRole = roleSeviceImpl.findAll();
        model.addAttribute("lstRole", lstRole);
        model.addAttribute("lstWorkTime", lstWorkTime);
        model.addAttribute("lstHospital", lstHospital);
        model.addAttribute("lstSpecialized", lstSpecialized);
        model.addAttribute("lstUser", lstUser);
        model.addAttribute("formSearch", form);
        model.addAttribute("curentPage", page);
        model.addAttribute("totalPage", totalPage);
        return "admin/views/managerUserUDelete";
    }

    @GetMapping(value = {"/admin/editProfile", "/doctor/editProfile"})
    public String profilePage(Model model, HttpSession session) {
        MyUser userDetails = (MyUser) session.getAttribute("userDetails");
        User userDto = userServiceImpl.findOneById(userDetails.getId());
        model.addAttribute("userDto", userDto);
        List<HospitalDto> lstHospital = hospitalServiceImpl.findAll();
        List<SpecializedDto> lstSpecialized = specializedServiceImpl.findAll();
        List<WorkTimeDto> lstWorkTime = workTimeServiceImpl.findAll();
        model.addAttribute("lstWorkTime", lstWorkTime);
        model.addAttribute("lstHospital", lstHospital);
        model.addAttribute("lstSpecialized", lstSpecialized);
        return "admin/views/editProfile";
    }
    @PostMapping(value = {"/admin/editProfile", "/doctor/editProfile"})
    public ResponseEntity<?> editProfilePage(Model model, @ModelAttribute UserForm form) {
        try {
            userServiceImpl.saveDoctor(form);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("true");
    }
}
