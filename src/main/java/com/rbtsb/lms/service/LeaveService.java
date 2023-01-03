package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.LeavePojo;

import java.util.List;

public interface LeaveService {
    public ApiErrorPojo insertLeave(LeaveDTO leaveDTO);

    public List<LeaveDTO> getAllLeave();

    public ApiErrorPojo updateLeaveApplication(String id, LeaveDTO leaveDTO);

    public String deleteLeaveById(String id);

    String approveLeaveStatus(String id);

    String rejectLeaveStatus(String id);

    @Deprecated
    ApiErrorPojo checkDuplicateReason(LeaveDTO leaveDTO);
}
