package com.rbtsb.lms.api;

import com.rbtsb.lms.dao.AppUserDao;
import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", exposedHeaders = "token")
public class AuthenticationController {

    //https://www.javainuse.com/spring/boot-jwt

    private final AuthenticationManager authenticationManager;
    //private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AppUserDao userDao;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping(path = "/login")
    private String generateAuthorizationHeader(LoginDTO request){
        return jwtUtils.generateToken(new User(request.getUsername(), request.getPassword(), new ArrayList<>()));
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginDTO request
    ){
        try{
            log.info("Authentication header: " + generateAuthorizationHeader(request));
            authenticate(request.getUsername(), request.getPassword());
            final UserDetails user = userDao.findByEmail(request.getUsername());//userDetailsService.loadUserByUsername(request.getUsername());
            log.info(user.toString());
            if(user != null){
                return new ResponseEntity<>(jwtUtils.generateToken(user), HttpStatus.OK);
            }
            return new ResponseEntity<>("failed to login. Please contact the system administration.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    //https://stackoverflow.com/questions/39789408/how-to-handle-spring-security-internalauthenticationserviceexception-thrown-in-s
    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            log.info("authenticate");
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            log.info(token.toString());
            authenticationManager.authenticate(token);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch(InternalAuthenticationServiceException e){
            throw new Exception("NO IDEA", e);
        }
    }
}
