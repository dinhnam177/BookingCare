package com.KMA.BookingCare.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SearchFullTextDto {

    private Long id;

    private String title;

    private String description;

    private String img;

    private String tableName;

}
