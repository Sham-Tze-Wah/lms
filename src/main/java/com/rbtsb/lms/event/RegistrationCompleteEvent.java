package com.rbtsb.lms.event;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.pojo.AppUserPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private AppUserPojo appUserPojo;
    private String applicationUrl;

    public RegistrationCompleteEvent(AppUserPojo appUserPojo, String applicationUrl ){
        super(appUserPojo);
        this.appUserPojo = appUserPojo;
        this.applicationUrl = applicationUrl;
    }
}
