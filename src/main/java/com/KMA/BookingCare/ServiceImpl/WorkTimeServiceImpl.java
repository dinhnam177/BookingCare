package com.KMA.BookingCare.ServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.KMA.BookingCare.Repository.HolidayRepository;
import com.KMA.BookingCare.common.GetUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.WorkTimeEntity;
import com.KMA.BookingCare.Mapper.WorkTimeMapper;
import com.KMA.BookingCare.Repository.WorkTimeRepository;
import com.KMA.BookingCare.Service.WorkTimeService;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {

    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @Override
    public List<WorkTimeDto> findAll() {
        List<WorkTimeEntity> entities = workTimeRepository.findAll();
        List<WorkTimeDto> lstDto = new ArrayList<>();
        for (WorkTimeEntity entity : entities) {
            if (Strings.isBlank(entity.getTime()) || Strings.isBlank(entity.getName())) {
                continue;
            }
            WorkTimeDto dto = new WorkTimeDto();
            dto.setId(entity.getId());
            dto.setTime(entity.getTime());
            dto.setName(entity.getName());
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public List<WorkTimeEntity> findByListId(List<Long> id) {
        // TODO Auto-generated method stub
        return workTimeRepository.findAllById(id);
    }

    @Override
    public List<WorkTimeDto> findByDateAndDoctorId(String date, Long id) throws ParseException {
        List<WorkTimeEntity> lstEntity = workTimeRepository.findByDateAndDoctorId(date, id);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        boolean isSameDate = sp.format(currentDate).equals(date);
        List<Long> wkIdInHoliday = holidayRepository.getWorkTimeIdByDateAndDoctorId(id, sp.parse(date));
        Calendar calendar = Calendar.getInstance();
        List<WorkTimeDto> lstDto = new ArrayList<>();
        for (WorkTimeEntity entity : lstEntity) {
            if(wkIdInHoliday.contains(entity.getId())) continue;
            if (!isSameDate || GetUtils.isValidWorkTime(entity.getTime(), calendar)) {
                WorkTimeDto dto = WorkTimeMapper.convertToDto(entity);
                lstDto.add(dto);
            }
        }
        return lstDto;
    }

    @Override
    public WorkTimeDto findOneById(Long id) {
        Optional<WorkTimeEntity> workTimeEntity = workTimeRepository.findById(id);
        if (!workTimeEntity.isPresent()) return new WorkTimeDto();
        return WorkTimeMapper.convertToDto(workTimeEntity.get());
    }

    @Override
    public List<WorkTimeDto> findAllByDoctorId(Long id) {
        List<WorkTimeEntity> entities = workTimeRepository.findAllByDoctorId(id);
        return entities.stream().map(WorkTimeMapper::convertToDto).collect(Collectors.toList());
    }


}
