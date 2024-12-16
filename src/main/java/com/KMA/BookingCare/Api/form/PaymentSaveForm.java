package com.KMA.BookingCare.Api.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentSaveForm {

    @JsonProperty("vnp_Amount")
    private Integer vnpAmount;

    @JsonProperty("vnp_BankCode")
    private String vnpBankCode;

    @JsonProperty("vnp_BankTranNo")
    private String vnpBankTranNo;

    @JsonProperty("vnp_CardType")
    private String vnpCardType;

    @JsonProperty("vnp_OrderInfo")
    private String vnpOrderInfo;

    @JsonProperty("vnp_PayDate")
    private String vnpPayDate;

    @JsonProperty("vnp_ResponseCode")
    private String vnpResponseCode;

    @JsonProperty("vnp_SecureHash")
    private String vnpSecureHash;

    @JsonProperty("vnp_SecureHashType")
    private String vnpSecureHashType;

    @JsonProperty("vnp_TmnCode")
    private String vnpTmnCode;

    @JsonProperty("vnp_TransactionNo")
    private String vnpTransactionNo;

    @JsonProperty("vnp_TxnRef")
    private String vnpTxnRef;

    @JsonProperty("vnp_TransactionStatus")
    private String vnpTransactionStatus;

    private Long medicalId;

    private String paymentReturnUrl;


    public String getVnpOrderInfo() {
        return vnpOrderInfo;
    }

    public void setVnpOrderInfo(String vnpOrderInfo) throws UnsupportedEncodingException {
        if (Strings.isBlank(vnpOrderInfo)) this.vnpOrderInfo = null;
        this.vnpOrderInfo = java.net.URLDecoder.decode(vnpOrderInfo, StandardCharsets.UTF_8.name());
    }
}
