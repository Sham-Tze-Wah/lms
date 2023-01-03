package com.rbtsb.lms.dto;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.constant.LeaveType;
import com.rbtsb.lms.pojo.EmployeePojo;
import lombok.Data;

import java.util.Date;

@Data
public class LeaveDTO {
    private LeaveStatus leaveStatus = LeaveStatus.New;
    private String reason;
    private String description;
    private LeaveType leaveType;
    private Date startDateLeave = new Date();
    private Date endDateLeave = new Date();
    private String employeeName;
}