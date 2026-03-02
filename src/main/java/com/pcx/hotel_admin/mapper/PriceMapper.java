package com.pcx.hotel_admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface PriceMapper {
    void upsertPrice(@Param("roomTypeId") Long roomTypeId, @Param("date") LocalDate date, @Param("price") BigDecimal price);
    
    List<Map<String, Object>> selectPrices(@Param("roomTypeId") Long roomTypeId, @Param("from") LocalDate from, @Param("to") LocalDate to);
}
