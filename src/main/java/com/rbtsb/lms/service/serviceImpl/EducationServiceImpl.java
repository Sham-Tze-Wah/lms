package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.EducationRepo;
import com.rbtsb.lms.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationServiceImpl implements EducationService {

    @Autowired
    private EducationRepo educationRepo;

    @Override
    public String insertEducationByEmpId(EducationPojo educationPojo) {
        if(!educationPojo.getQualification().toString().equalsIgnoreCase("")){
            if(!educationPojo.getInstitute().equalsIgnoreCase("")){
                if(!educationPojo.getCourse().toString().equalsIgnoreCase("")){
                    if(!educationPojo.getEmployeePojo().equals(null)){
                        educationRepo.saveAndFlush(educationPojo);
                        return "Insert successfully.";
                    }
                    else{
                        return "Invalid employee pojo";
                    }
                }
                else{
                    return "Invalid course";
                }
            }
            else{
                return "Invalid institute";
            }
        }
        else{
            return "Invalid qualification";
        }
    }

    @Override
    public List<EducationPojo> getAllEducation() {
        return educationRepo.findAll();
    }

    @Override
    public String updateEducationByEmpId(String empId, EducationPojo educationPojo) {
        EducationPojo educationPojoFromDB = educationRepo.selectEducationByEmpId(empId);

        if(!educationPojo.equals(null)){
            if(!educationPojo.getQualification().equals(null)){
                if(!educationPojo.getInstitute().equalsIgnoreCase("")){
                    if(!educationPojo.getCourse().equals(null)){
                        if(!educationPojo.getEmployeePojo().equals(null)){
                            educationPojoFromDB.setQualification(educationPojo.getQualification());
                            educationPojoFromDB.setInstitute(educationPojo.getInstitute());
                            educationPojoFromDB.setCourse(educationPojo.getCourse());
                            educationPojoFromDB.setEmployeePojo(educationPojo.getEmployeePojo());
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
        Optional<EducationPojo> educationPojo = educationRepo.findById(id);
        if(educationPojo.isPresent()){
            educationRepo.deleteById(id);
            return "Deleted successfully.";
        }
        else{
            return "Deleted unsuccessfully.";
        }
    }
}
