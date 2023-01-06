package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<RoleEntity, String> {
    RolePojo findByRoleName(String roleName);
}
