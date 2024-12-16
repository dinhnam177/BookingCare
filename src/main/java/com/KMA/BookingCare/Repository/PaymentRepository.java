package com.KMA.BookingCare.Repository;

import com.KMA.BookingCare.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByVnpTxnRef(String vpnTxnRef);

}
