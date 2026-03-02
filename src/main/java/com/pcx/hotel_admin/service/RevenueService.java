package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.vo.RevenuePointVO;
import java.time.LocalDate;
import java.util.List;

public interface RevenueService {
    List<RevenuePointVO> getRevenue(LocalDate from, LocalDate to);
}
