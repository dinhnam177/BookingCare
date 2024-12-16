package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class UpdateCientForm {
	private String fullName;

	private String email;

	private String phone;

	private String passwod;

	private MultipartFile img;


}
