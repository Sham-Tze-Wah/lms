package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepo extends JpaRepository<RoleEntity, String> {

    @Query(value = "SELECT * FROM roles r WHERE r.role_name = ?1",nativeQuery = true)
    RoleEntity findByRoleName(String roleName);
}
