package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.mapper.RoomDashMapper;
import com.pcx.hotel_admin.service.RoomDashService;
import com.pcx.hotel_admin.vo.RoomDashVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomDashServiceImpl implements RoomDashService {

    @Autowired
    private RoomDashMapper roomDashMapper;

    @Override
    public List<RoomDashVO> dashboard(LocalDate date) {
        return roomDashMapper.selectDashboard(date);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (status < 0 || status > 2) {
            throw new RuntimeException("状态只能为0/1/2");
        }
        roomDashMapper.updateStatus(id, status);
    }

    @Override
    public void addReq(Long roomId, String content, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        roomDashMapper.insertReq(roomId, date, content);
    }

    @Override
    public List<com.pcx.hotel_admin.vo.RoomReqVO> getReqs(Long roomId, LocalDate date) {
        return roomDashMapper.selectReqs(roomId, date);
    }
}
