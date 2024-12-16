package com.KMA.BookingCare.Service;

import com.KMA.BookingCare.Api.form.PaymentSaveForm;
import com.KMA.BookingCare.Entity.PaymentEntity;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface PaymentService {
    PaymentEntity save(PaymentSaveForm form);

    int isStatusPaymentReturn(PaymentSaveForm form) throws MalformedURLException, UnsupportedEncodingException;
}
