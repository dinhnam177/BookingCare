package com.KMA.BookingCare.Controller.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Service.WorkTimeService;
import com.KMA.BookingCare.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.KMA.BookingCare.Dto.MedicalExaminationScheduleDto;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Service.MedicalExaminationScheduleService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MedicalExaminationSheduleEntityController {

    @Autowired
    private MedicalExaminationScheduleService medicalServiceImpl;
    @Autowired
    private WorkTimeService workTimeServiceImpl;

    @GetMapping(value = {"/admin/managerMedical", "/doctor/managerMedical"})
    public String medicalPage(Model model, HttpSession session,
                              @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        MyUser user = (MyUser) session.getAttribute("userDetails");
        List<WorkTimeDto> lstWorkTime = workTimeServiceImpl.findAll();
        List<MedicalExaminationScheduleDto> lstMedical;
        Integer totalPage;
        if (user.getRoles().contains("ROLE_DOCTOR")) {
            lstMedical = medicalServiceImpl.findAllByDoctorIdAndStatusPage(user.getId(), Constant.MEDICAL_SCHEDULE_IS_WAITING, pageable);
            totalPage = medicalServiceImpl.getTotalByDoctorIdAndStatusPage(user.getId(), Constant.MEDICAL_SCHEDULE_IS_WAITING, pageable);
        } else {
            lstMedical = medicalServiceImpl.findAllByStatusPage(Constant.MEDICAL_SCHEDULE_IS_WAITING, pageable);
            totalPage = medicalServiceImpl.getToTalByStatusPage(Constant.MEDICAL_SCHEDULE_IS_WAITING, pageable);
        }

        model.addAttribute("lstMedical", lstMedical);
        model.addAttribute("lstWorkTime", lstWorkTime);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        return "admin/views/managerMedical";
    }

    @GetMapping(value = {"/admin/managerMedicalComplete", "/doctor/managerMedicalComplete"})
    public String medicalConpletePage(Model model, HttpSession session,
                                      @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        MyUser user = (MyUser) session.getAttribute("userDetails");
        Pageable pageable = PageRequest.of(page - 1, 10);
        List<MedicalExaminationScheduleDto> lstMedical;
        Integer totalPage;
        if (user.getRoles().contains("ROLE_DOCTOR")) {
            lstMedical = medicalServiceImpl.findAllByDoctorIdAndStatusPage(user.getId(), Constant.MEDICAL_SCHEDULE_IS_COMPLETE, pageable);
            totalPage = medicalServiceImpl.getTotalByDoctorIdAndStatusPage(user.getId(), Constant.MEDICAL_SCHEDULE_IS_COMPLETE, pageable);
        } else {
            lstMedical = medicalServiceImpl.findAllByStatusPage(Constant.MEDICAL_SCHEDULE_IS_COMPLETE, pageable);
            totalPage = medicalServiceImpl.getToTalByStatusPage(Constant.MEDICAL_SCHEDULE_IS_COMPLETE, pageable);
        }

        model.addAttribute("lstMedical", lstMedical);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        return "admin/views/managerMedicalComplete";
    }

    @GetMapping(value = {"/admin/managerMedicalCancel", "/doctor/managerMedicalCancel"})
    public String medicalCancelPage(Model model, HttpSession session,
                                    @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        MyUser user = (MyUser) session.getAttribute("userDetails");
        Pageable pageable = PageRequest.of(page - 1, 10);
        List<MedicalExaminationScheduleDto> lstMedical;
        Integer totalPage;
        if (user.getRoles().contains("ROLE_DOCTOR")) {
            lstMedical = medicalServiceImpl.findAllByDoctorIdAndStatusPage(user.getId(), Constant.MEDICAL_SCHEDULE_IS_CANCEL, pageable);
            totalPage = medicalServiceImpl.getTotalByDoctorIdAndStatusPage(user.getId(), Constant.MEDICAL_SCHEDULE_IS_CANCEL, pageable);
        } else {
            lstMedical = medicalServiceImpl.findAllByStatus(Constant.MEDICAL_SCHEDULE_IS_CANCEL);
            totalPage = medicalServiceImpl.getToTalByStatusPage(Constant.MEDICAL_SCHEDULE_IS_CANCEL, pageable);
        }

        model.addAttribute("lstMedical", lstMedical);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);
        return "admin/views/managerMedicalCancel";
    }
}
