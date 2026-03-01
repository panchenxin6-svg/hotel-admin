package com.pcx.hotel_admin.controller;

import com.pcx.hotel_admin.common.Result;
import com.pcx.hotel_admin.entity.SysUser;
import com.pcx.hotel_admin.mapper.SysUserMapper;
import com.pcx.hotel_admin.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MeController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole());
        vo.setActive(user.getActive());
        
        return Result.success(vo);
    }
}
