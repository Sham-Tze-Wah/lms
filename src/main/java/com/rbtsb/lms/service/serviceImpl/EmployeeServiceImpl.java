package com.rbtsb.lms.service.serviceImpl;


import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.EducationEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.mapper.EducationMapper;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public String insertEmployee(EmployeePojo employeePojo) {
        if(!employeePojo.getName().equalsIgnoreCase("")){
            if(!employeePojo.getPhoneNo().equalsIgnoreCase("")){
                if(!employeePojo.getEmail().equalsIgnoreCase("")){
                    if(!employeePojo.getPosition().equals(null)){
                        if(!employeePojo.getRole().equals(null)){
                            if(employeePojo.getDateJoined().equals(null)){
                                employeeRepo.saveAndFlush(EmployeeMapper.pojoToEntity(employeePojo));
                                return "Insert successfully.";
                            }
                            else{
                                employeePojo.setDateJoined(new Date());
                                employeeRepo.saveAndFlush(EmployeeMapper.pojoToEntity(employeePojo));
                                return "Insert successfully.";
                            }
                        }
                        else{
                            return "Role cannot be null.";
                        }
                    }
                    else{
                        return "Position cannot be null";
                    }
                }
                else {
                    return "email cannot be null";
                }
            }
            else{
                return "phone no cannot be null";
            }
        }
        else{
            return "name cannot be null.";
        }
    }

    @Override
    public List<EmployeePojo> getAllEmployee() {
        List<EmployeeEntity> employeeEntities = employeeRepo.findAll();
        List<EmployeePojo> employeePojoList = new ArrayList<>();
        employeeEntities.forEach(employeeEntity -> {
            employeePojoList.add(EmployeeMapper.entityToPojo(employeeEntity));
        });

        if(!employeePojoList.isEmpty()){
            return employeePojoList;
        }
        else{
            return null;
        }

    }

    @Override
    public Optional<EmployeePojo> getEmployeeById(String id) {
        Optional<EmployeeEntity> emp = employeeRepo.findById(id);
        if(emp.isPresent()){
            return Optional.of(EmployeeMapper.entityToPojo(emp.get()));
        }
        else{
            return null;
        }
    }

    @Override
    public String updateEmployeeById(String id, EmployeePojo employeePojo) {
        Optional<EmployeeEntity> emp = employeeRepo.findById(id);
        if(emp.isPresent()){
            if(!employeePojo.getName().equalsIgnoreCase("")){
                if(!employeePojo.getPhoneNo().equalsIgnoreCase("")){
                    if(!employeePojo.getEmail().equalsIgnoreCase("")){
                        if(!employeePojo.getPosition().equals(null)){
                            if(!employeePojo.getRole().equals(null)){
                                if(employeePojo.getDateJoined().equals(null)){
                                    emp.get().setName(employeePojo.getName());
                                    emp.get().setEmail(employeePojo.getEmail());
                                    emp.get().setAddress(employeePojo.getAddress());
                                    emp.get().setPhoneNo(employeePojo.getPhoneNo());
                                    emp.get().setDateJoined(employeePojo.getDateJoined());
                                    emp.get().setDateLeave(employeePojo.getDateLeave());
                                    emp.get().setPosition(employeePojo.getPosition());
                                    emp.get().setRole(employeePojo.getRole());
                                    employeeRepo.saveAndFlush(emp.get());
                                    return "updated successfully.";
                                }
                                else{
                                    employeePojo.setDateJoined(new Date());
                                    employeeRepo.saveAndFlush(EmployeeMapper.pojoToEntity(employeePojo));
                                    return "Insert successfully.";
                                }
                            }
                            else{
                                return "Role cannot be null.";
                            }
                        }
                        else{
                            return "Position cannot be null";
                        }
                    }
                    else {
                        return "email cannot be null";
                    }
                }
                else{
                    return "phone no cannot be null";
                }
            }
            else{
                return "name cannot be null.";
            }
        }
        else{
            return "update unsucessfully.";
        }
    }

    @Override
    public String deleteEmployeeById(String id) {
        Optional<EmployeeEntity> emp = employeeRepo.findById(id);
        if(emp.isPresent()){
            employeeRepo.delete(emp.get());
            return "deleted successfully.";
        }
        else{
            return "deleted unsuccessfully.";
        }
    }






}
