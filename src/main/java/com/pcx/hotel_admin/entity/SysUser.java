package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String passwordHash;
    private String role;
    private Integer active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
