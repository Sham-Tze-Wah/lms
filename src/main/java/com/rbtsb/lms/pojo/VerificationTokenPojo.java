package com.rbtsb.lms.pojo;

import com.rbtsb.lms.entity.AppUserEntity;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class VerificationTokenPojo {
    private String id = UUID.randomUUID().toString();
    private String token;
    private Date expirationTime;
    private AppUserPojo appUserPojo;
}
