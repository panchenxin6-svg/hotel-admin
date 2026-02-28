package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.dto.RoomTypeDTO;
import com.pcx.hotel_admin.vo.RoomTypeVO;
import java.util.List;

public interface RoomTypeService {
    RoomTypeVO create(RoomTypeDTO dto);
    RoomTypeVO update(Long id, RoomTypeDTO dto);
    void delete(Long id);
    RoomTypeVO getById(Long id);
    List<RoomTypeVO> listAll();
    RoomTypeVO createVersion(Long id, RoomTypeDTO dto);
}
