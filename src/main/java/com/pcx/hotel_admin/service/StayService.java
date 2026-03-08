package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.dto.CheckInDTO;
import com.pcx.hotel_admin.entity.StayGuest;
import java.util.List;

public interface StayService {
    void checkIn(Long roomId, CheckInDTO dto);
    
    void checkOut(Long roomId);
    
    List<StayGuest> getGuestsByRoomId(Long roomId);
}
