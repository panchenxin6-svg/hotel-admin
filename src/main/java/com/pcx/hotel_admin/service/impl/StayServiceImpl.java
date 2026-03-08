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
import java.util.List;

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
        
        // Insert main guest
        StayGuest mainGuest = new StayGuest();
        mainGuest.setStayId(stay.getId());
        mainGuest.setName(dto.getMainGuestName());
        mainGuest.setGender(dto.getMainGuestGender());
        mainGuest.setIdCard(dto.getMainGuestIdCard());
        mainGuest.setIsMain(1);
        stayMapper.insertGuest(mainGuest);
        
        // Insert other guests
        if (dto.getOtherGuests() != null && !dto.getOtherGuests().isEmpty()) {
            for (CheckInDTO.GuestDTO guestDto : dto.getOtherGuests()) {
                StayGuest guest = new StayGuest();
                guest.setStayId(stay.getId());
                guest.setName(guestDto.getName());
                guest.setGender(guestDto.getGender());
                guest.setIdCard(guestDto.getIdCard());
                guest.setIsMain(0);
                stayMapper.insertGuest(guest);
            }
        }
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

    @Override
    public List<StayGuest> getGuestsByRoomId(Long roomId) {
        Stay stay = stayMapper.selectActiveStayByRoomId(roomId);
        if (stay == null) {
            return null;
        }
        return stayMapper.selectGuestsByStayId(stay.getId());
    }
}
