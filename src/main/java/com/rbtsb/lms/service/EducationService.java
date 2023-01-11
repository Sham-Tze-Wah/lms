package com.rbtsb.lms.service;

import com.rbtsb.lms.pojo.EducationPojo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

public interface EducationService {
    public String insertEducationByEmpId(EducationPojo educationPojo);

    public List<EducationPojo> getAllEducation();

    public String updateEducationByEmpId(String empId, String qualification, String institute, String course, String employeeId);

    public String deleteEducationById(String id);

    public List<EducationPojo> getEducationByEmpId(String empId);
}
