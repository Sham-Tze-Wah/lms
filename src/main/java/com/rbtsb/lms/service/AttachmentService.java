package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AttachmentService {
    public String insertAttachment(AttachmentDTO attachmentDTO, MultipartFile file) throws IOException;

    public List<AttachmentDTO> getAllAttachment();

    public String updateAttachmentById(String id, AttachmentDTO attachmentDTO, MultipartFile file) throws IOException;

    public String deleteAttachmentById(String id);

    public String uploadFile(MultipartFile file, AttachmentDTO attachmentDTO) throws IOException;

    public byte[] downloadFile(String fileName);

    Optional<AttachmentEntity> getAttachmentByFileName(String fileName);
}
