package com.rbtsb.lms.pojo;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.constant.Role;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class EmployeePojo {

    //employee pojo
    private String empId = UUID.randomUUID().toString();
    private String name;
    private String phoneNo;
    private String email;
    private String address;
    private Position position;
    private Date dateJoined;
    private Date dateLeave;
}
