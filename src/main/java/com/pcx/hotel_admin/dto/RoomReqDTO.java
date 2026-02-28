package com.pcx.hotel_admin.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RoomReqDTO {
    private String content;
    private LocalDate date;
}
