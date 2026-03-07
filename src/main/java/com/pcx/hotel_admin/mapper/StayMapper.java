package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.entity.Stay;
import com.pcx.hotel_admin.entity.StayGuest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StayMapper {
    int insertStay(Stay stay);
    
    int insertGuest(StayGuest guest);
    
    Stay selectActiveStayByRoomId(@Param("roomId") Long roomId);
    
    int checkoutStay(@Param("id") Long id);
}
