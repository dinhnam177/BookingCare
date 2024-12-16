package com.KMA.BookingCare.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer vnpAmount;

    private String vnpBankCode;

    private String vnpBankTranNo;

    private String vnpCardType;

    private String vnpOrderInfo;

    private String vnpPayDate;

    private String vnpResponseCode;

    private String vnpSecureHash;

    private String vnpSecureHashType;

    private String vnpTmnCode;

    private String vnpTransactionNo;

    private String vnpTxnRef;

    private Long createdBy;

    private Date createdDate;

    @OneToOne(mappedBy = "payment")
    private MedicalExaminationScheduleEntity medicalExaminationSchedule;
}

