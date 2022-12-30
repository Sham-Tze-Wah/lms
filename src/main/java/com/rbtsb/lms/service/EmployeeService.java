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

    public String insertEmployee(EmployeePojo employeePojo);

    public List<EmployeePojo> getAllEmployee();

    public Optional<EmployeePojo> getEmployeeById(String id);

    public Optional<EmployeePojo> getEmployeeByName(String name);

    public String updateEmployeeById(String id, EmployeePojo employeePojo);

    public String deleteEmployeeById(String id);

    public Optional<EmployeePojo> getEmployeePojo(String empId);

    Optional<EmployeeEntity> getEmployeeByEmployeeName(String employeeName);
}
