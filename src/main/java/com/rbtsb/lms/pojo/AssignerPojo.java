package com.rbtsb.lms.pojo;

import com.rbtsb.lms.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignerPojo {
    private String id = UUID.randomUUID().toString();
    private EmployeePojo employeePojo;
}
