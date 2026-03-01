package com.pcx.hotel_admin.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class RoomReqDTO {
    @NotBlank
    private String content;
    private LocalDate date;
}
