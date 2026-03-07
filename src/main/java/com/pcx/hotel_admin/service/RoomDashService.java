package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.vo.RoomDashVO;
import com.pcx.hotel_admin.vo.RoomReqVO;
import java.time.LocalDate;
import java.util.List;

public interface RoomDashService {
    
    List<RoomDashVO> dashboard(LocalDate date, String q);
    
    void updateStatus(Long id, Integer status);
    
    void addReq(Long roomId, String content, LocalDate date);
    
    List<RoomReqVO> getReqs(Long roomId, LocalDate date);
}
