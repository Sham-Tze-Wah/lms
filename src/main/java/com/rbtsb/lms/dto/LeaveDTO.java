package com.rbtsb.lms.dto;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.pojo.EmployeePojo;
import lombok.Data;

import java.util.Date;

@Data
public class LeaveDTO {
    private LeaveStatus leaveStatus = LeaveStatus.Submitted;
    private String reason;
    private String description;
    private Date dateLeave = new Date();
    private String employeeName;
}