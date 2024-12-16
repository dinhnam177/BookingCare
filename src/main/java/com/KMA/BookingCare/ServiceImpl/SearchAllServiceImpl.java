package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Dto.SearchFullTextDto;
import com.KMA.BookingCare.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchAllServiceImpl implements SearchAllService {

    @Autowired
    private UserService userService;

    @Autowired
    private HandbookService handbookService;

    @Autowired
    private SpecializedService specializedService;

    @Autowired
    private HospitalService hospitalService;

    @Override
    public List<SearchFullTextDto> searchAllByFullText(String query) {
        List<SearchFullTextDto> users = userService.searchAllByFullText(query);
        List<SearchFullTextDto> specialties = specializedService.searchAllByFullText(query);
        List<SearchFullTextDto> handbooks = handbookService.searchAllByFullText(query);
        List<SearchFullTextDto> hospitals = hospitalService.searchAllByFullText(query);
        List<SearchFullTextDto> result = new ArrayList<>();
        result.addAll(users);
        result.addAll(handbooks);
        result.addAll(hospitals);
        result.addAll(specialties);
        return result;
    }
}
