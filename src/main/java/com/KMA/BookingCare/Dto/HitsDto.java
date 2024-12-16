package com.KMA.BookingCare.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HitsDto {

    private Object total;

    private Object max_score;

    private List<HitsArrayDto> hits;

}
