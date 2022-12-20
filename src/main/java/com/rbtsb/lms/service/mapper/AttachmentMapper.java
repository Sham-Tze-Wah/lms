package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;

public class AttachmentMapper {
    public static AttachmentDTO entityToDTO(AttachmentEntity attachmentEntity){
        AttachmentDTO dto = new AttachmentDTO();
        dto.setFileId(attachmentEntity.getFileId());
        dto.setFileName(attachmentEntity.getFileName());
        dto.setFileType(attachmentEntity.getFileType());
        dto.setDirectory(attachmentEntity.getDirectory());
        dto.setLeaveDTO(LeaveMapper.entityToDTO(attachmentEntity.getLeaveEntity()));

        return dto;
    }

    public static AttachmentEntity DTOToEntity(AttachmentDTO attachmentDTO){
        AttachmentEntity entity = new AttachmentEntity();
        entity.setFileId(attachmentDTO.getFileId());
        entity.setFileName(attachmentDTO.getFileName());
        entity.setFileType(attachmentDTO.getFileType());
        entity.setDirectory(attachmentDTO.getDirectory());
        entity.setLeaveEntity(LeaveMapper.DTOToEntity(attachmentDTO.getLeaveDTO()));

        return entity;
    }
}
