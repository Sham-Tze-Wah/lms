package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {
    public static EmployeePojo entityToPojo(EmployeeEntity employeeEntity){
        EmployeePojo emp = new EmployeePojo();
        emp.setEmpId(employeeEntity.getEmpId());
        emp.setName(employeeEntity.getName());
        emp.setEmail(employeeEntity.getEmail());
        emp.setPhoneNo(employeeEntity.getPhoneNo());
        emp.setPosition(employeeEntity.getPosition());
        emp.setAddress(employeeEntity.getAddress());
        emp.setDateJoined(employeeEntity.getDateJoined());
        emp.setDateLeave(employeeEntity.getDateLeave());

        return emp;
    }

    public static EmployeeEntity pojoToEntity(EmployeePojo employeePojo){
        EmployeeEntity emp = new EmployeeEntity();
        emp.setEmpId(employeePojo.getEmpId());
        emp.setName(employeePojo.getName());
        emp.setEmail(employeePojo.getEmail());
        emp.setPhoneNo(employeePojo.getPhoneNo());
        emp.setPosition(employeePojo.getPosition());
        emp.setAddress(employeePojo.getAddress());
        emp.setDateJoined(employeePojo.getDateJoined());
        emp.setDateLeave(employeePojo.getDateLeave());

        return emp;
    }
}
