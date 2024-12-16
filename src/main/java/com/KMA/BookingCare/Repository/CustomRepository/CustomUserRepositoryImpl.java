package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.SpecialtyFeaturedDto;
import com.KMA.BookingCare.Dto.UserFeaturedDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserRepositoryImpl implements CustomUserRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserFeaturedDto> getFeaturedUser() {
        StringBuilder sql = new StringBuilder("SELECT u.id as userId, count(m.doctor_id) as total ");
        sql.append(" FROM");
        sql.append(" bookingCare.user u ");
        sql.append(" INNER JOIN user_role ur ON ");
        sql.append(" u.id = ur.user_id");
        sql.append(" LEFT JOIN medical_examination_schedule m");
        sql.append(" ON u.id = m.doctor_id WHERE ur.role_id = 2");
        sql.append(" GROUP BY userId");
        sql.append(" ORDER BY total DESC LIMIT 6");
        Query query = entityManager.createNativeQuery(sql.toString());
        List<UserFeaturedDto> userFeaturedDtos = (List<UserFeaturedDto>) query.getResultList()
                .stream()
                .map( item -> {
                    Object[] result = (Object[]) item;
                    BigInteger id = (BigInteger) result[0];
                    BigInteger total = (BigInteger) result[1];

                    return UserFeaturedDto.builder().id(id.longValue()).total(total.longValue()).build();
                })
                .collect(Collectors.toList());
        return userFeaturedDtos;
    }
}
