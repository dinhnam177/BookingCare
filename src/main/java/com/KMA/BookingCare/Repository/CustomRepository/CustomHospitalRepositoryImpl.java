package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.HospitalFeaturedDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHospitalRepositoryImpl implements CustomHospitalRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalFeaturedDto> getFeaturedHospital() {
        String sql = "SELECT h.id AS id, COUNT(*) AS total " +
                "FROM medical_examination_schedule AS m " +
                "INNER JOIN hospital AS h ON m.hospital_name = h.name " +
                "GROUP BY h.id " +
                "HAVING COUNT(*) > 1 " +
                "ORDER BY total DESC LIMIT 6";
        Query query = entityManager.createNativeQuery(sql);
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
