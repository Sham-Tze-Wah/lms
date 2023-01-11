package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.Course;
import com.rbtsb.lms.constant.Qualification;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EducationEntity;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.EducationRepo;
import com.rbtsb.lms.service.EducationService;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.mapper.AttachmentMapper;
import com.rbtsb.lms.service.mapper.EducationMapper;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EducationServiceImpl implements EducationService {

    @Autowired
    private EducationRepo educationRepo;

    @Autowired
    private EmployeeService employeeService; //TODO might be wrong

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public String insertEducationByEmpId(EducationPojo educationPojo) {
        educationRepo.saveAndFlush(educationMapper.pojoToEntity(educationPojo));
        return educationPojo.getEmployeePojo().toString() + " Insert successfully.";
    }

    @Override
    public List<EducationPojo> getAllEducation() {
        List<EducationEntity> educationEntities = educationRepo.findAll();
        List<EducationPojo> educationPojoList = new ArrayList<>();
        educationEntities.forEach(educationEntity -> {
            try {
                educationPojoList.add(educationMapper.entityToPojo(educationEntity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (!educationPojoList.isEmpty()) {
            return educationPojoList;
        } else {
            return null;
        }

    }

    @Override
    public String updateEducationByEmpId(String eduId, String qualification, String institute, String course, String employeeId) {
        if (employeeId != null && !employeeId.equalsIgnoreCase("")) {
            Optional<EmployeePojo> emp = employeeService.getEmployeeById(employeeId);
            if (emp.isPresent()) {
                if (eduId != null && !eduId.equalsIgnoreCase("")) {
                    Optional<EducationEntity> educationEntityFromDB = educationRepo.findById(eduId);

                    if (educationEntityFromDB != null) {
                        EducationPojo educationPojoFromDB = educationMapper.entityToPojo(educationEntityFromDB.get());
                        if (qualification != null && !qualification.equalsIgnoreCase("")) {
                            if (institute != null && !institute.equalsIgnoreCase("")) {
                                if (course != null && !course.equalsIgnoreCase("")) {
                                    educationPojoFromDB.setEducationId(eduId);
                                    educationPojoFromDB.setQualification(Qualification.valueOf(qualification));
                                    educationPojoFromDB.setInstitute(institute);
                                    educationPojoFromDB.setCourse(Course.valueOf(course));
                                    educationPojoFromDB.setEmployeePojo(emp.get());
                                    educationRepo.saveAndFlush(educationMapper.pojoToEntity(educationPojoFromDB));
                                    return "Updated successfully";
                                } else {
                                    return "the provided body does not have course";
                                }

                            } else {
                                return "the provided body does not have institute";
                            }

                        } else {
                            return "the provided body does not have qualification";
                        }
                    } else {
                        return "the id does not exist";
                    }
                } else {
                    return "the id provided is null.";
                }
            } else {
                throw new NullPointerException("employee id provided is not exist.");
            }

        } else {
            throw new NullPointerException("employee id is empty.");
        }


    }

    @Override
    public String deleteEducationById(String id) {
        Optional<EducationEntity> educationEntity = educationRepo.findById(id);

        if (educationEntity.isPresent()) {
            educationRepo.deleteById(id);
            return "Deleted successfully.";
        } else {
            return "Deleted unsuccessfully.";
        }
    }

    @Override
    public List<EducationPojo> getEducationByEmpId(String empId) {
        List<EducationEntity> educationEntityList = educationRepo.findByEmpId(empId);
        List<EducationPojo> educationPojoList = new ArrayList<>();

        if (educationEntityList != null && !educationEntityList.isEmpty()) {
            for (EducationEntity edu : educationEntityList) {
                EducationPojo eduPojo = educationMapper.entityToPojo(edu);
                educationPojoList.add(eduPojo);
            }
            return educationPojoList;
        } else {
            throw new NullPointerException("id is not valid. Please provide another id.");
        }
    }
}
