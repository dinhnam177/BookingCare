package com.KMA.BookingCare.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class RecordTableReportDto {

    private Long id;

    private String title;

    private Long totalSchedule;

    private Long price;
}
