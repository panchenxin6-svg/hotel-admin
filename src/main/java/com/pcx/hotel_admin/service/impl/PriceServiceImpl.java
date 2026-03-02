package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.mapper.PriceMapper;
import com.pcx.hotel_admin.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceMapper priceMapper;

    @Override
    public void upsertPrice(Long roomTypeId, LocalDate date, BigDecimal price) {
        priceMapper.upsertPrice(roomTypeId, date, price);
    }

    @Override
    public void upsertPriceRange(Long roomTypeId, LocalDate from, LocalDate to, BigDecimal price) {
        if (from.isAfter(to)) {
            throw new RuntimeException("from不能大于to");
        }
        LocalDate current = from;
        while (!current.isAfter(to)) {
            priceMapper.upsertPrice(roomTypeId, current, price);
            current = current.plusDays(1);
        }
    }

    @Override
    public List<Map<String, Object>> getPrices(Long roomTypeId, LocalDate from, LocalDate to) {
        return priceMapper.selectPrices(roomTypeId, from, to);
    }
}
