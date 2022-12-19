package com.rbtsb.lms.service;

import com.rbtsb.lms.dto.AttachmentDTO;

import java.util.List;

public interface AttachmentService {
    public String insertAttachment(AttachmentDTO attachmentDTO);

    public List<AttachmentDTO> getAllAttachmnet();

    public String updateAttachmentById(String id, AttachmentDTO attachmentDTO);

    public String deleteAttachmentById(String id);
}
