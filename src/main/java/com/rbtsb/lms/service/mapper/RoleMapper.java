package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;

public class RoleMapper {
    public static RoleEntity pojoToEntity(RolePojo rolePojo){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(roleEntity.getRoleId());
        roleEntity.setRoleName(roleEntity.getRoleName());
        return roleEntity;
    }
}
