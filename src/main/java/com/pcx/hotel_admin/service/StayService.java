package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.dto.CheckInDTO;

public interface StayService {
    void checkIn(Long roomId, CheckInDTO dto);
    
    void checkOut(Long roomId);
}
