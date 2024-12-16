package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Api.form.PaymentSaveForm;
import com.KMA.BookingCare.Entity.MedicalExaminationScheduleEntity;
import com.KMA.BookingCare.Entity.PaymentEntity;
import com.KMA.BookingCare.Mapper.PaymentMapper;
import com.KMA.BookingCare.Repository.PaymentRepository;
import com.KMA.BookingCare.Service.MedicalExaminationScheduleService;
import com.KMA.BookingCare.Service.PaymentService;
import com.KMA.BookingCare.common.Constant;
import com.KMA.BookingCare.common.PaymentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MedicalExaminationScheduleService medicalExaminationScheduleService;

    @Override
    public PaymentEntity save(PaymentSaveForm form) {
        System.out.println("save i");
        PaymentEntity entity ;
        List<PaymentEntity> paymentEntities = paymentRepository.findByVnpTxnRef(form.getVnpTxnRef());
        if (!CollectionUtils.isEmpty(paymentEntities)) {
            entity = paymentEntities.get(0);
            Optional<MedicalExaminationScheduleEntity> optional = medicalExaminationScheduleService.findOneById(form.getMedicalId());
            entity.setMedicalExaminationSchedule(optional.get());
            entity.getMedicalExaminationSchedule().setUser(null);
            entity.getMedicalExaminationSchedule().setDoctor(null);
            entity.getMedicalExaminationSchedule().setPayment(null);
            return entity;
        }
        entity = PaymentMapper.convertToEntity(form);
        entity.setCreatedDate(new Date());
        entity.setCreatedBy(userDetailsService.getUserDetailsImplFromContext().getId());

        Optional<MedicalExaminationScheduleEntity> optional = medicalExaminationScheduleService.findOneById(form.getMedicalId());
        MedicalExaminationScheduleEntity medicalExaminationSchedule = optional.get();
        medicalExaminationSchedule.setStatusPayment(Constant.payment_paid);
        medicalExaminationSchedule.setPayment(entity);
        medicalExaminationScheduleService.update(medicalExaminationSchedule);

        entity.setMedicalExaminationSchedule(medicalExaminationSchedule);
        entity.getMedicalExaminationSchedule().setUser(null);
        entity.getMedicalExaminationSchedule().setDoctor(null);
        return entity;
    }

    @Override
    public int isStatusPaymentReturn(PaymentSaveForm form) throws UnsupportedEncodingException {
        String url = java.net.URLDecoder.decode(form.getPaymentReturnUrl(), StandardCharsets.UTF_8.name());
        String query = url.split("\\?")[1];
        String[] params = query.split("&");
        Map fields = new HashMap();

        for (String param : params) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = param.split("=")[0];
                fieldValue = URLEncoder.encode(param.split("=")[1], StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = (String) fields.get("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = this.hashAllFields(fields);
        if (!signValue.equals(vnp_SecureHash)) return -1;
        if ("00".equals(fields.get("vnp_TransactionStatus"))) {
            return 1;
        } else {
            return 0;
        }
    }
    
    private String hashAllFields(Map<String, String> fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(fieldValue);
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        return PaymentUtils.hmacSHA512(Constant.vnp_HashSecret, hashData.toString());
    }

}
