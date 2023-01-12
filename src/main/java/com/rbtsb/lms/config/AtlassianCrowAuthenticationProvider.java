//package com.rbtsb.lms.config;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//
//class CustomTokenAuthenticationProvider implements AuthenticationProvider {
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String customToken = (String) authentication.getPrincipal();
//        // Custom logic to validate the token
//        return getValidationToken(customToken);
//    }
//
//    private Authentication getValidationToken(String customToken) {
//        // call auth service to check validity of token
//        // keeping boolean flag for simplicity
//        boolean isValid = true;
//        if (isValid)
//            return new PreAuthenticatedAuthenticationToken("AuthenticatedUser", "ROLE_ADMIN");
//        else
//            throw new AccessDeniedException("Invalid authetication token");
//
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        // Lets use inbuilt token class for simplicity
//        return PreAuthenticatedAuthenticationToken.class.equals(authentication);
//    }
//
//}
