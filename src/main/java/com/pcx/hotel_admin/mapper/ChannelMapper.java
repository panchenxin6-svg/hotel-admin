package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.entity.Channel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChannelMapper {
    List<Channel> selectAll();
    Channel selectById(Long id);
    int insert(Channel channel);
    int update(Channel channel);
    int deleteById(Long id);
}
