package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentForm {

    private Integer amount;

    private String bankCode;

    private Long medicalId;
}
