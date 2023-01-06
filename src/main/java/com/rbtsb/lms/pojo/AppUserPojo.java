package com.rbtsb.lms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUserPojo {
    String id = UUID.randomUUID().toString();
    String username;
    String password;
    String matchingPassword;
    boolean enabled = true;
    Collection<RolePojo> roles;
    EmployeePojo employeePojo;
}
