package com.rbtsb.lms.repo;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.pojo.AppUserPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface AppUserRepo
        extends JpaRepository<AppUserEntity,String> {
    @Query(value = "SELECT * FROM app_user a, user_roles u, roles r WHERE a.app_user_id = u.id AND u.role_id = r.role_id AND " +
            "a.username = ?1 AND r.role_name = ?2",nativeQuery = true)
    AppUserEntity findByUsernameAndRoleName(String email, String roleName);

//    @Query("UPDATE AppUser")
//    int updatePasswordByUsername(String email);

    AppUserEntity findByUsername(String email);
}
