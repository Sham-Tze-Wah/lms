//package com.rbtsb.lms.repo;
//
//import com.rbtsb.lms.entity.AppUserEntity;
//import com.rbtsb.lms.entity.RoleEntity;
//import com.rbtsb.lms.entity.UserRolesEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface UserRolesRepo extends JpaRepository<UserRolesEntity, String> {
//    @Query(value = "SELECT * FROM user_roles ur WHERE au.app_user_id = ?1", nativeQuery = true)
//    List<UserRolesEntity> findByAppUserId(String appUserId);
//
//    @Query(value = "SELECT * FROM user_roles ur, app_user au WHERE ur.app_user_id = au.app_user_id AND au.email = ?1",nativeQuery = true)
//    List<UserRolesEntity> findByEmail(String email);
//}
