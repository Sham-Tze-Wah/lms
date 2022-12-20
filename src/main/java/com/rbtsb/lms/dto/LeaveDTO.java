package com.rbtsb.lms.dto;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.pojo.EmployeePojo;
import lombok.Data;

@Data
public class LeaveDTO {
    private int leaveId;
    private LeaveStatus leaveStatus;
    private String reason;
    private String description;
    private EmployeePojo employeePojo;
}
