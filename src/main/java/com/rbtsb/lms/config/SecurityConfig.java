package com.rbtsb.lms.config;
import com.rbtsb.lms.dao.AppUserDao;
import com.sun.tracing.ProbeName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {

    private final JwtAthFilter jwtAuthFilter;
    private AppUserDao userDao;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private ApplicationConfig applicationConfig;

    private final AuthenticationProvider authenticationProvider;



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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(applicationConfig.userDetailsService()).passwordEncoder(applicationConfig.passwordEncoder());
    }

    @Bean
    //@Order(SecurityProperties.BASIC_AUTH_ORDER) //2147483642
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
//5,7,8
        // We don't need CSRF for this example
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                // dont authenticate this particular request
                .authorizeRequests().antMatchers(APIPath.WHITE_LIST_URLS_FOR_ANNOYMOUS).permitAll()
                // all other requests need to be authenticated
                        .anyRequest().permitAll().and()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add a filter to validate the tokens with every request

        //http.cors();


        return (SecurityFilterChain)http.build();
    }

}
