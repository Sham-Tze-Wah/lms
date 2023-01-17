package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.AssignerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AssignerRepo extends JpaRepository<AssignerEntity,String> {

    @Query(value = "SELECT * FROM assigner WHERE emp_id = ?1", nativeQuery = true)
    Optional<AssignerEntity> findByEmpId(String empId);
}
