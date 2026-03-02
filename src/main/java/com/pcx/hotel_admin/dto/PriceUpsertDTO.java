package com.pcx.hotel_admin.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PriceUpsertDTO {
    private Long roomTypeId;
    private LocalDate date;
    private BigDecimal price;
}
