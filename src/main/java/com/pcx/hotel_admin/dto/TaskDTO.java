package com.pcx.hotel_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDTO {
    private Long roomId;
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String content;
}
