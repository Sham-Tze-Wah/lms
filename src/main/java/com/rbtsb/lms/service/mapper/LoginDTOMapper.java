package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.constant.Role;
import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.RoleEntity;

import java.lang.reflect.Array;
import java.util.*;

public class LoginDTOMapper {
    public static AppUserEntity dtoToEntity(LoginDTO loginDTO){
        AppUserEntity appUserEntity = new AppUserEntity();
        Set<RoleEntity> roleEntityList = new HashSet<>();

        appUserEntity.setUsername(loginDTO.getUsername());
        appUserEntity.setPassword(loginDTO.getPassword());
        appUserEntity.setMatchingPassword(loginDTO.getMatchingPassword());

        RoleEntity role = new RoleEntity();
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleName("USER");
        roleEntityList.add(role);
        appUserEntity.setRoles(roleEntityList);
        return appUserEntity;
    }
}
