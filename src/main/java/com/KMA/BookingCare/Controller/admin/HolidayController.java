package com.KMA.BookingCare.Controller.admin;

import com.KMA.BookingCare.Dto.HolidayDTO;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.HolidayEntity;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Service.HolidayService;
import com.KMA.BookingCare.Service.WorkTimeService;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private WorkTimeService workTimeService;


    @GetMapping(value = { "/doctor/managerHoliday"})
    public String getHoliday(Model model, @RequestParam(required = false,name = "page",defaultValue = "1") Integer page) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        MyUser userDetails = UserMapper.convertToMyUser(user);
        List<WorkTimeDto> workTimeDTOS = workTimeService.findAllByDoctorId(userDetails.getId());
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<HolidayEntity> pageResult = holidayService.findAllOfDoctorId(userDetails.getId() ,pageable);
        List<HolidayDTO> holidayDTOS = holidayService.getDTOs(pageResult);
        Integer total = holidayService.getTotal(pageResult);
        model.addAttribute("holidays", holidayDTOS);
        model.addAttribute("total", total);
        model.addAttribute("currentPage", page);
        model.addAttribute("holidays", holidayDTOS);
        model.addAttribute("workTimes", workTimeDTOS);
        return "admin/views/managerHoliday";
    }

    @GetMapping(value = {"/admin/managerHoliday"})
    public String getHolidayAdmin(Model model, @RequestParam(required = false,name = "page",defaultValue = "1") Integer page) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page - 1, 5);
        List<WorkTimeDto> workTimeDTOS = workTimeService.findAll();
        Page<HolidayEntity> pageResult = holidayService.findAllOfDoctor(pageable);
        List<HolidayDTO> holidayDTOS = holidayService.getDTOs(pageResult);
        Integer total = holidayService.getTotal(pageResult);
        model.addAttribute("holidays", holidayDTOS);
        model.addAttribute("total", total);
        model.addAttribute("currentPage", page);
        model.addAttribute("workTimes", workTimeDTOS);
        return "admin/views/managerHolidayAdmin";
    }
}
