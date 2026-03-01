package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.dto.UserCreateDTO;
import com.pcx.hotel_admin.dto.UserRoleDTO;
import com.pcx.hotel_admin.entity.SysUser;
import com.pcx.hotel_admin.mapper.SysUserMapper;
import com.pcx.hotel_admin.service.UserService;
import com.pcx.hotel_admin.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserVO createUser(UserCreateDTO dto) {
        // 禁止创建 SUPER_ADMIN
        if ("SUPER_ADMIN".equals(dto.getRole())) {
            throw new RuntimeException("Cannot create SUPER_ADMIN user");
        }
        // role 只能是 ADMIN 或 USER
        if (!"ADMIN".equals(dto.getRole()) && !"USER".equals(dto.getRole())) {
            throw new RuntimeException("Role must be ADMIN or USER");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setActive(1);

        sysUserMapper.insert(user);
        return toVO(user);
    }

    @Override
    public UserVO updateUserRole(Long id, UserRoleDTO dto) {
        // role 只能是 ADMIN 或 USER
        if (!"ADMIN".equals(dto.getRole()) && !"USER".equals(dto.getRole())) {
            throw new RuntimeException("Role must be ADMIN or USER");
        }

        SysUser user = new SysUser();
        user.setId(id);
        user.setRole(dto.getRole());

        sysUserMapper.updateById(user);

        SysUser updated = sysUserMapper.selectById(id);
        return toVO(updated);
    }

    @Override
    public List<UserVO> listAllUsers() {
        return sysUserMapper.selectAll().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    private UserVO toVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
