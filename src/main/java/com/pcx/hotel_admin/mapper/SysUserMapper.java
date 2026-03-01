package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectByUsername(@Param("username") String username);
    List<SysUser> selectAll();
    int insert(SysUser sysUser);
    int updateById(SysUser sysUser);
    SysUser selectById(@Param("id") Long id);
}
