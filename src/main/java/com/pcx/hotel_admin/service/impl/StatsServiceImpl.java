package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.mapper.StatsMapper;
import com.pcx.hotel_admin.service.StatsService;
import com.pcx.hotel_admin.vo.ChannelStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsMapper statsMapper;

    @Override
    public List<ChannelStatVO> getChannelStats(LocalDate from, LocalDate to) {
        // 左闭右开区间
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.plusDays(1).atStartOfDay();
        return statsMapper.getChannelStats(fromDt, toDt);
    }
}
