package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.RolePojo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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

    public static AppUserPojo entityToPojo(AppUserEntity appUserEntity){
        AppUserPojo appUserPojo = new AppUserPojo();
        appUserPojo.setId(appUserEntity.getId());
        appUserPojo.setUsername(appUserEntity.getUsername());
        appUserPojo.setPassword(appUserEntity.getPassword());
        appUserPojo.setMatchingPassword(appUserEntity.getMatchingPassword());
        appUserPojo.setEnabled(appUserEntity.isEnabled());
        appUserPojo.setRoles(appUserEntity.getRoles().stream()
                .map(roleEntity -> RoleMapper.entityToPojo(roleEntity)).collect(Collectors.toList()));
        appUserPojo.setEmployeePojo(EmployeeMapper.entityToPojo(appUserEntity.getEmployeeEntity()));
        return appUserPojo;
    }

    public static UserDetails entityToUserDetails(AppUserEntity appUserEntity){
        Collection<SimpleGrantedAuthority> roles = RoleMapper.entityToSGA(appUserEntity.getRoles());
        if(roles == null){
            return new User(appUserEntity.getUsername(), appUserEntity.getPassword(), new ArrayList<>());
        }
        else{
            return new User(appUserEntity.getUsername(), appUserEntity.getPassword(), roles);
        }

    }
}
