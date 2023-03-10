package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.LeavePojo;

import java.text.ParseException;
import java.util.List;

public interface LeaveService {
    public ApiErrorPojo insertLeave(LeaveDTO leaveDTO);

    public List<LeaveDTO> getAllLeave();

    public ApiErrorPojo updateLeaveApplication(String id, String leaveStatus, String reason, String description, String leaveType, String startDateLeave, String endDateLeave, String employeeId);

    public String deleteLeaveById(String id);

    ApiErrorPojo approveLeaveStatus(String id, String bossId);

    ApiErrorPojo rejectLeaveStatus(String id, String bossId);

    @Deprecated
    ApiErrorPojo checkDuplicateReason(LeaveDTO leaveDTO);

    List<LeaveDTO> getLeaveApplicationByEmpId(String empId);

    String assignHR(String leaveId, String assignerId, String hrId) throws ParseException;

    String validateLeave(String leaveId, String assignerId, String hrId, String rejectOrValidated);

    List<LeaveDTO> getAssignedLeaveApplicationByHRId(String hrId);
}
