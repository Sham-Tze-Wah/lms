package com.rbtsb.lms.pojo;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesPojo {
    private String id = UUID.randomUUID().toString();
    private AppUserEntity appUserEntity;
    private RoleEntity roleEntity;
}
