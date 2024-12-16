package com.KMA.BookingCare.Service;

import com.KMA.BookingCare.Api.form.SalesReportFrom;
import com.KMA.BookingCare.Dto.ReportResponse;

import java.util.Map;

public interface SaleReportService {
    ReportResponse getSaleReport(SalesReportFrom form);
}
