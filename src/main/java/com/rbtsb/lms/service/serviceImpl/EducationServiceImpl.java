package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EducationEntity;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.EducationRepo;
import com.rbtsb.lms.service.EducationService;
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

        if(!educationPojoList.isEmpty()){
            return educationPojoList;
        }
        else{
            return null;
        }

    }

    @Override
    public String updateEducationByEmpId(String empId, EducationPojo educationPojo) {
        EducationEntity educationPojoFromDB = educationRepo.selectEducationByEmpId(empId);

        if(!educationPojo.equals(null)){
            if(!educationPojo.getQualification().equals(null)){
                if(!educationPojo.getInstitute().equalsIgnoreCase("")){
                    if(!educationPojo.getCourse().equals(null)){
                        if(!educationPojo.getEmployeePojo().equals(null)){
                            educationPojoFromDB.setQualification(educationPojo.getQualification());
                            educationPojoFromDB.setInstitute(educationPojo.getInstitute());
                            educationPojoFromDB.setCourse(educationPojo.getCourse());
                            educationPojoFromDB.setEmployeeEntity(employeeMapper.pojoToEntity(educationPojo.getEmployeePojo()));
                            educationRepo.saveAndFlush(educationPojoFromDB);
                            return "Updated successfully";
                        }
                        else{
                            return "the provided body does not belongs to any employee";
                        }

                    }
                    else{
                        return "the provided body does not have course";
                    }

                }
                else{
                    return "the provided body does not have institute";
                }

            }
            else{
                return "the provided body does not have qualification";
            }

        }
        else{
            return "the empId provided is not exist.";
        }
    }

    @Override
    public String deleteEducationById(String id) {
        Optional<EducationEntity> educationEntity = educationRepo.findById(id);

        if(educationEntity.isPresent()){
            educationRepo.deleteById(id);
            return "Deleted successfully.";
        }
        else{
            return "Deleted unsuccessfully.";
        }
    }

    @Override
    public List<EducationPojo> getEducationByEmpId(String empId) {
        List<EducationEntity> educationEntityList = educationRepo.findByEmpId(empId);
        List<EducationPojo> educationPojoList = new ArrayList<>();

        if(educationEntityList != null && !educationEntityList.isEmpty()){
            for(EducationEntity edu : educationEntityList){
                EducationPojo eduPojo = educationMapper.entityToPojo(edu);
                educationPojoList.add(eduPojo);
            }
            return educationPojoList;
        }
        else{
            throw new NullPointerException("id is not valid. Please provide another id.");
        }
    }
}
