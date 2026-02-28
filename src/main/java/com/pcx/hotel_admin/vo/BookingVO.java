package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingVO {
    private Long id;
    private String bookingNo;
    private String guestName;
    private String guestPhone;
    private String idCard;
    private Long roomTypeId;
    private String roomTypeName;
    private Long channelId;
    private String channelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusName;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
