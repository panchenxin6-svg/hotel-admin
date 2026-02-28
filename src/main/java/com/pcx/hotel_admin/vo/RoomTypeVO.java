package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomTypeVO {
    private Long id;
    private String typeName;
    private BigDecimal price;
    private Integer capacity;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
