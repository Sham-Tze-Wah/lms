//package com.rbtsb.lms.repo;
//
//import com.rbtsb.lms.dto.LoginDTO;
//import com.rbtsb.lms.entity.AppUserEntity;
//import com.rbtsb.lms.pojo.AppUserPojo;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public interface AppUserRepo extends JpaRepository<AppUserEntity,String> {
//    AppUserPojo findByUsername(String email);
//}
