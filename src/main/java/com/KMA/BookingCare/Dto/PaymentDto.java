package com.KMA.BookingCare.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDto {

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
}
