package com.rbtsb.lms.config;
import com.rbtsb.lms.dao.AppUserDao;
import com.sun.tracing.ProbeName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAthFilter jwtAuthFilter;
    private AppUserDao userDao;

    private static final String[] WHITE_LIST_URLS_FOR_EVERYONE={
            "/register",
            "/verifyRegistration*",
            "/resendVerifyToken*",
            "/savePassword",
            "/changePassword",
            "/all",
            "/hello",
            "/allUsers",
    };

    private static final String[] WHITE_LIST_URLS_FOR_USERS = {

    };

    private static final String[] WHITE_LIST_URLS_FOR_EMPLOYEE = {

    };

    private static final String[] WHITE_LIST_URLS_FOR_MANAGER = {

    };

    private static final String[] WHITE_LIST_URLS_FOR_HR = {

    };

    private static final String[] WHITE_LIST_URLS_FOR_BOSS = {
        "",

    };

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER) //2147483642
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers("")
//                        .permitAll()
//                        .anyRequest()
//                        .authenticated())
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


//.csrf().disable().headers().frameOptions().disable().and() // TODO add CSRF
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers(HttpMethod.POST,"/v1/auth/authenticate")
//                        .permitAll()
//                        .anyRequest()
//                        .authenticated())
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//                .and()
//                .httpBasic();

//        http.formLogin();
//        http.httpBasic();

        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URLS_FOR_EVERYONE)
                .permitAll();
//                .antMatchers("/api/**")
//                .authenticated()
//                .and()
//                .oauth2Login(oauth2login ->
//                        oauth2login.loginPage("/oauth2/authorization/api-client-oidc"))
//                .oauth2Client(Customizer.withDefaults());

        return (SecurityFilterChain)http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);//new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userDao.findByEmail(email);
            }
        };
    }


}
