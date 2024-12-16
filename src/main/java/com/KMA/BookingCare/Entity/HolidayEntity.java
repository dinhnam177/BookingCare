package com.KMA.BookingCare.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "holiday")
public class HolidayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "holiday_workTime",
            joinColumns = @JoinColumn(name="holiday_id"),
            inverseJoinColumns = @JoinColumn(name="workTime_id"))
    private List<WorkTimeEntity> workTimes;
}
