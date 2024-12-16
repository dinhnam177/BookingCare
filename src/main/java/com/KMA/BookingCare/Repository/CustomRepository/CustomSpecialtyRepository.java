package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.HospitalFeaturedDto;
import com.KMA.BookingCare.Dto.SpecialtyFeaturedDto;

import java.util.List;

public interface CustomSpecialtyRepository {
    List<SpecialtyFeaturedDto> getFeaturedSpecialty();
}
