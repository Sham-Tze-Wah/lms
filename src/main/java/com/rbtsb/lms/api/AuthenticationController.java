package com.rbtsb.lms.api;

import com.rbtsb.lms.dao.AppUserDao;
import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    //https://www.javainuse.com/spring/boot-jwt

    private final AuthenticationManager authenticationManager;
    //private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AppUserDao userDao;

    @PostMapping(path = "/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginDTO request
    ){
        try{
            authenticate(request.getUsername(), request.getPassword());
            final UserDetails user = userDao.findByEmail(request.getUsername());//userDetailsService.loadUserByUsername(request.getUsername());
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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
