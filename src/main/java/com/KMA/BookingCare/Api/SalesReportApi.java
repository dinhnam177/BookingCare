package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.SalesReportFrom;
import com.KMA.BookingCare.Dto.ReportResponse;
import com.KMA.BookingCare.Service.SaleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SalesReportApi {

    @Autowired
    private SaleReportService saleReportService;

    @PostMapping("/api/sale-report")
    public ResponseEntity<?> getSealReport(@RequestBody SalesReportFrom form) {
        System.out.println("call");
        ReportResponse reportResponse = saleReportService.getSaleReport(form);
        return ResponseEntity.ok(reportResponse);
    }
}
