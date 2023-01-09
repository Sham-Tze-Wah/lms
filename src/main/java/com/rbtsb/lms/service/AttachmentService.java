package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.dto.VisibleAttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.AttachmentPojo;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AttachmentService {

    @Deprecated
    public void insertAttachment(AttachmentDTO attachmentDTO, MultipartFile file);

    public String insertAttachments(AttachmentDTO attachmentDTO, String fileName, byte[] file) throws IOException;

    public List<VisibleAttachmentDTO> getAllAttachment();

    public ApiErrorPojo updateAttachmentById(String id, AttachmentDTO attachmentDTO, MultipartFile file) throws IOException;

    public String deleteAttachmentById(String id);

    @Deprecated
    public String uploadImage(MultipartFile file, AttachmentDTO attachmentDTO) throws IOException;

    public String uploadFile(String leaveId, MultipartFile file, AttachmentDTO attachmentDTO) throws IOException;

    public byte[] downloadFile(String fileName);

    @Deprecated
    public byte[] downloadFileFromDB(String fileName);

    Optional<AttachmentEntity> getAttachmentByFileName(String fileName);

    long getAttachmentLength();

    List<AttachmentDTO> getAttachmentByEmpIdAndDate(String empId, String startLeaveDate, String endDateLeave);
}
