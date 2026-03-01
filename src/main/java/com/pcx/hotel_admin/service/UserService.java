package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.dto.UserCreateDTO;
import com.pcx.hotel_admin.dto.UserRoleDTO;
import com.pcx.hotel_admin.vo.UserVO;
import java.util.List;

public interface UserService {
    UserVO createUser(UserCreateDTO dto);
    UserVO updateUserRole(Long id, UserRoleDTO dto);
    List<UserVO> listAllUsers();
}
