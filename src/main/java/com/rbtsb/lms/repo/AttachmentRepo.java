package com.rbtsb.lms.repo;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.pojo.AttachmentPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepo extends JpaRepository<AttachmentEntity, String> {

    //@Query(value="SELECT * FROM Attachment a WHERE a.file_name=?1",nativeQuery = true)
    Optional<AttachmentEntity> findByFileName(String fileName); //next time return pojo or dto, else entity need to convert to pojo or dto

    @Query(value = "SELECT * FROM attachment a, leave_application l, employee e WHERE a.leave_id = l.leave_id AND l.emp_id = e.emp_id AND " +
            "e.emp_id = ?1 AND l.start_date_leave = ?2 AND l.end_date_leave = ?3",nativeQuery = true)
    List<AttachmentDTO> findByEmpIdAndStartDateLeaveAndEndDateLeave(String empId, Date startDateLeave, Date endDateLeave);
}
