package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.service.RevenueService;
import com.pcx.hotel_admin.vo.RevenuePointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
public class RevenueController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/revenue")
    public Result<List<RevenuePointVO>> getRevenue(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return Result.success(revenueService.getRevenue(from, to));
    }
}
