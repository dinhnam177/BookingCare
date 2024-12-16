package com.KMA.BookingCare.Service;

import com.KMA.BookingCare.Dto.HitsArrayDto;
import com.KMA.BookingCare.Dto.SearchAllDto;
import com.KMA.BookingCare.Dto.SearchHitDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ElasticSearchService {
    void syncData() throws ParseException;

    List<SearchAllDto> searchAll(String query) throws IOException;
}
