package com.KMA.BookingCare.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchHitDto {
    private Long took;
    private Boolean timed_out;
    private Object _shards;
    private HitsDto hits;
}
