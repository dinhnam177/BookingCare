package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordForm {

    private String userName;

    private String email;

    private String key;

    private String password;
}
