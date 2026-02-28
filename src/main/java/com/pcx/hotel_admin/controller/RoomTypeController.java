package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.RoomTypeDTO;
import com.pcx.hotel_admin.service.RoomTypeService;
import com.pcx.hotel_admin.vo.RoomTypeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间类型管理")
@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Operation(summary = "创建房间类型")
    @PostMapping
    public Result<RoomTypeVO> create(@Valid @RequestBody RoomTypeDTO dto) {
        return Result.success(roomTypeService.create(dto));
    }

    @Operation(summary = "更新房间类型")
    @PutMapping("/{id}")
    public Result<RoomTypeVO> update(@PathVariable Long id, @Valid @RequestBody RoomTypeDTO dto) {
        return Result.success(roomTypeService.update(id, dto));
    }

    @Operation(summary = "删除房间类型")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomTypeService.delete(id);
        return Result.success();
    }

    @Operation(summary = "获取房间类型详情")
    @GetMapping("/{id}")
    public Result<RoomTypeVO> getById(@PathVariable Long id) {
        return Result.success(roomTypeService.getById(id));
    }

    @Operation(summary = "获取所有房间类型")
    @GetMapping
    public Result<List<RoomTypeVO>> listAll() {
        return Result.success(roomTypeService.listAll());
    }

    @Operation(summary = "版本化创建房间类型")
    @PostMapping("/{id}/version")
    public Result<RoomTypeVO> createVersion(@PathVariable Long id, @Valid @RequestBody RoomTypeDTO dto) {
        return Result.success(roomTypeService.createVersion(id, dto));
    }
}
