package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StayGuest {
    private Long id;
    private Long stayId;
    private String name;
    private Integer gender; // 0-女 1-男
    private String idCard;
    private Integer isMain; // 1-主住客
    private LocalDateTime createTime;
}
