package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.vo.TaskVO;
import com.pcx.hotel_admin.dto.TaskDTO;
import com.pcx.hotel_admin.dto.TaskAssignDTO;
import java.util.List;

public interface TaskService {
    TaskVO create(TaskDTO dto, String username);
    
    List<TaskVO> list(Integer status, Boolean mine, String username, boolean isAdmin);
    
    TaskVO assign(Long id, TaskAssignDTO dto);
    
    TaskVO done(Long id, String username, boolean isAdmin);
    
    TaskVO approve(Long id, String username);
}
