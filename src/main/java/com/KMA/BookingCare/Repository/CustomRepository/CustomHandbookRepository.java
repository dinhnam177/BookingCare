package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.HandbookFeaturedDto;
import com.KMA.BookingCare.Dto.UserFeaturedDto;

import java.util.List;

public interface CustomHandbookRepository {
    List<HandbookFeaturedDto> getFeaturedHandbook();
}
