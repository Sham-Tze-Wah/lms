package com.rbtsb.lms.repo;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.pojo.AttachmentPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttachmentRepo extends JpaRepository<AttachmentEntity, String> {

    //@Query(value="SELECT * FROM Attachment a WHERE a.file_name=?1",nativeQuery = true)
    Optional<AttachmentEntity> findByFileName(String fileName); //next time return pojo or dto, else entity need to convert to pojo or dto
}
