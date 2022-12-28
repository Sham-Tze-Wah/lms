package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.LeaveDTO;

import java.util.List;

public interface LeaveService {
    public String insertLeave(LeaveDTO leaveDTO);

    public List<LeaveDTO> getAllLeave();

    public String updateLeaveStatus(int id, LeaveDTO leaveDTO);

    public String deleteLeaveById(int id);

    String approveLeaveStatus(int id);

    String rejectLeaveStatus(int id);
}
