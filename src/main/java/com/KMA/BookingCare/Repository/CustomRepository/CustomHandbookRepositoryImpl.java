package com.KMA.BookingCare.Repository.CustomRepository;

import com.KMA.BookingCare.Dto.HandbookFeaturedDto;
import com.KMA.BookingCare.Dto.UserFeaturedDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHandbookRepositoryImpl implements CustomHandbookRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HandbookFeaturedDto> getFeaturedHandbook() {
        StringBuilder sql = new StringBuilder("SELECT h.id, count(c.id) as total ");
        sql.append(" FROM");
        sql.append(" bookingCare.handbook h ");
        sql.append(" LEFT JOIN comment c ");
        sql.append(" ON h.id = c.handbook_id");
        sql.append(" GROUP BY h.id");
        sql.append(" ORDER BY total DESC LIMIT 6");
        Query query = entityManager.createNativeQuery(sql.toString());
        List<HandbookFeaturedDto> handbookFeaturedDtoList = (List<HandbookFeaturedDto>) query.getResultList()
                .stream()
                .map( item -> {
                    Object[] result = (Object[]) item;
                    BigInteger id = (BigInteger) result[0];
                    BigInteger total = (BigInteger) result[1];

                    return HandbookFeaturedDto.builder().id(id.longValue()).total(total.longValue()).build();
                })
                .collect(Collectors.toList());
        return handbookFeaturedDtoList;
    }
}
