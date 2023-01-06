//package com.rbtsb.lms.api;
//
//import com.rbtsb.lms.dao.AppUserDao;
//import com.rbtsb.lms.dto.LoginDTO;
//import com.rbtsb.lms.util.JwtUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthenticationController {
//
//    private final AuthenticationManager authenticationManager;
//    //private final UserDetailsService userDetailsService;
//    private final JwtUtils jwtUtils;
//    private final AppUserDao userDao;
//
//    @PostMapping(path = "/authenticate")
//    public ResponseEntity<?> authenticate(
//            @RequestBody LoginDTO request
//    ){
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        final UserDetails user = userDao.findByEmail(request.getUsername());//userDetailsService.loadUserByUsername(request.getUsername());
//        if(user != null){
//            return new ResponseEntity<>(jwtUtils.generateToken(user), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("failed to login. Please contact the system administration.", HttpStatus.BAD_REQUEST);
//    }
//}
