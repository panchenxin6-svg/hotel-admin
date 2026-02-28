package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.BookingDTO;
import com.pcx.hotel_admin.service.BookingService;
import com.pcx.hotel_admin.vo.BookingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "预订管理")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "创建预订")
    @PostMapping
    public Result<BookingVO> create(@Valid @RequestBody BookingDTO dto) {
        return Result.success(bookingService.create(dto));
    }

    @Operation(summary = "更新预订")
    @PutMapping("/{id}")
    public Result<BookingVO> update(@PathVariable Long id, @Valid @RequestBody BookingDTO dto) {
        return Result.success(bookingService.update(id, dto));
    }

    @Operation(summary = "删除预订")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return Result.success();
    }

    @Operation(summary = "获取预订详情")
    @GetMapping("/{id}")
    public Result<BookingVO> getById(@PathVariable Long id) {
        return Result.success(bookingService.getById(id));
    }

    @Operation(summary = "获取所有预订")
    @GetMapping
    public Result<List<BookingVO>> listAll() {
        return Result.success(bookingService.listAll());
    }
}
