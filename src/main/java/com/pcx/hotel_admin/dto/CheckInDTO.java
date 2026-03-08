package com.pcx.hotel_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CheckInDTO {
    private Long channelId;
    
    private String remark;
    
    private LocalDateTime expectedCheckOutAt;
    
    @NotNull(message = "入住人数不能为空")
    private Integer guestCount;
    
    @NotBlank(message = "主客姓名不能为空")
    private String mainGuestName;
    
    @NotNull(message = "主客性别不能为空")
    private Integer mainGuestGender;
    
    @NotBlank(message = "主客身份证号不能为空")
    private String mainGuestIdCard;
    
    // 其他入住人（可选）
    private List<GuestDTO> otherGuests;
    
    @Data
    public static class GuestDTO {
        @NotBlank(message = "入住人姓名不能为空")
        private String name;
        
        @NotNull(message = "入住人性别不能为空")
        private Integer gender;
        
        @NotBlank(message = "入住人身份证号不能为空")
        private String idCard;
    }
}
