package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Dto.HospitalFeaturedDto;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomHospitalRepository {
    List<HospitalFeaturedDto> getFeaturedHospital();
}
