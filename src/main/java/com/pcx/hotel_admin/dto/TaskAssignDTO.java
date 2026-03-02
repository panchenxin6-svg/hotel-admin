package com.pcx.hotel_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskAssignDTO {
    @NotBlank(message = "指派人不能为空")
    private String assignedTo;
}
