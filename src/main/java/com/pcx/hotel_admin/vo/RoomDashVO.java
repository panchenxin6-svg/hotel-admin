package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.math.BigDecimal;

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
}
