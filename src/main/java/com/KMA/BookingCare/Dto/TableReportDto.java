package com.KMA.BookingCare.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableReportDto {
    private List<RecordTableReportDto> records;
}
