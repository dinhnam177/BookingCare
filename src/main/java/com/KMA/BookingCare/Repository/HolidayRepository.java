package com.KMA.BookingCare.Repository;

import com.KMA.BookingCare.Entity.HolidayEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

    @Query("SELECT h FROM HolidayEntity AS h WHERE h.user.id = :userId")
    Page<HolidayEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT h FROM HolidayEntity AS h WHERE h.user.id in (:userIds)")
    Page<HolidayEntity> findAllByUserIds(@Param("userIds") List<Long> userIds, Pageable pageable);

    @Query("SELECT count(h) FROM HolidayEntity AS h WHERE DATE_FORMAT(h.date,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d')")
    Long totalHolidayByDate(@Param("date") Date date);

    @Query("SELECT h FROM HolidayEntity AS h WHERE h.id in :ids")
    List<HolidayEntity> findAllByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM HolidayEntity h WHERE h.id in (:ids)")
    void deleteByIds(List<Long> ids);

    @Query("SELECT wk.id FROM HolidayEntity h INNER JOIN h.workTimes AS wk WHERE h.user.id = :doctorId AND DATE_FORMAT(h.date,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d') ")
    List<Long> getWorkTimeIdByDateAndDoctorId(@Param("doctorId") Long doctorId, @Param("date") Date date);

    @Query("SELECT h FROM HolidayEntity h INNER JOIN h.workTimes AS wk WHERE h.user.id in (:doctorIds) AND DATE_FORMAT(h.date,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d')")
    List<HolidayEntity> getAllByDateAndDoctorIdAndWkId(@Param("doctorIds") List<Long> doctorIds, @Param("date") Date date);

    @Modifying
    @Transactional
    @Query("DELETE FROM HolidayEntity h WHERE h.user.id in (:userIds)")
    void deleteAllByUserIds(@Param("userIds") List<Long> userIds);

    @Query("SELECT h FROM HolidayEntity AS h INNER JOIN h.workTimes AS wk " +
            "WHERE DATE_FORMAT(h.date,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d') AND h.user.id in :doctorId " +
            "AND wk.id = :wkId")
    HolidayEntity getOneByDateAndDoctorIdAndWorkTimeId(@Param("date") String date,
                                                       @Param("doctorId") Long doctorId,
                                                       @Param("wkId") Long wkId);
}
