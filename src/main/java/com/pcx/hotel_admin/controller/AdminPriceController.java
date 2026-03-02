package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.PriceRangeDTO;
import com.pcx.hotel_admin.dto.PriceUpsertDTO;
import com.pcx.hotel_admin.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/prices")
public class AdminPriceController {

    @Autowired
    private PriceService priceService;

    @PutMapping
    public Result<Void> upsertPrice(@RequestBody PriceUpsertDTO dto) {
        priceService.upsertPrice(dto.getRoomTypeId(), dto.getDate(), dto.getPrice());
        return Result.success();
    }

    @PutMapping("/range")
    public Result<Void> upsertPriceRange(@RequestBody PriceRangeDTO dto) {
        priceService.upsertPriceRange(dto.getRoomTypeId(), dto.getFrom(), dto.getTo(), dto.getPrice());
        return Result.success();
    }

    @GetMapping
    public Result<List<Map<String, Object>>> getPrices(
            @RequestParam Long roomTypeId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<Map<String, Object>> prices = priceService.getPrices(roomTypeId, from, to);
        return Result.success(prices);
    }
}
