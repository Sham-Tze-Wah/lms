package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;

public class RoleMapper {
    public static RoleEntity pojoToEntity(RolePojo rolePojo){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(rolePojo.getRoleId());
        roleEntity.setRoleName(rolePojo.getRoleName());
        return roleEntity;
    }

    public static RolePojo entityToPojo(RoleEntity roleEntity){
        RolePojo rolePojo = new RolePojo();
        rolePojo.setRoleId(roleEntity.getRoleId());
        rolePojo.setRoleName(roleEntity.getRoleName());
        return rolePojo;
    }
}
