package com.rbtsb.lms.repo;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.pojo.LeavePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface LeaveDTORepo extends JpaRepository<LeaveEntity, String> {
//    @Query(value = "SELECT leave_id FROM leave_application l, employee e " +
//            "WHERE l.emp_id = e.emp_id AND l.reason = ?1 AND e.name = ?2 AND l.date_leave = ?3",nativeQuery = true)
//    Optional<String> findByReasonAndEmployeeAndDate(String reason, String employeeName, Date startDateLeave);

    //next time return pojo or dto, else entity need to convert to pojo or dto
    @Query(value = "SELECT * FROM leave_application l, employee e " +
            "WHERE l.emp_id = e.emp_id AND " +
            "e.name = ?1 AND l.start_date_leave = ?2 AND end_date_leave = ?3",nativeQuery = true)
    Optional<LeaveEntity> findByEmployeeNameAndStartDateLeaveAndEndDateLeave(String employeeName, Date startDateLeave, Date endDateLeave);

    @Query(value = "SELECT * FROM leave_application l WHERE l.emp_id = ?1", nativeQuery = true)
    List<LeaveEntity> findByEmpId(String empId);

    @Query(value = "SELECT * FROM leave_application l WHERE l.assigner_id = ?1", nativeQuery = true)
    List<LeaveEntity> findByAssignerId(String assignerId);

    @Query(value = "SELECT * FROM leave_application l WHERE l.hr_id = ?1", nativeQuery = true)
    List<LeaveEntity> findByHRId(String hrId);

    @Query(value = "SELECT * FROM leave_application ORDER BY start_date_leave", nativeQuery = true)
    List<LeaveEntity> findByPriority();

}
