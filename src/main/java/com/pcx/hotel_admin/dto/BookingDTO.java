package com.pcx.hotel_admin.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingDTO {
    @NotBlank(message = "客人姓名不能为空")
    private String guestName;

    @NotBlank(message = "客人电话不能为空")
    private String guestPhone;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @NotNull(message = "房间类型ID不能为空")
    private Long roomTypeId;

    @NotNull(message = "渠道ID不能为空")
    private Long channelId;

    @NotNull(message = "入住日期不能为空")
    private LocalDate checkInDate;

    @NotNull(message = "退房日期不能为空")
    private LocalDate checkOutDate;

    @NotNull(message = "总金额不能为空")
    @Min(value = 0, message = "总金额不能小于0")
    private BigDecimal totalAmount;

    @NotNull(message = "预订状态不能为空")
    private Integer status;

    private String remark;
}
