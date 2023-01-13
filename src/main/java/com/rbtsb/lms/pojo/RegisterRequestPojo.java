package com.rbtsb.lms.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestPojo {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
}