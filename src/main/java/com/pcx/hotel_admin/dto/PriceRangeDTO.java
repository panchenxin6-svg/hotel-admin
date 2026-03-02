package com.pcx.hotel_admin.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PriceRangeDTO {
    private Long roomTypeId;
    private LocalDate from;
    private LocalDate to;
    private BigDecimal price;
}
