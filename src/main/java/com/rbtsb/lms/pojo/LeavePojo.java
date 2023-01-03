package com.rbtsb.lms.pojo;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.constant.LeaveType;
import com.rbtsb.lms.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeavePojo {
    private String leaveId;
    private LeaveStatus leaveStatus;
    private LeaveType leaveType;
    private String reason;
    private String description;
    private Date startDateLeave = new Date();
    private Date endDateLeave = new Date();
    private EmployeePojo employeePojo;
}
