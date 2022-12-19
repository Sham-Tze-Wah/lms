package com.rbtsb.lms.service;

import com.rbtsb.lms.pojo.EducationPojo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EducationService {
    public String insertEducationByEmpId(EducationPojo educationPojo);

    public List<EducationPojo> getAllEducation();

    public String updateEducationByEmpId(String empId, EducationPojo educationPojo);

    public String deleteEducationById(String id);
}
