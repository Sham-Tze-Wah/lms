package com.rbtsb.lms.pojo;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.constant.Role;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeePojo {

    //employee pojo
    private String empId;
    private String name;
    private String phoneNo;
    private String email;
    private String address;
    private Position position;
    private Role role;
    private Date dateJoined;
    private Date dateLeave;
}
