package com.rbtsb.lms.pojo;

import com.rbtsb.lms.constant.Course;
import com.rbtsb.lms.constant.Qualification;
import lombok.Data;

@Data
public class EducationPojo {
    //education pojo
    private String educationId;
    private Qualification qualification;
    private String institute;
    private Course course;
    private EmployeePojo employeePojo;
}
