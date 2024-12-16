package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadMedicalRecordsForm {

    private MultipartFile medicalRecords;

    private Long medicalId;
}
