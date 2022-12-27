package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.repo.AttachmentRepo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class AttachmentMapper {

    @Autowired
    private AttachmentRepo attachmentRepo;

    @Autowired
    private LeaveDTORepo leaveDTORepo;

    public AttachmentDTO entityToDTO(AttachmentEntity attachmentEntity){
        AttachmentDTO dto = new AttachmentDTO();
        dto.setFileName(attachmentEntity.getFileName());
        dto.setFileType(attachmentEntity.getFileType());
        dto.setDirectory(attachmentEntity.getDirectory());
        dto.setLeaveReason(attachmentEntity.getLeaveEntity().getReason());
        dto.setDateLeave(attachmentEntity.getLeaveEntity().getDateLeave());
        dto.setEmployeeName(attachmentEntity.getLeaveEntity().getEmployeeEntity().getName());

        return dto;
    }

    public AttachmentEntity DTOToEntity(AttachmentDTO attachmentDTO, byte[] file){
        AttachmentEntity entity = new AttachmentEntity();
        entity.setFileName(attachmentDTO.getFileName());
        entity.setFileType(attachmentDTO.getFileType());
        entity.setDirectory(attachmentDTO.getDirectory());
        entity.setFileData(file);
        if(attachmentRepo.findByName(entity.getFileName()).isPresent()){
            entity.setLeaveEntity(attachmentRepo.findByName(entity.getFileName()).get().getLeaveEntity());
            return entity;
        }
        else{
            return null;
        }
    }

    public AttachmentEntity DTOToEntityCreate(AttachmentDTO attachmentDTO, byte[] file) throws ParseException {
        AttachmentEntity entity = new AttachmentEntity();
        entity.setFileName(attachmentDTO.getFileName());
        entity.setFileType(attachmentDTO.getFileType());
        entity.setDirectory(attachmentDTO.getDirectory());
        entity.setFileData(file);
        Optional<Integer> leave_id = leaveDTORepo.findByReasonAndEmployeeAndDate(
                attachmentDTO.getLeaveReason(),
                attachmentDTO.getEmployeeName(),
                DateTimeUtil.yyyyMMddDate(attachmentDTO.getDateLeave())
        );
        if(leave_id.isPresent()){
            entity.setLeaveEntity(leaveDTORepo.findById(leave_id.get()).get());
            return entity;
        }
        else{
            return null;
        }
    }
}
