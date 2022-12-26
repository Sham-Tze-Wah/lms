package com.rbtsb.lms.repo;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
@Repository
public interface LeaveDTORepo extends JpaRepository<LeaveEntity, Integer> {
    @Query(value = "SELECT leave_id FROM leave WHERE reason = ?1, name = ?2, dateLeave = ?3",nativeQuery = true)
    Optional<Integer> findByReasonAndEmployeeAndDate(String reason, String employeeName, Date dateLeave);
}
