package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.util.DateTimeUtil;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EmployeeMapper {
    public static EmployeePojo entityToPojo(EmployeeEntity employeeEntity) {
        EmployeePojo emp = new EmployeePojo();
        emp.setEmpId(employeeEntity.getEmpId());
        if(employeeEntity.getName() == null || employeeEntity.getName().equalsIgnoreCase("")){
            throw new NullPointerException("name is null");
        }
        else if(employeeEntity.getEmail() == null || employeeEntity.getEmail().equalsIgnoreCase("")){
            throw new NullPointerException("email is null");
        }
        else if(employeeEntity.getPhoneNo() == null || employeeEntity.getPhoneNo().equalsIgnoreCase("")){
            throw new NullPointerException("phone no is null");
        }
        else if(employeeEntity.getAddress() == null || employeeEntity.getAddress().equalsIgnoreCase("")){
            throw new NullPointerException("address is null");
        }
        else if(employeeEntity.getDateJoined() == null){
            throw new NullPointerException("date joined is null");
        }

        emp.setName(employeeEntity.getName());
        emp.setEmail(employeeEntity.getEmail());
        emp.setPhoneNo(employeeEntity.getPhoneNo());
        emp.setPosition(employeeEntity.getPosition());
        emp.setAddress(employeeEntity.getAddress());
        emp.setDateJoined(employeeEntity.getDateJoined());

        if(employeeEntity.getDateJoined() != null)
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
