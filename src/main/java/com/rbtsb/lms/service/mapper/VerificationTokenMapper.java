package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.VerificationTokenEntity;
import com.rbtsb.lms.pojo.VerificationTokenPojo;

public class VerificationTokenMapper {
    public static VerificationTokenEntity pojoToEntity(VerificationTokenPojo verificationTokenPojo){
        VerificationTokenEntity entity = new VerificationTokenEntity();
        entity.setId(verificationTokenPojo.getId());
        entity.setToken(verificationTokenPojo.getToken());
        entity.setExpirationTime(verificationTokenPojo.getExpirationTime());

        AppUserEntity appUserEntity = AppUserMapper.pojoToEntity(verificationTokenPojo.getAppUserPojo());
        entity.setAppUserEntity(appUserEntity);
        return entity;
    }

    public static VerificationTokenPojo entityToPojo(VerificationTokenEntity byToken) {
        VerificationTokenPojo pojo = new VerificationTokenPojo();
        pojo.setId(byToken.getId());
        pojo.setToken(byToken.getToken());
        pojo.setExpirationTime(byToken.getExpirationTime());
        pojo.setAppUserPojo(AppUserMapper.entityToPojo(byToken.getAppUserEntity()));
        return pojo;
    }
}
