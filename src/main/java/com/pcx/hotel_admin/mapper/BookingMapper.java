package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.entity.Booking;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BookingMapper {
    List<Booking> selectAll();
    Booking selectById(Long id);
    int insert(Booking booking);
    int update(Booking booking);
    int deleteById(Long id);
}
