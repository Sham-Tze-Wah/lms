package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.RolePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, String> {


    //already define in the role entity
    @Query(nativeQuery = true)
    RolePojo findByRoleName(String roleName);
}
