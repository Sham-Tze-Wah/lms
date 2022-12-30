package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.LeaveDTO;

import java.util.List;

public interface LeaveService {
    public String insertLeave(LeaveDTO leaveDTO);

    public List<LeaveDTO> getAllLeave();

    public String updateLeaveStatus(String id, LeaveDTO leaveDTO);

    public String deleteLeaveById(String id);

    String approveLeaveStatus(String id);

    String rejectLeaveStatus(String id);
}
