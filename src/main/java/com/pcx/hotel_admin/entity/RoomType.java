package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomType {
    private Long id;
    private String typeName;
    private BigDecimal price;
    private Integer capacity;
    private String description;
    private Integer active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
