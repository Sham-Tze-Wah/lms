package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.error.exception.ResourceNotFoundException;
import com.rbtsb.lms.pojo.AuthenticationRequestPojo;
import com.rbtsb.lms.pojo.AuthenticationResponsePojo;
import com.rbtsb.lms.pojo.RegisterRequestPojo;
import com.rbtsb.lms.repo.AppUserRepo;
import com.rbtsb.lms.service.mapper.AppUserMapper;
import com.rbtsb.lms.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl {

    private final AppUserRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

//        public AuthenticationResponsePojo register(RegisterRequestPojo request) {
//            AppUserPojo user = User.builder()
//                    .firstname(request.getFirstname())
//                    .lastname(request.getLastname())
//                    .email(request.getEmail())
//                    .password(passwordEncoder.encode(request.getPassword()))
//                    .role(Role.USER)
//                    .build();
//            repository.save(user);
//            var jwtToken = jwtUtils.generateToken(user);
//            return AuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .build();
//        }

    public AuthenticationResponsePojo authenticate(AuthenticationRequestPojo request) throws Exception {
        log.info("Start to authenticating...");
        try {
            log.info("Username : " + request.getUsername());
            log.info("Credentials: " + request.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            log.info("Checking from db...");

            AppUserEntity appUser = repository.findByEmail(request.getUsername());

            log.info(appUser.toString());
            if (appUser != null) {
                log.info("Username: " + appUser.getUsername());
                UserDetails user = AppUserMapper.entityToUserDetails(appUser);
                String jwtToken = jwtUtils.generateToken(user);
                return AuthenticationResponsePojo.builder()
                        .token(jwtToken)
                        .build();
            } else {
                throw new NullPointerException("the username is not exist");
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (InternalAuthenticationServiceException e) {
            throw new Exception(e.toString(), e);
        }

    }

}
