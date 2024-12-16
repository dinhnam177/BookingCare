package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.PaymentForm;
import com.KMA.BookingCare.Api.form.PaymentSaveForm;
import com.KMA.BookingCare.Dto.PaymentResultDto;
import com.KMA.BookingCare.Service.PaymentService;
import com.KMA.BookingCare.common.Constant;
import com.KMA.BookingCare.common.PaymentUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PaymentApi {

    @Autowired
    private PaymentService paymentService;

    @Value("${booking_care.ui.domain}")
    private String domain;

    @PostMapping("api/payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentForm form) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = "vnp_IpAddr";
        String vnp_TmnCode = Constant.vnp_TmnCode;
        Integer amount = form.getAmount() * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (!Strings.isBlank(form.getBankCode())) {
            vnp_Params.put("vnp_BankCode", form.getBankCode());
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", Constant.vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", domain + Constant.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentUtils.hmacSHA512(Constant.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Constant.vnp_PayUrl + "?" + queryUrl;

        PaymentResultDto result = new PaymentResultDto();
        result.setCode("00");
        result.setMessage("success");
        result.setData(paymentUrl);

        return ResponseEntity.ok(result);
    }

    @PostMapping("api/payment/save")
    public ResponseEntity<?> returnUrl(@RequestBody PaymentSaveForm form) {
        return ResponseEntity.ok(paymentService.save(form));
    }

    @PostMapping("api/payment/return")
    public ResponseEntity<?> paymentReturn(@RequestBody PaymentSaveForm form) throws MalformedURLException, UnsupportedEncodingException {
        System.out.println("Return payment");
        Map<String, Object> response = new HashMap<>();
        int status = paymentService.isStatusPaymentReturn(form);
        response.put("status", status);
        if (Objects.equals(1, status)) {
            response.put("data", paymentService.save(form));
        }
        return ResponseEntity.ok(response);
    }


}
