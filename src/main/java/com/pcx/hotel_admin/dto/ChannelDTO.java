package com.pcx.hotel_admin.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ChannelDTO {
    @NotBlank(message = "渠道名称不能为空")
    private String channelName;

    @NotBlank(message = "渠道编码不能为空")
    private String channelCode;

    private String contactPerson;

    private String contactPhone;

    @NotNull(message = "佣金比例不能为空")
    @Min(value = 0, message = "佣金比例不能小于0")
    @Max(value = 100, message = "佣金比例不能大于100")
    private BigDecimal commissionRate;
}
