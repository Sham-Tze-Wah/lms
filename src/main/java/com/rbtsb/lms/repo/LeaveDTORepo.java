package com.rbtsb.lms.repo;

import com.rbtsb.lms.dto.LeaveDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface LeaveDTORepo extends JpaRepository<LeaveDTO, Integer> {
}
