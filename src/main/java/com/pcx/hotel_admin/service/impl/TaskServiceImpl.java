package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.mapper.TaskMapper;
import com.pcx.hotel_admin.service.TaskService;
import com.pcx.hotel_admin.vo.TaskVO;
import com.pcx.hotel_admin.dto.TaskDTO;
import com.pcx.hotel_admin.dto.TaskAssignDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public TaskVO create(TaskDTO dto, String username) {
        TaskVO task = new TaskVO();
        task.setRoomId(dto.getRoomId());
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setStatus(0); // 待处理
        task.setCreatedBy(username);
        taskMapper.insert(task);
        return taskMapper.selectById(task.getId());
    }

    @Override
    public List<TaskVO> list(Integer status, Boolean mine, String username, boolean isAdmin) {
        // USER 强制 mine=true
        if (!isAdmin) {
            mine = true;
        }
        return taskMapper.selectList(status, username, mine);
    }

    @Override
    public TaskVO assign(Long id, TaskAssignDTO dto) {
        taskMapper.updateAssign(id, dto.getAssignedTo());
        return taskMapper.selectById(id);
    }

    @Override
    public TaskVO done(Long id, String username, boolean isAdmin) {
        // 负责人本人或管理员可完成
        TaskVO task = taskMapper.selectById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        if (!isAdmin && !username.equals(task.getAssignedTo())) {
            throw new RuntimeException("只有负责人可以完成");
        }
        taskMapper.updateDone(id, username);
        return taskMapper.selectById(id);
    }

    @Override
    public TaskVO approve(Long id, String username) {
        taskMapper.updateApprove(id, username);
        return taskMapper.selectById(id);
    }
}
