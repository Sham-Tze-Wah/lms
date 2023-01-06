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
}
