package com.KMA.BookingCare.Service;

import com.KMA.BookingCare.Dto.SearchFullTextDto;

import java.util.List;

public interface SearchAllService {

    List<SearchFullTextDto> searchAllByFullText(String query);
}
