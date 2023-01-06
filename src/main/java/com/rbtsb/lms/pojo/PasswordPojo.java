package com.rbtsb.lms.pojo;

import lombok.Data;

@Data
public class PasswordPojo {
    private String email;
    private String oldPassword;
    private String newPassword;
}
