package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.repo.WorkExperienceRepo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.WorkExperienceService;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.service.mapper.WorkExpMapper;
import com.rbtsb.lms.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {

    @Autowired
    private WorkExperienceRepo workExperienceRepo;

    @Autowired
    private EmployeeService employeeService;

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
            try {
                leavePojoList.add(workExpMapper.entityToPojo(workExperienceEntity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (!leavePojoList.isEmpty()) {
            return leavePojoList;
        } else {
            return null;
        }
    }

    @Override
    public String updateWorkExperience(String workId, String workTitle, String yearsOfExperience, String companyName, String dateJoined, String dateResign, String empId) {
        try{
            if (workId != null && !workId.equalsIgnoreCase("")) {
                Optional<WorkExperienceEntity> workExperienceEntityFromDB = workExperienceRepo.findById(workId);

                if (workExperienceEntityFromDB.isPresent()) {
                    WorkExperiencePojo workExperiencePojoFromDB = workExpMapper.entityToPojo(workExperienceEntityFromDB.get());

                    if (empId != null && !empId.equalsIgnoreCase("")) {
                        Optional<EmployeePojo> empPojo = employeeService.getEmployeeById(empId);

                        if (empPojo.isPresent()) {

                            if (workTitle != null && !workTitle.equalsIgnoreCase("")) {

                                if (yearsOfExperience != null && !yearsOfExperience.equalsIgnoreCase("")) {

                                    if (companyName != null && !companyName.equalsIgnoreCase("")) {

                                        if (dateJoined != null && !dateJoined.equalsIgnoreCase("")) {

                                            if(dateResign != null && !dateResign.equalsIgnoreCase("")){
                                                workExperiencePojoFromDB.setWorkTitle(Position.valueOf(workTitle));
                                                workExperiencePojoFromDB.setYearsOfExperience(yearsOfExperience);
                                                workExperiencePojoFromDB.setDateJoined(DateTimeUtil.stringToDate(dateJoined));
                                                workExperiencePojoFromDB.setDateResign(DateTimeUtil.stringToDate(dateResign));
                                                workExperiencePojoFromDB.setCompanyName(companyName);
                                                workExperiencePojoFromDB.setEmployeePojo(empPojo.get());
                                                workExperienceRepo.saveAndFlush(workExpMapper.pojoToEntity(workExperiencePojoFromDB));
                                                return "Updated successfully";
                                            }
                                            else{
                                                return "Updated unsuccessfully due to date resign is empty";
                                            }

                                        } else {
                                            return "Updated unsuccessfully due to date joined is empty";
                                        }
                                    } else {
                                        return "Updated unsuccessfully due to company name is empty";
                                    }
                                } else {
                                    return "Updated unsuccessfully due to year of experience not exists";
                                }
                            }else{
                                return "Updated unsuccessfully due to work title is null";
                            }
                        } else {
                            return "Updated unsuccessfully due to emp id is not exist";
                        }
                    } else {
                        return "Updated unsuccessfully due to emp id is null";
                    }
                } else {
                    return "Updated unsuccessfully due to work id is not exist";
                }
            } else {
                return "Updated unsuccessfully due to work id is null";
            }
        }
        catch(ParseException paEx){
            return paEx.toString();
        }
        catch(Exception ex){
            return ex.toString();
        }
    }

    @Override
    public String deleteWorkExperienceById(String id) {
        Optional<WorkExperienceEntity> workExp = workExperienceRepo.findById(id);
        if (workExp.isPresent()) {
            workExperienceRepo.delete(workExp.get());
            return "work deleted successfully.";
        } else {
            return "work exp is empty";
        }
    }

    @Override
    public List<WorkExperiencePojo> getWorkExperienceByEmpId(String empId) {
        List<WorkExperiencePojo> workExpPojoList = new ArrayList<>();
        List<WorkExperienceEntity> workExpEntityList = workExperienceRepo.findByEmpId(empId);
        if (workExpEntityList != null && !workExpEntityList.isEmpty()) {
            for (WorkExperienceEntity workExperienceEntity : workExpEntityList) {
                WorkExperiencePojo workExperiencePojo = workExpMapper.entityToPojo(workExperienceEntity);
                workExpPojoList.add(workExperiencePojo);
            }
            return workExpPojoList;
        } else {
            return null;
        }
    }
}
