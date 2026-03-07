package com.pcx.hotel_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CheckInDTO {
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    @NotNull(message = "性别不能为空")
    private Integer gender; // 0-女 1-男
    
    @NotBlank(message = "身份证号不能为空")
    private String idCard;
    
    private Long channelId;
    
    private String remark;
    
    private LocalDateTime expectedCheckOutAt;
}
