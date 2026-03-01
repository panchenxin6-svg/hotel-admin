package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.RoomReqDTO;
import com.pcx.hotel_admin.dto.RoomStatusDTO;
import com.pcx.hotel_admin.service.RoomDashService;
import com.pcx.hotel_admin.vo.RoomDashVO;
import com.pcx.hotel_admin.vo.RoomReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomDashController {

    @Autowired
    private RoomDashService roomDashService;

    @GetMapping("/dashboard")
    public Result<List<RoomDashVO>> dashboard(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(roomDashService.dashboard(date));
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
        return Result.success();
    }

    @GetMapping("/{id}/req")
    public Result<List<RoomReqVO>> getReqs(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(roomDashService.getReqs(id, date));
    }
}
