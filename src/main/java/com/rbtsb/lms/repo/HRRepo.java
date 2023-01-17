package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.AssignerEntity;
import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.HREntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HRRepo extends JpaRepository<HREntity,String> {

    @Query(value = "SELECT * FROM hr h, employee e WHERE h.emp_id = e.emp_id AND e.email = ?1", nativeQuery = true)
    Optional<HREntity> findByEmail(String fromEmail);

    @Query(value = "SELECT * FROM hr h WHERE h.emp_id = ?1", nativeQuery = true)
    Optional<HREntity> findByEmpId(String empId);
}
