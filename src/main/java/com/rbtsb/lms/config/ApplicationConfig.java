package com.rbtsb.lms.config;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.error.exception.ResourceNotFoundException;
import com.rbtsb.lms.repo.AppUserRepo;
import com.rbtsb.lms.service.mapper.AppUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    private final AppUserRepo appUserRepo;

    @Bean
    public UserDetailsService userDetailsService(){
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//                if(email == null){
//                    throw new UsernameNotFoundException("username cannot be null.");
//                }
//                log.info("Start to initialize user details service");
//                AppUserEntity appUserEntity = appUserRepo.findByEmail(email);
//                log.info("App user entity might be null: "+email+"@"+appUserEntity);
//                if(appUserEntity != null){
//                    UserDetails user = AppUserMapper.entityToUserDetails(appUserEntity);
//                    log.info("Try to get user: " + user.toString());
//                    if(user == null){
//                        throw new UsernameNotFoundException("conversion of user entity failed.");
//                    }
//                    List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream().
//                            map(authority -> new SimpleGrantedAuthority(authority.toString())).
//                            collect(Collectors.toList());
//                    return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
//                }
//                else{
//                    throw new UsernameNotFoundException("Something wrong with your credential or token.");
//                }
//            }
//        };
        return username -> Optional.ofNullable(AppUserMapper.entityToUserDetails(appUserRepo.findByEmail(username)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        log.info("Started to initialize authentication provider");
        authProvider.setUserDetailsService(userDetailsService());
        log.info("Starting Password Encoder");
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080/"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
