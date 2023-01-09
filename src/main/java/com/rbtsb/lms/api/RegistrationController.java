package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.GlobalConstant;
import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.event.RegistrationCompleteEvent;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.PasswordPojo;
import com.rbtsb.lms.pojo.VerificationTokenPojo;
import com.rbtsb.lms.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequestMapping("/api")
@RestController
public class RegistrationController {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody LoginDTO loginDTO, final HttpServletRequest request){
        AppUserPojo user = appUserService.registerUser(loginDTO);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = appUserService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User Verifies Successfully";
        }
        return "Bad User";
    }

    private String applicationUrl(HttpServletRequest request) {


        return "http://" + request.getServerName() +
                ":" + request.getServerPort() + GlobalConstant.ENTITY_PATH_REGISTRATION +
                request.getContextPath();
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request){
        VerificationTokenPojo verificationToken
                = appUserService.generateNewVerificationToken(oldToken);
        AppUserPojo userEmp = verificationToken.getAppUserPojo();

        resendVerificationTokenMail(userEmp, applicationUrl(request), verificationToken);
        return "Verification Link Sent";
    }

    private void resendVerificationTokenMail(AppUserPojo userEmp, String applicationUrl,
                                             VerificationTokenPojo verificationToken){
        String url = applicationUrl + "/verifyRegistration?" +
                "token=" + verificationToken.getToken();

        //Send verification email
        log.info("click the link to verify your account: {}", url);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordPojo passwordPojo, HttpServletRequest request){
        AppUserPojo userEmp = appUserService.findByUsername(passwordPojo.getEmail());
        String url = "";
        if(userEmp!=null){
            String token = UUID.randomUUID().toString();
            appUserService.createPasswordResetTokenForUser(userEmp, token);
            url = passwordResetTokenMail(userEmp, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordPojo passwordPojo){
        String result = appUserService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            log.debug(result);
            return "Invalid Token";
        }
        Optional<AppUserPojo> user = appUserService.getUserByPasswordResetToken(token);
        log.debug(user.toString());
        if(user.isPresent()){
            appUserService.changePassword(user.get(), passwordPojo.getNewPassword());
            return "Password Reset Successfully";
        }
        else{
            return "Invalid Token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordPojo passwordPojo){
        AppUserPojo userEmp = appUserService.findByUsername(passwordPojo.getEmail());
        if(!appUserService.checkIfValidOldPassword(userEmp, passwordPojo.getOldPassword())){
            return "Invalid Old Password";
        }

        //save new password
        appUserService.changePassword(userEmp, passwordPojo.getNewPassword());
        return "Password Changed Successfully.";
    }

    private String passwordResetTokenMail(AppUserPojo userEmp, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?" +
                "token=" + token;

        //Send verification email
        log.info("click the link to reset your password: {}", url);
        return url;
    }
}
