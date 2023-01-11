package com.rbtsb.lms.config;
import com.rbtsb.lms.dao.AppUserDao;
import com.sun.tracing.ProbeName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {

    private final JwtAthFilter jwtAuthFilter;
    private AppUserDao userDao;

    private static final String attch_prefix = "/api/attachment";
    private static final String edu_prefix = "/api/education";
    private static final String emp_prefix = "/api/emp";
    private static final String leave_prefix = "/api/leave";
    private static final String reg_prefix = "/api";
    private static final String work_prefix = "/api/workexp";
    private static final String assign_prefix = "/api/assignWork";
    private static final String role_prefix = "/api/role";

    private static final String[] WHITE_LIST_URLS_FOR_EVERYONE={
            "/api/authenticate",
            reg_prefix + "/register",
            reg_prefix + "/verifyRegistration*",
            reg_prefix + "/resendVerifyToken*",
            reg_prefix + "/resetPassword",
            reg_prefix + "/savePassword",
            reg_prefix + "/changePassword",
            role_prefix + "/post",
            role_prefix + "/get/all",
            role_prefix + "/put",
            role_prefix + "/delete/{id}",
            emp_prefix + "/add",
    };

    private static final String[] WHITE_LIST_URLS_FOR_EMPLOYEE = {
            emp_prefix + "/get",
            emp_prefix + "/get/{id}",
            edu_prefix + "/post",
            edu_prefix + "/get",
            edu_prefix + "/get/{id}",
            work_prefix + "/post",
            work_prefix + "/get",
            work_prefix + "/get/{id}",
            leave_prefix + "/post",
            leave_prefix + "/get",
            attch_prefix + "/post",
            attch_prefix + "/get",
            attch_prefix + "/post/download"
    };

    private static final String[] WHITE_LIST_URLS_FOR_HR = { //WHITE_LIST_URLS_FOR_EMPLOYEE

            emp_prefix + "/put/{id}",
            emp_prefix + "/delete/{id}",
            edu_prefix + "/put/{id}",
            edu_prefix + "/delete/{id}",
            work_prefix + "/put/{id}",
            work_prefix + "/delete/{id}",
            leave_prefix + "/put/{id}",
            leave_prefix + "/delete/{id}",
            assign_prefix + "/get/all", // TODO complete it
            attch_prefix + "/put",
            attch_prefix + "/delete/{id}",
            attch_prefix + "/displayImage"
    };

    private static final String[] WHITE_LIST_URLS_FOR_MANAGER = { //WHITE_LIST_URLS_FOR_HR
            "/assign", // TODO complete it
            "/unassign" // TODO complete it
    };

    private static final String[] WHITE_LIST_URLS_FOR_BOSS = {
            emp_prefix + "/get/all",
            edu_prefix + "/get/all",
            work_prefix + "/get/all",
            leave_prefix + "/get/all",
            leave_prefix + "/approve/{id}",
            leave_prefix + "/reject/{id}",
            attch_prefix + "/get/all",
            role_prefix + "/assignRole",
            role_prefix + "/unassignRole"
//            role_prefix + "/post",
//            role_prefix + "/get/all",
//            role_prefix + "/put",
//            role_prefix + "/delete/{id}"
    };

//    private static final Set<String> FINAL_WHITE_LIST_URLS_FOR_EMPLOYEE = Arrays.stream(Stream.concat(Arrays.stream(WHITE_LIST_URLS_FOR_EVERYONE), Arrays.stream(WHITE_LIST_URLS_FOR_EMPLOYEE))
//            .toArray(size -> (String[]) Array.newInstance(WHITE_LIST_URLS_FOR_EMPLOYEE.getClass().getComponentType(), size))).collect(Collectors.toSet());
//
//    private static final Set<String> FINAL_WHITE_LIST_URLS_FOR_HR = Stream.concat(Arrays.stream(WHITE_LIST_URLS_FOR_HR),
//            FINAL_WHITE_LIST_URLS_FOR_EMPLOYEE.stream()).collect(Collectors.toSet());
//
//    //    private static final Set<String> FINAL_WHITE_LIST_URLS_FOR_HR = Stream.concat(Arrays.stream(FINAL_WHITE_LIST_URLS_FOR_EMPLOYEE), Arrays.stream(WHITE_LIST_URLS_FOR_HR))
////            .toArray(size -> (String[]) Array.newInstance(WHITE_LIST_URLS_FOR_HR.getClass().getComponentType(), size));
//    private static final Set<String> FINAL_WHITE_LIST_URLS_FOR_MANAGER = Stream.concat(FINAL_WHITE_LIST_URLS_FOR_HR.stream(),
//            Arrays.stream(WHITE_LIST_URLS_FOR_MANAGER)).collect(Collectors.toSet());
//
//    private static final Set<String> FINAL_WHITE_LIST_URLS_FOR_BOSS = Stream.concat(FINAL_WHITE_LIST_URLS_FOR_MANAGER.stream(),
//            Arrays.stream(WHITE_LIST_URLS_FOR_BOSS)).collect(Collectors.toSet());

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER) //2147483642
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        http
//                .cors()
//                .and()
//                .csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .antMatchers(WHITE_LIST_URLS_FOR_EVERYONE).permitAll()
////                .antMatchers(
////                        String.valueOf(WHITE_LIST_URLS_FOR_EMPLOYEE))
////                .hasAnyRole("USER", "ADMIN", "ASSIGNER", "SUPER_ADMIN")
////
////                .antMatchers(
////                        String.valueOf(WHITE_LIST_URLS_FOR_HR))
////                .hasAnyRole("ADMIN", "ASSIGNER", "SUPER_ADMIN")
////
////                .antMatchers(
////                        String.valueOf(WHITE_LIST_URLS_FOR_MANAGER))
////                .hasAnyRole("ASSIGNER", "SUPER_ADMIN")
////
////                .antMatchers(
////                        String.valueOf(WHITE_LIST_URLS_FOR_BOSS))
////                .hasRole("SUPER_ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
////                .antMatchers("/api/**")
////                .authenticated()
////                .and()
////                .oauth2Login(oauth2login ->
////                        oauth2login.loginPage("/oauth2/authorization/api-client-oidc"))
////                .oauth2Client(Customizer.withDefaults());
//
//        //        http
////                .csrf().disable()
////                .authorizeHttpRequests(req -> req
////                        .requestMatchers("")
////                        .permitAll()
////                        .anyRequest()
////                        .authenticated())
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .authenticationProvider(authenticationProvider())
////                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//
////.csrf().disable().headers().frameOptions().disable().and()
////                .authorizeHttpRequests(req -> req
////                        .requestMatchers(HttpMethod.POST,"/v1/auth/authenticate")
////                        .permitAll()
////                        .anyRequest()
////                        .authenticated())
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .authenticationProvider(authenticationProvider())
////                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
////
////                .and()
////                .httpBasic();
//
////        http.formLogin();
////        http.httpBasic();

        // We don't need CSRF for this example
        http.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers("/api/authenticate").permitAll().
                // all other requests need to be authenticated
                        anyRequest().permitAll().and()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return (SecurityFilterChain)http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        log.info(authenticationProvider.toString());
        return authenticationProvider;
//        return new AtlassianCrowAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        log.info("finding config auth manager");
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
                UserDetails user = userDao.findByEmail(email);
                log.info("Try to get user: " + user.toString());
                if(user == null){
                    throw new UsernameNotFoundException("Something wrong with your credential or token.");
                }
                List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream().
                        map(authority -> new SimpleGrantedAuthority(authority.toString())).
                        collect(Collectors.toList());
                return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
            }
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080/"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}
