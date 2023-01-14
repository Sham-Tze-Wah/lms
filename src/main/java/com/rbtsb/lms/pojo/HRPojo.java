package com.rbtsb.lms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HRPojo {
    private String id = UUID.randomUUID().toString();
    private EmployeePojo employeePojo;
}
