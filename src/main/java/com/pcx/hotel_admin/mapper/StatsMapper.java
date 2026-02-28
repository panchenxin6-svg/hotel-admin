package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.vo.ChannelStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface StatsMapper {
    List<ChannelStatVO> getChannelStats(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
