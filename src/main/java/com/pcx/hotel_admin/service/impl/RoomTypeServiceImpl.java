package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.dto.RoomTypeDTO;
import com.pcx.hotel_admin.entity.RoomType;
import com.pcx.hotel_admin.mapper.RoomTypeMapper;
import com.pcx.hotel_admin.service.RoomTypeService;
import com.pcx.hotel_admin.vo.RoomTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Override
    public RoomTypeVO create(RoomTypeDTO dto) {
        RoomType roomType = new RoomType();
        BeanUtils.copyProperties(dto, roomType);
        roomTypeMapper.insert(roomType);
        return convertToVO(roomType);
    }

    @Override
    public RoomTypeVO update(Long id, RoomTypeDTO dto) {
        RoomType existing = roomTypeMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("房间类型不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        roomTypeMapper.update(existing);
        return convertToVO(roomTypeMapper.selectById(id));
    }

    @Override
    public void delete(Long id) {
        roomTypeMapper.deleteById(id);
    }

    @Override
    public RoomTypeVO getById(Long id) {
        RoomType roomType = roomTypeMapper.selectById(id);
        if (roomType == null) {
            throw new RuntimeException("房间类型不存在");
        }
        return convertToVO(roomType);
    }

    @Override
    public List<RoomTypeVO> listAll() {
        List<RoomType> roomTypes = roomTypeMapper.selectAll();
        return roomTypes.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomTypeVO createVersion(Long id, RoomTypeDTO dto) {
        RoomType oldRoomType = roomTypeMapper.selectById(id);
        if (oldRoomType == null) {
            throw new RuntimeException("房间类型不存在");
        }
        
        RoomType newRoomType = new RoomType();
        BeanUtils.copyProperties(dto, newRoomType);
        newRoomType.setActive(1);
        roomTypeMapper.insert(newRoomType);
        
        return convertToVO(newRoomType);
    }

    private RoomTypeVO convertToVO(RoomType roomType) {
        RoomTypeVO vo = new RoomTypeVO();
        BeanUtils.copyProperties(roomType, vo);
        return vo;
    }
}
