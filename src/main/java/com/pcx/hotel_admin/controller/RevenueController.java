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
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from == null) {
            from = LocalDate.now().minusDays(6);
        }
        if (to == null) {
            to = LocalDate.now();
        }
        return Result.success(revenueService.getRevenue(from, to));
    }
}
