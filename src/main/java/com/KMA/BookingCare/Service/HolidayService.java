package com.KMA.BookingCare.Service;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.HolidayForm;
import com.KMA.BookingCare.Dto.HolidayDTO;

import com.KMA.BookingCare.Entity.HolidayEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;

public interface HolidayService {
    Page<HolidayEntity> findAllByUserId(Long userId, Pageable pageable);

    Page<HolidayEntity> findAllOfDoctor(Pageable pageable);

    Page<HolidayEntity> findAllOfDoctorId(Long doctorId, Pageable pageable);

    boolean isExistHolidayByDate(Date date);

    String isExistMedicalByDateAndWorkTime(HolidayForm form);

    void save(HolidayForm form);

    void delete(DeleteForm form);

    Integer getTotal(Page<HolidayEntity> page);

    List<HolidayDTO> getDTOs(Page<HolidayEntity> page);
}
