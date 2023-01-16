package com.rbtsb.lms.util.validation;

import com.rbtsb.lms.service.ValidatorService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
public class MailValidation implements ValidatorService {
    public String validateEmail(String email) {
        String errorCode = "";

        try{
            if(EmailValidator.getInstance().isValid(email)){
                errorCode = "000";
            }
            else{
                errorCode = "998";
            }
        }
        catch(Exception ex){
            return "999";
        }

        return errorCode;
    }

}
