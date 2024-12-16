package com.KMA.BookingCare.Mapper;

import com.KMA.BookingCare.Api.form.PaymentSaveForm;
import com.KMA.BookingCare.Dto.PaymentDto;
import com.KMA.BookingCare.Entity.PaymentEntity;

public class PaymentMapper {

    public static PaymentEntity convertToEntity(PaymentSaveForm form) {
        PaymentEntity entity = new PaymentEntity();
        entity.setVnpAmount(form.getVnpAmount());
        entity.setVnpBankCode(form.getVnpBankCode());
        entity.setVnpBankTranNo(form.getVnpBankTranNo());
        entity.setVnpCardType(form.getVnpCardType());
        entity.setVnpPayDate(form.getVnpPayDate());
        entity.setVnpOrderInfo(form.getVnpOrderInfo());
        entity.setVnpResponseCode(form.getVnpResponseCode());
        entity.setVnpSecureHash(form.getVnpSecureHash());
        entity.setVnpSecureHashType(form.getVnpSecureHashType());
        entity.setVnpTmnCode(form.getVnpTmnCode());
        entity.setVnpTxnRef(form.getVnpTxnRef());
        return entity;
    }

    public static PaymentDto convertToDTO(PaymentEntity entity) {
        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVnpAmount(entity.getVnpAmount());
        dto.setVnpBankTranNo(entity.getVnpBankTranNo());
        dto.setVnpBankCode(entity.getVnpBankCode());
        dto.setVnpCardType(entity.getVnpCardType());
        dto.setVnpOrderInfo(entity.getVnpOrderInfo());
        dto.setVnpPayDate(entity.getVnpPayDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setVnpSecureHash(entity.getVnpSecureHash());
        dto.setVnpResponseCode(entity.getVnpResponseCode());
        dto.setVnpTmnCode(entity.getVnpTmnCode());
        dto.setVnpTransactionNo(entity.getVnpTransactionNo());
        dto.setVnpTxnRef(entity.getVnpTxnRef());
        return dto;
    }
}
