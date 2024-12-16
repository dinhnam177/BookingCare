package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayForm {

    private Long id;

    private Date date;

    private List<Long> workTimeIds;
}
