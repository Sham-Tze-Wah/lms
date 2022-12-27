package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.EducationEntity;
import com.rbtsb.lms.pojo.EducationPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public EducationPojo entityToPojo(EducationEntity educationEntity){
        EducationPojo educationPojo = new EducationPojo();
        educationPojo.setEducationId(educationEntity.getEducationId());
        educationPojo.setQualification(educationEntity.getQualification());
        educationPojo.setInstitute(educationEntity.getInstitute());
        educationPojo.setCourse(educationEntity.getCourse());
        educationPojo.setEmployeePojo(employeeMapper.entityToPojo(educationEntity.getEmployeeEntity()));

        return educationPojo;
    }

    public EducationEntity pojoToEntity(EducationPojo educationPojo){
        EducationEntity education = new EducationEntity();
        education.setEducationId(educationPojo.getEducationId());
        education.setQualification(educationPojo.getQualification());
        education.setInstitute(educationPojo.getInstitute());
        education.setCourse(educationPojo.getCourse());
        education.setEmployeeEntity(employeeMapper.pojoToEntity(educationPojo.getEmployeePojo()));

        return education;
    }
}
