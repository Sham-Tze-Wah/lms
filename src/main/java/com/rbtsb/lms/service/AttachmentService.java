package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    public String insertAttachment(AttachmentDTO attachmentDTO);

    public List<AttachmentDTO> getAllAttachment();

    public String updateAttachmentById(String id, AttachmentDTO attachmentDTO);

    public String deleteAttachmentById(String id);

    public String uploadFile(MultipartFile file, AttachmentDTO attachmentDTO) throws IOException;

    public byte[] downloadFile(String fileName);
}
