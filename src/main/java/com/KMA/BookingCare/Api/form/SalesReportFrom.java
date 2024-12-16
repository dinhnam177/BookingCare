package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesReportFrom {

    private Integer typeReport;

    private Integer timeReport;

    private LocalDate startTimeReport;

    private LocalDate endTimeReport;
}
