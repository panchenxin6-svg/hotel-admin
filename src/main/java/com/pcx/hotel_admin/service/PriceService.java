package com.pcx.hotel_admin.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PriceService {
    void upsertPrice(Long roomTypeId, LocalDate date, BigDecimal price);
    
    void upsertPriceRange(Long roomTypeId, LocalDate from, LocalDate to, BigDecimal price);
    
    List<Map<String, Object>> getPrices(Long roomTypeId, LocalDate from, LocalDate to);
}
