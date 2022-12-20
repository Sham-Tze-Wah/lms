package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.WorkExperiencePojo;

public class WorkExpMapper {
    public static WorkExperiencePojo entityToPojo(WorkExperienceEntity workExperienceEntity){
        WorkExperiencePojo workExperiencePojo = new WorkExperiencePojo();
        workExperiencePojo.setExpId(workExperienceEntity.getExpId());
        workExperiencePojo.setWorkTitle(workExperienceEntity.getWorkTitle());
        workExperiencePojo.setYearsOfExperience(workExperienceEntity.getYearsOfExperience());
        workExperiencePojo.setDateJoined(workExperienceEntity.getDateJoined());
        workExperiencePojo.setDateLeave(workExperienceEntity.getDateLeave());
        workExperiencePojo.setCompanyName(workExperienceEntity.getCompanyName());
        workExperiencePojo.setEmployeePojo(EmployeeMapper.entityToPojo(workExperienceEntity.getEmployeeEntity()));

        return workExperiencePojo;
    }

    public static WorkExperienceEntity pojoToEntity(WorkExperiencePojo workExperiencePojo){
        WorkExperienceEntity workExperience = new WorkExperienceEntity();
        workExperience.setExpId(workExperiencePojo.getExpId());
        workExperience.setWorkTitle(workExperiencePojo.getWorkTitle());
        workExperience.setYearsOfExperience(workExperiencePojo.getYearsOfExperience());
        workExperience.setDateJoined(workExperiencePojo.getDateJoined());
        workExperience.setDateLeave(workExperiencePojo.getDateLeave());
        workExperience.setCompanyName(workExperiencePojo.getCompanyName());
        workExperience.setEmployeeEntity(EmployeeMapper.pojoToEntity(workExperiencePojo.getEmployeePojo()));

        return workExperience;
    }
}
