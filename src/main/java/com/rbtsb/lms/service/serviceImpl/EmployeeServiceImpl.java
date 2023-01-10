package com.rbtsb.lms.service.serviceImpl;


import com.rbtsb.lms.constant.Position;
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
import com.rbtsb.lms.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private EmployeeMapper employeeMapper;

    private Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public String insertEmployee(EmployeePojo employeePojo) {
        //System.out.println(employeePojo);

        log.debug("Employee Entity: "+employeePojo.toString());
        employeeRepo.save(employeeMapper.pojoToEntity(employeePojo));
        return "Insert successfully.";
    }

    @Override
    public List<EmployeePojo> getAllEmployee() {
        try{
            List<EmployeeEntity> employeeEntities = employeeRepo.findAll();
            List<EmployeePojo> employeePojoList = new ArrayList<>();
            employeeEntities.forEach(employeeEntity -> {
                try {
                    employeePojoList.add(employeeMapper.entityToPojo(employeeEntity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if(!employeePojoList.isEmpty()){
                return employeePojoList;
            }
            else{
                return null;
            }
        }
        catch(Exception ex){
            return null;
        }
    }

    @Override
    public Optional<EmployeePojo> getEmployeeById(String id) {
        Optional<EmployeeEntity> emp = employeeRepo.findById(id);
        try{
            if(emp.isPresent()){
                log.debug("Employee Entity: " + emp.get().toString());
                return Optional.of(employeeMapper.entityToPojo(emp.get()));
            }
            else{
                return null;
            }
        }
        catch(Exception ex){
            return null;
        }
    }

    @Override
    public Optional<EmployeePojo> getEmployeeByName(String name) {
        Optional<EmployeeEntity> empEntity = employeeRepo.findByName(name);
        //Map entity to pojo
        try{
            if(empEntity.isPresent()){
                EmployeePojo employeePojo = employeeMapper.entityToPojo(empEntity.get());
                return Optional.ofNullable(Optional.of(employeePojo)).orElse(null);
            }
            else{
                throw new NullPointerException("name is not exist. Please try another name");
            }
        }
        catch(Exception ex){
            return null;
        }
    }

    @Override
    public String updateEmployeeById(String id, String name, String phoneNo, String email, String address, String position, String dateJoined, String dateLeave) {
        try{
            if(id != null && !id.equalsIgnoreCase("")){
                Optional<EmployeeEntity> emp = employeeRepo.findById(id);
                if(emp.isPresent()){
                    String emp_id = emp.get().getEmpId();
                    if(!name.equalsIgnoreCase("")){
                        if(!phoneNo.equalsIgnoreCase("")){
                            if(!email.equalsIgnoreCase("")){
                                if(!position.equals(null)){
                                    //if(!employeePojo.getRole().equals(null)){

                                    int result = 0;
                                    if(dateJoined != null){

                                        if(dateLeave != null && !dateLeave.equalsIgnoreCase("")){
                                            emp.get().setDateJoined(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateJoined)));
                                            emp.get().setDateLeave(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateLeave)));
                                            //emp.get().setRole(employeePojo.getRole());
//                                        employeeRepo.saveAndFlush(emp.get());
//                                        result =employeeRepo.updateByEmployee(
//                                                employeePojo.getName(),
//                                                employeePojo.getEmail(),
//                                                employeePojo.getAddress(),
//                                                employeePojo.getPhoneNo(),
//                                                employeePojo.getDateJoined(),
//                                                employeePojo.getDateLeave(),
//                                                employeePojo.getPosition(),
//                                                employeePojo.getRole(),
//                                                emp_id
//                                        );
//                                        result = 1;
//                                        return result + " updated successfully.";
                                        }
                                        else{
//                                        result = employeeRepo.save()
//                                        updateByEmployee(
//                                                employeePojo.getName(),
//                                                employeePojo.getEmail(),
//                                                employeePojo.getAddress(),
//                                                employeePojo.getPhoneNo(),
//                                                employeePojo.getDateJoined(),
//                                                null,
//                                                employeePojo.getPosition(),
//                                                employeePojo.getRole(),
//                                                emp_id
//                                        );


                                            emp.get().setDateJoined(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateJoined)));
                                            emp.get().setDateLeave(null);
                                            //emp.get().setRole(employeePojo.getRole());
                                        }
                                    }
                                    else{
                                        if(dateLeave != null && !dateLeave.equalsIgnoreCase("")){
//                                        result = employeeRepo.updateByEmployee(
//                                                employeePojo.getName(),
//                                                employeePojo.getEmail(),
//                                                employeePojo.getAddress(),
//                                                employeePojo.getPhoneNo(),
//                                                new Date(),
//                                                employeePojo.getDateLeave(),
//                                                employeePojo.getPosition(),
//                                                employeePojo.getRole(),
//                                                emp_id
//                                        );
                                            emp.get().setDateJoined(new Date());
                                            emp.get().setDateLeave(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateLeave)));
                                        }
                                        else{
//                                        result = employeeRepo.updateByEmployee(
//                                                employeePojo.getName(),
//                                                employeePojo.getEmail(),
//                                                employeePojo.getAddress(),
//                                                employeePojo.getPhoneNo(),
//                                                new Date(),
//                                                null,
//                                                employeePojo.getPosition(),
//                                                employeePojo.getRole(),
//                                                emp_id
//                                        );
                                            emp.get().setDateJoined(new Date());
                                            emp.get().setDateLeave(null);
                                        }
                                    }
                                    emp.get().setName(name);
                                    emp.get().setEmail(email);
                                    emp.get().setAddress(address);
                                    emp.get().setPhoneNo(phoneNo);
                                    emp.get().setPosition(Position.valueOf(position));
                                    employeeRepo.saveAndFlush(emp.get());
                                    result = 1;
                                    return result + " updated successfully.";
                                    //}
//                            else{
//                                return "Role cannot be null.";
//                            }
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
                    return "update unsucessfully due to id is not exist.";
                }
            }
            else{
                return "id is null.";
            }
        }
        catch(ParseException paEx){
            return "update unsuccessfully due to "+paEx.toString();
        }
        catch(Exception ex){
            return "update unsuccessfully due to "+ex.toString();
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

    @Override
    public Optional<EmployeePojo> getEmployeePojo(String empId) {
        Optional<EmployeeEntity> entity = employeeRepo.findById(empId);
        try{
            if(entity.isPresent()){
                return Optional.of(employeeMapper.entityToPojo(entity.get()));
            }
            else{
                return null;
            }
        }
        catch(Exception ex){
            return null;
        }

    }

//    @Override
//    public Optional<EmployeeEntity> getEmployeeByEmployeeName(String employeeName) {
//        return employeeRepo.findByName(employeeName);
//    }


}
