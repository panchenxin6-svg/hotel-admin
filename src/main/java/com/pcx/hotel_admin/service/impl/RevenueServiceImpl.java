package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.mapper.RevenueMapper;
import com.pcx.hotel_admin.service.RevenueService;
import com.pcx.hotel_admin.vo.RevenuePointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RevenueServiceImpl implements RevenueService {
    @Autowired
    private RevenueMapper revenueMapper;

    @Override
    public List<RevenuePointVO> getRevenue(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.plusDays(1).atStartOfDay();
        return revenueMapper.selectRevenue(fromDt, toDt);
    }
}
