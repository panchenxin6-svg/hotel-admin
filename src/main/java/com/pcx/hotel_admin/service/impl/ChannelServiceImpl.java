package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.dto.ChannelDTO;
import com.pcx.hotel_admin.entity.Channel;
import com.pcx.hotel_admin.mapper.ChannelMapper;
import com.pcx.hotel_admin.service.ChannelService;
import com.pcx.hotel_admin.vo.ChannelVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public ChannelVO create(ChannelDTO dto) {
        Channel channel = new Channel();
        BeanUtils.copyProperties(dto, channel);
        channelMapper.insert(channel);
        return convertToVO(channel);
    }

    @Override
    public ChannelVO update(Long id, ChannelDTO dto) {
        Channel existing = channelMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("渠道不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        channelMapper.update(existing);
        return convertToVO(channelMapper.selectById(id));
    }

    @Override
    public void delete(Long id) {
        channelMapper.deleteById(id);
    }

    @Override
    public ChannelVO getById(Long id) {
        Channel channel = channelMapper.selectById(id);
        if (channel == null) {
            throw new RuntimeException("渠道不存在");
        }
        return convertToVO(channel);
    }

    @Override
    public List<ChannelVO> listAll() {
        List<Channel> channels = channelMapper.selectAll();
        return channels.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private ChannelVO convertToVO(Channel channel) {
        ChannelVO vo = new ChannelVO();
        BeanUtils.copyProperties(channel, vo);
        return vo;
    }
}
