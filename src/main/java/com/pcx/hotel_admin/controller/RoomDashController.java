package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.CheckInDTO;
import com.pcx.hotel_admin.dto.RoomReqDTO;
import com.pcx.hotel_admin.dto.RoomStatusDTO;
import com.pcx.hotel_admin.dto.TaskDTO;
import com.pcx.hotel_admin.entity.StayGuest;
import com.pcx.hotel_admin.service.RoomDashService;
import com.pcx.hotel_admin.service.StayService;
import com.pcx.hotel_admin.service.TaskService;
import com.pcx.hotel_admin.vo.RoomDashVO;
import com.pcx.hotel_admin.vo.RoomReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomDashController {

    @Autowired
    private RoomDashService roomDashService;

    @Autowired
    private StayService stayService;
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/dashboard")
    public Result<List<RoomDashVO>> dashboard(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String q) {
        if (date == null) {
            date = LocalDate.now();
        }
        return Result.success(roomDashService.dashboard(date, q));
    }

    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody @Validated RoomStatusDTO dto) {
        if (dto.getStatus() < 0 || dto.getStatus() > 2) {
            throw new RuntimeException("状态只能为0/1/2");
        }
        roomDashService.updateStatus(id, dto.getStatus());
        return Result.success();
    }

    @PostMapping("/{id}/req")
    public Result<Void> addReq(@PathVariable Long id, @RequestBody @Validated RoomReqDTO dto) {
        LocalDate date = dto.getDate();
        if (date == null) {
            date = LocalDate.now();
        }
        roomDashService.addReq(id, dto.getContent(), date);
        
        // 同时创建工单
        String username = getCurrentUsername();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setRoomId(id);
        taskDTO.setTitle("房间需求");
        taskDTO.setContent(dto.getContent());
        taskDTO.setRemark(dto.getContent());
        taskService.create(taskDTO, username);
        
        return Result.success();
    }

    @GetMapping("/{id}/req")
    public Result<List<RoomReqVO>> getReqs(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(roomDashService.getReqs(id, date));
    }

    // Check-in
    @PostMapping("/{id}/checkin")
    public Result<Void> checkIn(@PathVariable Long id, @RequestBody @Validated CheckInDTO dto) {
        stayService.checkIn(id, dto);
        return Result.success();
    }

    // Check-out
    @PostMapping("/{id}/checkout")
    public Result<Void> checkOut(@PathVariable Long id) {
        stayService.checkOut(id);
        return Result.success();
    }

    // Get all guests for a room
    @GetMapping("/{id}/guests")
    public Result<List<StayGuest>> getGuests(@PathVariable Long id) {
        List<StayGuest> guests = stayService.getGuestsByRoomId(id);
        return Result.success(guests != null ? guests : List.of());
    }
    
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }
}
