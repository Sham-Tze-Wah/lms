package com.rbtsb.lms.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.constant.Role;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(value = TemporalType.DATE)
    private Date dateJoined;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(value = TemporalType.DATE)
    private Date dateLeave;
}
