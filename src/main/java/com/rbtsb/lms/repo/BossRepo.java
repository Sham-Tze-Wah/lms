package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.HREntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BossRepo extends JpaRepository<BossEntity,String> {

    @Query(value = "SELECT * FROM boss b, employee e WHERE b.emp_id = e.emp_id AND e.email = ?1", nativeQuery = true)
    Optional<BossEntity> findByEmail(String fromEmail);

    @Query(value = "SELECT * FROM boss WHERE emp_id = ?1", nativeQuery = true)
    Optional<BossEntity> findByEmpId(String empId);
}
