package com.KMA.BookingCare.Controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping(value = {"admin/salesReport", "doctor/salesReport"})
    public String saleReport() {
        return "admin/views/salesReport";
    }
}
