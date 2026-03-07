package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.vo.RoomDashVO;
import com.pcx.hotel_admin.vo.RoomReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RoomDashMapper {
    
    List<RoomDashVO> selectDashboard(@Param("date") LocalDate date, @Param("q") String q);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int insertReq(@Param("roomId") Long roomId, @Param("date") LocalDate date, @Param("content") String content);
    
    List<RoomReqVO> selectReqs(@Param("roomId") Long roomId, @Param("date") LocalDate date);
    
    // Status transition constraints
    int occupyRoom(@Param("id") Long id);
    
    int checkoutToClean(@Param("id") Long id);
}
