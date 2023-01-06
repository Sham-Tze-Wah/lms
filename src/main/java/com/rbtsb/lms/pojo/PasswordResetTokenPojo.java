package com.rbtsb.lms.pojo;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PasswordResetTokenPojo {
    private String id = UUID.randomUUID().toString();
    private String token;
    private Date expirationTime;
    private AppUserPojo appUserPojo;
}
