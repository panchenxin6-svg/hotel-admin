package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomDashVO {
    private Long id;
    private String roomNo;
    private Integer status;
    private Integer isHourly;
    private Long roomTypeId;
    private String roomTypeName;
    private BigDecimal todayPrice;
    private Long demandCount;
    // Stay info
    private String guestName;
    private String guestIdCard;
    private LocalDateTime checkInAt;
    private LocalDateTime expectedCheckOutAt;
    private String channelName;
}
