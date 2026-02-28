package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ChannelVO {
    private Long id;
    private String channelName;
    private String channelCode;
    private String contactPerson;
    private String contactPhone;
    private BigDecimal commissionRate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
