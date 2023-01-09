package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.VerificationTokenPojo;

import java.util.Optional;

public interface AppUserService {
    LoginDTO registerUser(LoginDTO loginDTO);

    void saveVerificationTokenForUser(String token, LoginDTO loginDTO);

    String validateVerificationToken(String token);

    VerificationTokenPojo generateNewVerificationToken(String oldToken);

    AppUserPojo findUserByEmailAndRoleName(String email, String roleName);

    void createPasswordResetTokenForUser(AppUserPojo user, String token);

    String validatePasswordResetToken(String token);

    Optional<AppUserPojo> getUserByPasswordResetToken(String token);

    void changePassword(AppUserPojo AppUser, String newPassword);

    boolean checkIfValidOldPassword(AppUserPojo AppUser, String oldPassword);

}
