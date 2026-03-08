package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Stay {
    private Long id;
    private Long roomId;
    private Long channelId;
    private LocalDateTime checkInAt;
    private LocalDateTime expectedCheckOutAt;
    private LocalDateTime checkOutAt;
    private Integer status; // 1-在住 2-已退房
    private String remark;
    private Integer active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
