package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.repo.WorkExperienceRepo;
import com.rbtsb.lms.service.WorkExperienceService;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
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

    @Autowired
    private WorkExpMapper workExpMapper;

    @Override
    public String insertWorkExperience(WorkExperiencePojo workExperiencePojo) {

        workExperienceRepo.saveAndFlush(workExpMapper.pojoToEntity(workExperiencePojo));
        return "Insert successfully.";
    }

    @Override
    public List<WorkExperiencePojo> getAllWorkExperience() {
        List<WorkExperienceEntity> workExpEntities = workExperienceRepo.findAll();
        List<WorkExperiencePojo> leavePojoList = new ArrayList<>();
        workExpEntities.forEach(workExperienceEntity -> {
            leavePojoList.add(workExpMapper.entityToPojo(workExperienceEntity));
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
        WorkExperiencePojo workExperiencePojoFromDB = workExpMapper.
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
                                workExperiencePojoFromDB.setDateResign(workExperiencePojo.getDateResign());
                                workExperiencePojoFromDB.setCompanyName(workExperiencePojo.getCompanyName());
                                workExperiencePojoFromDB.setEmployeePojo(workExperiencePojo.getEmployeePojo());
                                workExperienceRepo.saveAndFlush(workExpMapper.pojoToEntity(workExperiencePojoFromDB));
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

    @Override
    public List<WorkExperiencePojo> getWorkExperienceByEmpId(String empId) {
        List<WorkExperiencePojo> workExpPojoList = new ArrayList<>();
        List<WorkExperienceEntity> workExpEntityList = workExperienceRepo.findByEmpId(empId);
        if(workExpEntityList != null && !workExpEntityList.isEmpty()){
            for(WorkExperienceEntity workExperienceEntity : workExpEntityList){
                WorkExperiencePojo workExperiencePojo = workExpMapper.entityToPojo(workExperienceEntity);
                workExpPojoList.add(workExperiencePojo);
            }
            return workExpPojoList;
        }
        else{
            return null;
        }
    }
}
