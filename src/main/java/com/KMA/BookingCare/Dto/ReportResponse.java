package com.KMA.BookingCare.Dto;

import lombok.Data;

@Data
public class ReportResponse {

    private Long totalMedical;

    private Long totalMedicalCancel;

    private Long totalMedicalComplete;

    private Long totalMedicalWait;

    private Long totalPrice;

    private OverviewReportDto bar;

    private OverviewReportDto area;

    private OverviewReportDto donut;

    private TableReportDto table;
}
