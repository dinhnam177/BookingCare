package com.KMA.BookingCare.Dto;

import lombok.Data;

import java.util.List;

@Data
public class OverviewReportDto {

    private List<Series> series;

    private Xaxis xaxis;

    private String charType;

    private List<String> labels;
}
