package com.KMA.BookingCare.Mapper;

import com.KMA.BookingCare.Dto.HolidayDTO;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.HolidayEntity;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HolidayMapper {

    public static HolidayDTO convertToDTO(HolidayEntity entity) {
        HolidayDTO dto = new HolidayDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        User user = UserMapper.convertToDto(entity.getUser());
        dto.setUser(user);
        if(!CollectionUtils.isEmpty(entity.getWorkTimes())) {
            List<WorkTimeDto> workTimeDtos = entity.getWorkTimes().stream().map(e -> WorkTimeMapper.convertToDto(e)).collect(Collectors.toList());
            dto.setWorkTimeDTOs(workTimeDtos);
        }
        return dto;
    }
}
