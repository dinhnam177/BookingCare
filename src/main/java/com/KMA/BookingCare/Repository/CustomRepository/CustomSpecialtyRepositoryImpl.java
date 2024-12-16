package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.SpecialtyFeaturedDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSpecialtyRepositoryImpl implements CustomSpecialtyRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SpecialtyFeaturedDto> getFeaturedSpecialty() {
        StringBuilder sql = new StringBuilder("SELECT s.id as specialized_id ,count(specialized_id) as total");
        sql.append(" FROM");
        sql.append(" bookingCare.specialized s LEFT JOIN ( ");
        sql.append(" SELECT u.specialized_id as specialized_id, m.id as m_id ");
        sql.append(" FROM");
        sql.append(" medical_examination_schedule m inner join");
        sql.append(" bookingCare.user u ON u.id = m.doctor_id) AS tbn ON s.id = tbn.specialized_id  ");
        sql.append(" GROUP BY s.id");
        sql.append(" ORDER BY total DESC LIMIT 6");
        Query query = entityManager.createNativeQuery(sql.toString());
        List<SpecialtyFeaturedDto> specialtyFeaturedDtos = (List<SpecialtyFeaturedDto>) query.getResultList()
                .stream()
                .map( item -> {
                    Object[] result = (Object[]) item;
                    BigInteger id = (BigInteger) result[0];
                    BigInteger total = (BigInteger) result[1];

                    return SpecialtyFeaturedDto.builder().id(id.longValue()).total(total.longValue()).build();
                })
                .collect(Collectors.toList());
        return specialtyFeaturedDtos;
    }
}
