package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.entity.RoomType;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RoomTypeMapper {
    List<RoomType> selectAll();
    RoomType selectById(Long id);
    int insert(RoomType roomType);
    int update(RoomType roomType);
    int deleteById(Long id);
}
