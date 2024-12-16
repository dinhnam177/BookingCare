package com.KMA.BookingCare.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserFeaturedDto {
    private Long id;

    private Long total;
}
