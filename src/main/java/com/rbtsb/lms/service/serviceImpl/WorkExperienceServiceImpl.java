package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.repo.WorkExperienceRepo;
import com.rbtsb.lms.service.WorkExperienceService;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.service.mapper.WorkExpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {

    @Autowired
    private WorkExperienceRepo workExperienceRepo;

    @Override
    public String insertWorkExperience(WorkExperiencePojo workExperiencePojo) {
        if(!workExperiencePojo.equals(null)){
            if(!workExperiencePojo.getWorkTitle().equals(null)){
                if(!workExperiencePojo.getCompanyName().equalsIgnoreCase("")){
                    if(!workExperiencePojo.getYearsOfExperience().equalsIgnoreCase("")){
                        if(!workExperiencePojo.getDateJoined().equals(null)){
                            workExperienceRepo.saveAndFlush(WorkExpMapper.pojoToEntity(workExperiencePojo));
                            return "Insert successfully.";
                        }
                        else{
                            return "date joined cannot be null";
                        }
                    }
                    else{
                        return "work experience cannot be null. If work less than 1 year, just write 1.";
                    }
                }
                else{
                    return "company name cannot be null";
                }

            }
            else{
                return "work title cannot be null";
            }
        }
        else{
            return "requested body cannot be null";
        }
    }

    @Override
    public List<WorkExperiencePojo> getAllWorkExperience() {
        List<WorkExperienceEntity> workExpEntities = workExperienceRepo.findAll();
        List<WorkExperiencePojo> leavePojoList = new ArrayList<>();
        workExpEntities.forEach(workExperienceEntity -> {
            leavePojoList.add(WorkExpMapper.entityToPojo(workExperienceEntity));
        });

        if(!leavePojoList.isEmpty()){
            return leavePojoList;
        }
        else{
            return null;
        }
    }

    @Override
    public String updateWorkExperienceByEmpId(String empId, WorkExperiencePojo workExperiencePojo) {
        WorkExperiencePojo workExperiencePojoFromDB = WorkExpMapper.
                entityToPojo(workExperienceRepo.selectWorkExperienceByEmpId(empId));
        if(!workExperiencePojoFromDB.equals(null)){
            if(!workExperiencePojo.getWorkTitle().equals(null)){
                if(!workExperiencePojo.getYearsOfExperience().equalsIgnoreCase("")){
                    if(!workExperiencePojo.getCompanyName().equalsIgnoreCase("")){
                        if(!workExperiencePojo.getDateJoined().equals(null)){
                            if(!workExperiencePojo.getEmployeePojo().equals(null)){
                                workExperiencePojoFromDB.setWorkTitle(workExperiencePojo.getWorkTitle());
                                workExperiencePojoFromDB.setYearsOfExperience(workExperiencePojo.getYearsOfExperience());
                                workExperiencePojoFromDB.setDateJoined(workExperiencePojo.getDateJoined());
                                workExperiencePojoFromDB.setDateLeave(workExperiencePojo.getDateLeave());
                                workExperiencePojoFromDB.setCompanyName(workExperiencePojo.getCompanyName());
                                workExperiencePojoFromDB.setEmployeePojo(workExperiencePojo.getEmployeePojo());
                                workExperienceRepo.saveAndFlush(WorkExpMapper.pojoToEntity(workExperiencePojoFromDB));
                                return "Updated successfully";
                            }
                            else{
                                return "Updated unsuccessfully due to employee is empty";
                            }
                        }
                        else{
                            return "Updated unsuccessfully due to date joined is empty";
                        }
                    }
                    else{
                        return "Updated unsuccessfully due to company name is empty";
                    }
                }
                else{
                    return "Updated unsuccessfully due to year of experience not exists";
                }
            }
            else{
                return "Updated unsuccessfully due to empty work title";
            }

        }
        else{
            return "Updated unsuccessfully due to id not exists";
        }
    }

    @Override
    public String deleteWorkExperienceById(String id) {
        Optional<WorkExperienceEntity> workExp = workExperienceRepo.findById(id);
        if(workExp.isPresent()){
            workExperienceRepo.delete(workExp.get());
            return "work deleted successfully.";
        }
        else{
            return "work exp is empty";
        }
    }
}
