package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.vo.ChannelStatVO;

import java.time.LocalDate;
import java.util.List;

public interface StatsService {
    List<ChannelStatVO> getChannelStats(LocalDate from, LocalDate to);
}
