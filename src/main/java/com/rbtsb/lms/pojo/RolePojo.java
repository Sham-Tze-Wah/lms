package com.rbtsb.lms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolePojo {
    private String roleId = UUID.randomUUID().toString();
    private String roleName;
}
