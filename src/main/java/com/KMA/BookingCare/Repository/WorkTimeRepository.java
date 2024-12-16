package com.KMA.BookingCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.KMA.BookingCare.Entity.HospitalEntity;
import com.KMA.BookingCare.Entity.SpecializedEntity;
import com.KMA.BookingCare.Entity.WorkTimeEntity;

public interface WorkTimeRepository extends JpaRepository<WorkTimeEntity, Long> {

    @Query(value = "SELECT wt.id, wt.name, wt.time " +
            "FROM work_time wt " +
            "INNER JOIN " +
            "user_work_time uwt " +
            "on wt.id = uwt.work_time_id " +
            "WHERE uwt.user_id = :doctorId AND wt.id not in  " +
            "( " +
            "SELECT work_time_id as wki FROM medical_examination_schedule as m where date =:date  and m.doctor_id= :doctorId " +
            "AND m.status = 1 )", nativeQuery = true)
    List<WorkTimeEntity> findByDateAndDoctorId(@Param("date") String date, @Param("doctorId") Long idDoctor);

    @Query(value = "SELECT w FROM WorkTimeEntity w INNER JOIN w.userEntities u WHERE u.id = :doctorId")
    List<WorkTimeEntity> findAllByDoctorId(@Param("doctorId") Long doctorId);

}
