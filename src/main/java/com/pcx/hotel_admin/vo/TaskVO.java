package com.pcx.hotel_admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskVO {
    private Long id;
    private Long roomId;
    private String roomNo;
    private String title;
    private String content;
    private Integer status;
    private String createdBy;
    private LocalDateTime createdAt;
    private String assignedTo;
    private LocalDateTime assignedAt;
    private String doneBy;
    private LocalDateTime doneAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
}
