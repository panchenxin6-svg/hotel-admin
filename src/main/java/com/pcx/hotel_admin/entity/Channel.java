package com.pcx.hotel_admin.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Channel {
    private Long id;
    private String channelName;
    private String channelCode;
    private String contactPerson;
    private String contactPhone;
    private BigDecimal commissionRate;
    private Integer active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
