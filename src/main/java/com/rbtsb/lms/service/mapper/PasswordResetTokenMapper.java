package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.PasswordResetTokenEntity;
import com.rbtsb.lms.pojo.PasswordResetTokenPojo;

public class PasswordResetTokenMapper {
    public static PasswordResetTokenEntity pojoToEntity(PasswordResetTokenPojo passwordResetTokenPojo){
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setId(passwordResetTokenPojo.getId());
        passwordResetTokenEntity.setToken(passwordResetTokenPojo.getToken());
        passwordResetTokenEntity.setExpirationTime(passwordResetTokenPojo.getExpirationTime());
        passwordResetTokenEntity.setAppUserEntity(passwordResetTokenEntity.getAppUserEntity());
        return passwordResetTokenEntity;
    }

    public static PasswordResetTokenPojo entityToPojo(PasswordResetTokenEntity passwordResetTokenEntity) {
        PasswordResetTokenPojo passwordResetTokenPojo = new PasswordResetTokenPojo();
        passwordResetTokenPojo.setId(passwordResetTokenEntity.getId());
        passwordResetTokenPojo.setToken(passwordResetTokenEntity.getToken());
        passwordResetTokenPojo.setExpirationTime(passwordResetTokenEntity.getExpirationTime());
        passwordResetTokenPojo.setAppUserPojo(AppUserMapper.entityToPojo(passwordResetTokenEntity.getAppUserEntity()));
        return passwordResetTokenPojo;
    }
}
