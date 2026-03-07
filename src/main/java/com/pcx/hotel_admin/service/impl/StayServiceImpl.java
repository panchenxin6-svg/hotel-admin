package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.dto.CheckInDTO;
import com.pcx.hotel_admin.entity.Stay;
import com.pcx.hotel_admin.entity.StayGuest;
import com.pcx.hotel_admin.mapper.RoomDashMapper;
import com.pcx.hotel_admin.mapper.StayMapper;
import com.pcx.hotel_admin.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class StayServiceImpl implements StayService {

    @Autowired
    private StayMapper stayMapper;
    
    @Autowired
    private RoomDashMapper roomDashMapper;

    @Override
    @Transactional
    public void checkIn(Long roomId, CheckInDTO dto) {
        // Occupy room (status=0 -> 1)
        int updated = roomDashMapper.occupyRoom(roomId);
        if (updated == 0) {
            throw new RuntimeException("房间不是空房或不存在");
        }
        
        // Calculate expected check-out time
        LocalDateTime checkInAt = LocalDateTime.now();
        LocalDateTime expectedCheckOutAt = dto.getExpectedCheckOutAt();
        if (expectedCheckOutAt == null) {
            expectedCheckOutAt = LocalDate.now().plusDays(1).atTime(14, 0);
        }
        
        // Insert stay
        Stay stay = new Stay();
        stay.setRoomId(roomId);
        stay.setChannelId(dto.getChannelId());
        stay.setCheckInAt(checkInAt);
        stay.setExpectedCheckOutAt(expectedCheckOutAt);
        stay.setStatus(1);
        stay.setRemark(dto.getRemark());
        stayMapper.insertStay(stay);
        
        // Insert guest
        StayGuest guest = new StayGuest();
        guest.setStayId(stay.getId());
        guest.setName(dto.getName());
        guest.setGender(dto.getGender());
        guest.setIdCard(dto.getIdCard());
        guest.setIsMain(1);
        stayMapper.insertGuest(guest);
    }

    @Override
    @Transactional
    public void checkOut(Long roomId) {
        // Find active stay
        Stay stay = stayMapper.selectActiveStayByRoomId(roomId);
        if (stay == null) {
            throw new RuntimeException("该房间没有在住的记录");
        }
        
        // Checkout stay
        stayMapper.checkoutStay(stay.getId());
        
        // Set room to pending clean (status=2)
        int updated = roomDashMapper.checkoutToClean(roomId);
        if (updated == 0) {
            throw new RuntimeException("房间状态异常，无法退房");
        }
    }
}
