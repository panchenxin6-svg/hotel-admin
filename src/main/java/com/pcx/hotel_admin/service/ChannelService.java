package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.dto.ChannelDTO;
import com.pcx.hotel_admin.vo.ChannelVO;
import java.util.List;

public interface ChannelService {
    ChannelVO create(ChannelDTO dto);
    ChannelVO update(Long id, ChannelDTO dto);
    void delete(Long id);
    ChannelVO getById(Long id);
    List<ChannelVO> listAll();
}
