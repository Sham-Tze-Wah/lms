package com.rbtsb.lms.service;


import com.rbtsb.lms.pojo.WorkExperiencePojo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;


public interface WorkExperienceService {
    public String insertWorkExperience(WorkExperiencePojo workExperiencePojo);

    public List<WorkExperiencePojo> getAllWorkExperience();

    public String updateWorkExperience(String workId, String workTitle, String yearsOfExperience, String companyName, String dateJoined, String dateResign, String empId);

    public String deleteWorkExperienceById(String id);

    public List<WorkExperiencePojo> getWorkExperienceByEmpId(String empId) ;
}
