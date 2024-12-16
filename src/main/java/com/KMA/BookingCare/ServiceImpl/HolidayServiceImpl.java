package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.HolidayForm;
import com.KMA.BookingCare.Dto.HolidayDTO;
import com.KMA.BookingCare.Entity.HolidayEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Entity.WorkTimeEntity;
import com.KMA.BookingCare.Mapper.HolidayMapper;
import com.KMA.BookingCare.Repository.HolidayRepository;
import com.KMA.BookingCare.Repository.MedicalExaminationScheduleRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Repository.WorkTimeRepository;
import com.KMA.BookingCare.Service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.springframework.data.domain.Pageable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Autowired
    private MedicalExaminationScheduleRepository mediaRepository;

    @Override
    public Page<HolidayEntity> findAllByUserId(Long userId, Pageable pageable) {
        return holidayRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<HolidayEntity> findAllOfDoctor(Pageable pageable) {
        List<UserEntity> userEntities = userRepository.findAllDoctor();
        List<Long> userIds = userEntities.stream().map(UserEntity::getId).collect(Collectors.toList());
        return holidayRepository.findAllByUserIds(userIds, pageable);
    }

    @Override
    public Page<HolidayEntity> findAllOfDoctorId(Long doctorId, Pageable pageable) {
        return holidayRepository.findAllByUserIds(List.of(doctorId), pageable);
    }

    @Override
    public boolean isExistHolidayByDate(Date date) {
        return holidayRepository.totalHolidayByDate(date) > 0L;
    }

    @Override
    public String isExistMedicalByDateAndWorkTime(HolidayForm form) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String date = sp.format(form.getDate());
        List<Long> workTimeIds = mediaRepository.findAllWorkTimeIdByDateAndDoctorIdAndWorkTimeIdAndStatus(user.getId(), date, form.getWorkTimeIds());
        if(CollectionUtils.isEmpty(workTimeIds)) return "";
        List<WorkTimeEntity> wk = workTimeRepository.findAllById(workTimeIds);
        String errorMsg = "Không thể hoàn thành việc đăng kí ngày nghỉ do các ca khám sau đây đã có người đặt: \n";
        for(WorkTimeEntity entity : wk) {
            errorMsg = errorMsg + entity.getName() + ": " + entity.getTime();
        }
        return errorMsg;
    }

    @Override
    public void save(HolidayForm form) {
        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDate(form.getDate());
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        holidayEntity.setUser(userEntity);
        List<WorkTimeEntity> workTimeEntities = workTimeRepository.findAllById(form.getWorkTimeIds());
        holidayEntity.setWorkTimes(workTimeEntities);
        if(form.getId() != null) {
            holidayEntity.setId(form.getId());
        }
        holidayRepository.save(holidayEntity);
    }

    @Override
    public void delete(DeleteForm form) {
        List<Long> ids = form.getIds().stream().filter(Objects::nonNull).map(Long::parseLong).collect(Collectors.toList());
        List<HolidayEntity> holidayEntities = holidayRepository.findAllByIds(ids);
        for (HolidayEntity entity : holidayEntities) {
            entity.setWorkTimes(Collections.emptyList());
        }
        holidayRepository.deleteByIds(ids);
    }

    @Override
    public Integer getTotal(Page<HolidayEntity> page) {
        return page.getTotalPages();
    }

    @Override
    public List<HolidayDTO> getDTOs(Page<HolidayEntity> page) {
        List<HolidayEntity> holidayEntities = page.getContent();
        if(CollectionUtils.isEmpty(holidayEntities)) return Collections.emptyList();
        return holidayEntities.stream().map(e -> HolidayMapper.convertToDTO(e)).collect(Collectors.toList());
    }
}
