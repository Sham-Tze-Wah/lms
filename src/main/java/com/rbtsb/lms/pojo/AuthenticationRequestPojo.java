package com.rbtsb.lms.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequestPojo {
    private String id = UUID.randomUUID().toString();
    private String email;
    private String password;
}
