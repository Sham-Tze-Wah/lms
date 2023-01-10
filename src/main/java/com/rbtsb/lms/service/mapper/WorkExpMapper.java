package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class WorkExpMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public WorkExperiencePojo entityToPojo(WorkExperienceEntity workExperienceEntity) {
        WorkExperiencePojo workExperiencePojo = new WorkExperiencePojo();
        workExperiencePojo.setExpId(workExperienceEntity.getExpId());
        workExperiencePojo.setWorkTitle(workExperienceEntity.getWorkTitle());
        workExperiencePojo.setYearsOfExperience(workExperienceEntity.getYearsOfExperience());
        workExperiencePojo.setDateJoined(workExperienceEntity.getDateJoined());
        workExperiencePojo.setDateResign(workExperienceEntity.getDateResign());
        workExperiencePojo.setCompanyName(workExperienceEntity.getCompanyName());
        workExperiencePojo.setEmployeePojo(employeeMapper.entityToPojo(workExperienceEntity.getEmployeeEntity()));

        return workExperiencePojo;
    }

    public WorkExperienceEntity pojoToEntity(WorkExperiencePojo workExperiencePojo){
        WorkExperienceEntity workExperience = new WorkExperienceEntity();
        workExperience.setExpId(workExperiencePojo.getExpId());
        workExperience.setWorkTitle(workExperiencePojo.getWorkTitle());
        workExperience.setYearsOfExperience(workExperiencePojo.getYearsOfExperience());
        workExperience.setDateJoined(workExperiencePojo.getDateJoined());
        workExperience.setDateResign(workExperiencePojo.getDateResign());
        workExperience.setCompanyName(workExperiencePojo.getCompanyName());
        workExperience.setEmployeeEntity(employeeMapper.pojoToEntity(workExperiencePojo.getEmployeePojo()));

        return workExperience;
    }
}
