package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.dto.UserCreateDTO;
import com.pcx.hotel_admin.dto.UserRoleDTO;
import com.pcx.hotel_admin.service.UserService;
import com.pcx.hotel_admin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<UserVO> createUser(@RequestBody UserCreateDTO dto) {
        UserVO userVO = userService.createUser(dto);
        return Result.success(userVO);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<UserVO> updateUserRole(@PathVariable Long id, @RequestBody UserRoleDTO dto) {
        UserVO userVO = userService.updateUserRole(id, dto);
        return Result.success(userVO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<UserVO>> listUsers() {
        List<UserVO> users = userService.listAllUsers();
        return Result.success(users);
    }
}
