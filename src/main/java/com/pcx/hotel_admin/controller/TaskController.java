package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.TaskAssignDTO;
import com.pcx.hotel_admin.dto.TaskDTO;
import com.pcx.hotel_admin.service.TaskService;
import com.pcx.hotel_admin.vo.TaskVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 创建任务 - 任意登录用户
     */
    @PostMapping
    public Result<TaskVO> create(@RequestBody @Valid TaskDTO dto) {
        String username = getCurrentUsername();
        TaskVO task = taskService.create(dto, username);
        return Result.success(task);
    }

    /**
     * 任务列表
     * - ADMIN/SUPER_ADMIN 可查看全部
     * - 普通 USER 只能看 mine=true
     */
    @GetMapping
    public Result<List<TaskVO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Boolean mine) {
        String username = getCurrentUsername();
        boolean isAdmin = isAdmin();
        List<TaskVO> list = taskService.list(status, mine, username, isAdmin);
        return Result.success(list);
    }

    /**
     * 指派任务 - 仅 ADMIN/SUPER_ADMIN
     */
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Result<TaskVO> assign(@PathVariable Long id, @RequestBody @Valid TaskAssignDTO dto) {
        TaskVO task = taskService.assign(id, dto);
        return Result.success(task);
    }

    /**
     * 完成任务 - 负责人本人或 ADMIN+
     */
    @PatchMapping("/{id}/done")
    public Result<TaskVO> done(@PathVariable Long id) {
        String username = getCurrentUsername();
        boolean isAdmin = isAdmin();
        TaskVO task = taskService.done(id, username, isAdmin);
        return Result.success(task);
    }

    /**
     * 确认任务 - 仅 ADMIN+
     */
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Result<TaskVO> approve(@PathVariable Long id) {
        String username = getCurrentUsername();
        TaskVO task = taskService.approve(id, username);
        return Result.success(task);
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_ADMIN".equals(role) || "ROLE_SUPER_ADMIN".equals(role)) {
                return true;
            }
        }
        return false;
    }
}
