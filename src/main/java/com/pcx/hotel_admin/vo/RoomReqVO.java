package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RoomReqVO {
    private Long id;
    private String content;
    private LocalDate reqDate;
    private LocalDateTime createTime;
}
