package com.KMA.BookingCare.Controller.admin;

import com.KMA.BookingCare.Dto.SpecializedDto;
import com.KMA.BookingCare.Service.SpecializedService;
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
public class SpecializedController {

    @Autowired
    private SpecializedService specializedServiceImpl;

    @GetMapping(value = "/admin/managerSpecialized")
    public String specializedPage(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 3);
        List<SpecializedDto> lstSpecialized = specializedServiceImpl.findAllByStatus(Constant.del_flg_off, pageable);
        Integer totalPage = specializedServiceImpl.getTotalByStatus(Constant.del_flg_off, pageable);
        model.addAttribute("lstSpecialized", lstSpecialized);
        model.addAttribute("curentPage", page);
        model.addAttribute("totalPage", totalPage);
        return "admin/views/managerSpecialized";
    }

    @GetMapping(value = "/admin/managerUDeleteSpecialized")
    public String uDeleteSpecializedPage(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 3);
        List<SpecializedDto> lstSpecialized = specializedServiceImpl.findAllByStatus(Constant.del_flg_on, pageable);
        Integer totalPage = specializedServiceImpl.getTotalByStatus(Constant.del_flg_on, pageable);
        model.addAttribute("lstSpecialized", lstSpecialized);
        model.addAttribute("curentPage", page);
        model.addAttribute("totalPage", totalPage);
        return "admin/views/managerSpecializedUDelete";
    }
}
