package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;

import java.util.Set;

public interface RoleService {
    public String insertRole(RolePojo rolePojo);

    public Set<RolePojo> getAllRoles();

    public String updateRoleNameById(String id, String roleName);

    public String deleteRoleById(String id);

    String assignRole(String role, String empId);

    String unassignRole(String role, String empId);

    RoleEntity getRoleByName(String roleName);
}
