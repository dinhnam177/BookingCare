package com.KMA.BookingCare.Controller.admin;

import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Service.HospitalService;
import com.KMA.BookingCare.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HospitalController {

    @Autowired
    private HospitalService hospitalServiceImpl;

    @GetMapping(value = "/admin/managerHospital")
    public String hospitalPage(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 3);
        List<HospitalDto> lstHospital = hospitalServiceImpl.findAllByStatus(Constant.del_flg_off, pageable);
        Integer totalPage = hospitalServiceImpl.getTotalByStatus(Constant.del_flg_off, pageable);
        model.addAttribute("lstHospital", lstHospital);
        model.addAttribute("totalPage", totalPage);
        Integer curentPage = page;
        model.addAttribute("curentPage", curentPage);
        return "admin/views/managerHospital";
    }

    @GetMapping(value = "/admin/managerUDeleteHospital")
    public String uDeleteHospitalPage(Model model,
                                      @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 3);
        List<HospitalDto> lstHospital = hospitalServiceImpl.findAllByStatus(Constant.del_flg_on, pageable);
        Integer totalPage = hospitalServiceImpl.getTotalByStatus(Constant.del_flg_on, pageable);
        model.addAttribute("lstHospital", lstHospital);
        model.addAttribute("totalPage", totalPage);
        Integer curentPage = page;
        model.addAttribute("curentPage", curentPage);
        return "admin/views/managerHospitalUDelete";
    }
}
