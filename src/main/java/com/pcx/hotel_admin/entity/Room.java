package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Room {
    private Long id;
    private String roomNo;
    private Long roomTypeId;
    private Integer isHourly;
    private Integer status;
    private Integer active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
