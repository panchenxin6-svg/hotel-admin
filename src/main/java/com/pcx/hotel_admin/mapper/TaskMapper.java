package com.pcx.hotel_admin.mapper;

import com.pcx.hotel_admin.vo.TaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    List<TaskVO> selectList(@Param("status") Integer status,
                            @Param("username") String username,
                            @Param("mine") Boolean mine);

    int insert(TaskVO task);

    int updateAssign(@Param("id") Long id, @Param("assignedTo") String assignedTo);

    int updateDone(@Param("id") Long id, @Param("doneBy") String doneBy);

    int updateApprove(@Param("id") Long id, @Param("approvedBy") String approvedBy);

    TaskVO selectById(@Param("id") Long id);
}
