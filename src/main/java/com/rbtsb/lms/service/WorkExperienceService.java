package com.rbtsb.lms.service;


import com.rbtsb.lms.pojo.WorkExperiencePojo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;


public interface WorkExperienceService {
    public String insertWorkExperience(WorkExperiencePojo workExperiencePojo);

    public List<WorkExperiencePojo> getAllWorkExperience();

    public String updateWorkExperienceByEmpId(String empId, WorkExperiencePojo workExperiencePojo);

    public String deleteWorkExperienceById(String id);

    public List<WorkExperiencePojo> getWorkExperienceByEmpId(String empId) ;
}
