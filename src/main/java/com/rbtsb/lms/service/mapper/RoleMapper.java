package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class RoleMapper {
    public static RoleEntity pojoToEntity(RolePojo rolePojo){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(rolePojo.getRoleId());
        roleEntity.setRoleName(rolePojo.getRoleName());
        return roleEntity;
    }

    public static RolePojo entityToPojo(RoleEntity roleEntity){
        RolePojo rolePojo = new RolePojo(roleEntity.getRoleId(), roleEntity.getRoleName());
        return rolePojo;
    }

    public static Collection<SimpleGrantedAuthority> entityToSGA(Collection<RoleEntity> roles) {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority("ROLE_"+role.getRoleName())).collect(Collectors.toSet());
    }

    public static Collection<SimpleGrantedAuthority> pojoToSGA(Collection<RolePojo> roles) {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority("ROLE_"+role.getRoleName())).collect(Collectors.toSet());
    }
}
