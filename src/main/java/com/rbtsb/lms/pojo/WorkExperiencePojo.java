package com.rbtsb.lms.pojo;

import com.rbtsb.lms.constant.Position;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class WorkExperiencePojo {
    //workexp pojo
    private String expId = UUID.randomUUID().toString();
    private Position workTitle;
    private String yearsOfExperience;
    private String companyName;
    private Date dateJoined;
    private Date dateResign;
    private EmployeePojo employeePojo;
}
