package com.rbtsb.lms.event;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.pojo.AppUserPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private LoginDTO loginDTO;
    private String applicationUrl;

    public RegistrationCompleteEvent(LoginDTO loginDTO, String applicationUrl ){
        super(loginDTO);
        this.loginDTO = loginDTO;
        this.applicationUrl = applicationUrl;
    }
}
