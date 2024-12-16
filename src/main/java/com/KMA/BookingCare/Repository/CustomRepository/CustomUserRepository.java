package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.SpecialtyFeaturedDto;
import com.KMA.BookingCare.Dto.UserFeaturedDto;

import java.util.List;

public interface CustomUserRepository {
    List<UserFeaturedDto> getFeaturedUser();
}
