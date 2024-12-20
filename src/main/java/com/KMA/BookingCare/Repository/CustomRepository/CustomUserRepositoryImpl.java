package com.KMA.BookingCare.Repository.CustomRepository;

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
        String sql = "SELECT u.id AS id, COUNT(*) AS total " +
                "FROM medical_examination_schedule AS m " +
                "INNER JOIN user AS u ON m.doctor_id = u.id " +
                "GROUP BY u.id " +
                "HAVING COUNT(*) > 1 " +
                "ORDER BY total DESC " +
                "LIMIT 6";
        Query query = entityManager.createNativeQuery(sql);
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
