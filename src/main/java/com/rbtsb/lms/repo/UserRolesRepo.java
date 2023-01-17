package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepo extends JpaRepository<RoleEntity, String> {
}
