package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.RolePojo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppUserMapper {

    public static AppUserEntity pojoToEntity(AppUserPojo appUserPojo){
        AppUserEntity appUserEntity = new AppUserEntity();
        List<RoleEntity> roleEntityList = new ArrayList<>();
        appUserEntity.setId(appUserPojo.getId());
        appUserEntity.setUsername(appUserPojo.getUsername());
        appUserEntity.setPassword(appUserPojo.getPassword());
        appUserEntity.setMatchingPassword(appUserPojo.getMatchingPassword());
        appUserEntity.setEnabled(appUserPojo.isEnabled());
        for(RolePojo role: appUserPojo.getRoles()){
            roleEntityList.add(RoleMapper.pojoToEntity(role));
        }
        appUserEntity.setRoles(roleEntityList);
        appUserEntity.setEmployeeEntity(EmployeeMapper.pojoToEntity(appUserPojo.getEmployeePojo()));
        return appUserEntity;
    }
}