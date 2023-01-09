package com.rbtsb.lms.event.listener;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.event.RegistrationCompleteEvent;
import com.rbtsb.lms.pojo.AppUserPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.rbtsb.lms.service.AppUserService;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteListener
        implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private AppUserService AppUserService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification Token for the USER with LINK.

        AppUserPojo user = event.getAppUserPojo();
        String token = UUID.randomUUID().toString();
        AppUserService.saveVerificationTokenForUser(token, user);

        //Send Mail to user
        String url = event.getApplicationUrl() + "/verifyRegistration?" +
                "token=" + token;

        //Send verification email
        log.info("click the link to verify your account: {}", url);
    }


}
