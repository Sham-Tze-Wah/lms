//package com.rbtsb.lms.config;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//
//public class AtlassianCrowAuthenticationProvider implements AuthenticationProvider {
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getPrincipal().toString();
//        String password = authentication.getCredentials().toString();
//
//        User user = callAtlassianCrowdRestService(username, password);
//        if(user == null){
//            throw new AuthenticationServiceException("could not login.");
//        }
//
//        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return false;
//    }
//}
