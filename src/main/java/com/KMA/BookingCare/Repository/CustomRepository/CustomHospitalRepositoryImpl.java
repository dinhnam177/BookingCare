package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.HospitalDto;
import com.KMA.BookingCare.Dto.HospitalFeaturedDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomHospitalRepositoryImpl implements CustomHospitalRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalFeaturedDto> getFeaturedHospital() {
        StringBuilder sql = new StringBuilder("SELECT h.id as hospital_id ,count(hospital_id) as total");
        sql.append(" FROM");
        sql.append(" bookingCare.hospital h LEFT JOIN (");
        sql.append(" SELECT u.hospital_id as hospital_id, m.id as m_id");
        sql.append(" FROM");
        sql.append(" medical_examination_schedule m inner join");
        sql.append(" bookingCare.user u ON u.id = m.doctor_id) AS tbn ON h.id = tbn.hospital_id ");
        sql.append(" GROUP BY h.id");
        sql.append(" ORDER BY total DESC LIMIT 6");
        Query query = entityManager.createNativeQuery(sql.toString());
        List<HospitalFeaturedDto> hospitalFeaturedDtos = (List<HospitalFeaturedDto>) query.getResultList()
                .stream()
                .map( item -> {
                    Object[] result = (Object[]) item;
                    BigInteger id = (BigInteger) result[0];
                    BigInteger total = (BigInteger) result[1];

                    return HospitalFeaturedDto.builder().id(id.longValue()).total(total.longValue()).build();
                })
                .collect(Collectors.toList());
        return hospitalFeaturedDtos;
    }
}
