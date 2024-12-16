package com.KMA.BookingCare.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HitsArrayDto {
    private String  _index;
    private String _id;
    private String _score;
    private String _type;
    private SearchAllDto _source;

}
