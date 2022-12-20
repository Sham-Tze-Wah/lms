package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.EducationEntity;
import com.rbtsb.lms.pojo.EducationPojo;

public class EducationMapper {
    public static EducationPojo entityToPojo(EducationEntity educationEntity){
        EducationPojo educationPojo = new EducationPojo();
        educationPojo.setEducationId(educationEntity.getEducationId());
        educationPojo.setQualification(educationEntity.getQualification());
        educationPojo.setInstitute(educationEntity.getInstitute());
        educationPojo.setCourse(educationEntity.getCourse());
        educationPojo.setEmployeePojo(EmployeeMapper.entityToPojo(educationEntity.getEmployeeEntity()));

        return educationPojo;
    }

    public static EducationEntity pojoToEntity(EducationPojo educationPojo){
        EducationEntity education = new EducationEntity();
        education.setEducationId(educationPojo.getEducationId());
        education.setQualification(educationPojo.getQualification());
        education.setInstitute(educationPojo.getInstitute());
        education.setCourse(educationPojo.getCourse());
        education.setEmployeeEntity(EmployeeMapper.pojoToEntity(educationPojo.getEmployeePojo()));

        return education;
    }
}
