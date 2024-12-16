package com.KMA.BookingCare.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationChangeTimeDTO {

    private String date;

    private String workTime;

    private String hospitalName;

    private String oldDoctorName;

    private String olrDoctorEmail;

    private String newDoctorName;

    private String newDoctorEmail;

    private String userName;

    private String userEmail;


}
