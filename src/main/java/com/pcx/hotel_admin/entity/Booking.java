package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Booking {
    private Long id;
    private String bookingNo;
    private String guestName;
    private String guestPhone;
    private String idCard;
    private Long roomTypeId;
    private Long channelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
