package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

     String insertEmployee(EmployeePojo employeePojo);

     List<EmployeePojo> getAllEmployee();

     Optional<EmployeePojo> getEmployeeById(String id);

     Optional<EmployeePojo> getEmployeeByName(String name);

     String updateEmployeeById(String id, EmployeePojo employeePojo);

     String deleteEmployeeById(String id);

     Optional<EmployeePojo> getEmployeePojo(String empId);

    //Optional<EmployeeEntity> getEmployeeByEmployeeName(String employeeName);
}
