package com.rbtsb.lms.event.listener;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.event.RegistrationCompleteEvent;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.service.mapper.AppUserMapper;
import com.rbtsb.lms.service.mapper.RoleMapper;
import com.rbtsb.lms.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.rbtsb.lms.service.AppUserService;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteListener
        implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private AppUserService AppUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification Token for the USER with LINK.

        AppUserPojo appUser = event.getAppUserPojo();
        UserDetails user = new User(appUser.getUsername(), appUser.getPassword(), RoleMapper.pojoToSGA(appUser.getRoles()));
        String token = jwtUtils.generateToken(user);
        AppUserService.saveVerificationTokenForUser(token, appUser);

        //Send Mail to user
        String url = event.getApplicationUrl() + "/verifyRegistration?" +
                "token=" + token;

        //Send verification email
        log.info("click the link to verify your account: {}", url);
    }


}
