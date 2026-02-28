package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.ChannelDTO;
import com.pcx.hotel_admin.service.ChannelService;
import com.pcx.hotel_admin.vo.ChannelVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "渠道管理")
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Operation(summary = "创建渠道")
    @PostMapping
    public Result<ChannelVO> create(@Valid @RequestBody ChannelDTO dto) {
        return Result.success(channelService.create(dto));
    }

    @Operation(summary = "更新渠道")
    @PutMapping("/{id}")
    public Result<ChannelVO> update(@PathVariable Long id, @Valid @RequestBody ChannelDTO dto) {
        return Result.success(channelService.update(id, dto));
    }

    @Operation(summary = "删除渠道")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        channelService.delete(id);
        return Result.success();
    }

    @Operation(summary = "获取渠道详情")
    @GetMapping("/{id}")
    public Result<ChannelVO> getById(@PathVariable Long id) {
        return Result.success(channelService.getById(id));
    }

    @Operation(summary = "获取所有渠道")
    @GetMapping
    public Result<List<ChannelVO>> listAll() {
        return Result.success(channelService.listAll());
    }
}
