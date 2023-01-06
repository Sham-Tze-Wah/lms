package com.rbtsb.lms.util.validation;

import com.rbtsb.lms.pojo.AppUserPojo;

public class AppUserPojoValidation {
    public static AppUserPojo validation(AppUserPojo appUserPojo){
        if(appUserPojo.getId() == null){
            throw new NullPointerException("id is null. Please provide id.");
        }

        if(appUserPojo.getUsername() == null){
            throw new NullPointerException("username is null. Please provide username.");
        }

        if(appUserPojo.getPassword() == null){
            throw new NullPointerException("password is null. Please provide password.");
        }
        return appUserPojo;
    }
}
