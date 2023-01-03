package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.repo.AttachmentRepo;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentMapper {

    @Autowired
    private AttachmentRepo attachmentRepo;

    @Autowired
    private LeaveDTORepo leaveDTORepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    public AttachmentDTO entityToDTO(AttachmentEntity attachmentEntity){
        AttachmentDTO dto = new AttachmentDTO();
        dto.setFileName(attachmentEntity.getFileName());
        dto.setFileType(attachmentEntity.getFileType());
        dto.setDirectory(attachmentEntity.getDirectory());
        dto.setLeaveReason(attachmentEntity.getLeaveEntity().getReason());
        dto.setStartDateLeave(attachmentEntity.getLeaveEntity().getStartDateLeave());
        dto.setEndDateLeave(attachmentEntity.getLeaveEntity().getEndDateLeave());
        dto.setEmployeeName(attachmentEntity.getLeaveEntity().getEmployeeEntity().getName());

        return dto;
    }

    public AttachmentEntity DTOToEntity(String id, AttachmentDTO attachmentDTO, byte[] file) throws ParseException {
        AttachmentEntity attachmentEntity = new AttachmentEntity();
//        LeaveEntity leaveEntity = new LeaveEntity();
//        EmployeeEntity empEntity = new EmployeeEntity();

        if(id != null && !id.equalsIgnoreCase("")){
            attachmentEntity.setFileId(id);
        }

        attachmentEntity.setFileName(attachmentDTO.getFileName());
        attachmentEntity.setFileType(attachmentDTO.getFileType());
        attachmentEntity.setDirectory(attachmentDTO.getDirectory());
        attachmentEntity.setFileData(file);


        //convert dto to leave entity
        //get leave entity
        Optional<LeaveEntity> tempLeaveEntity = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
            attachmentDTO.getEmployeeName(),
                attachmentDTO.getStartDateLeave(),
                attachmentDTO.getEndDateLeave()
        );
        tempLeaveEntity.get().setStartDateLeave(DateTimeUtil.yyyyMMddDate(attachmentDTO.getStartDateLeave()));
        tempLeaveEntity.get().setEndDateLeave(DateTimeUtil.yyyyMMddDate(attachmentDTO.getEndDateLeave()));
        tempLeaveEntity.get().setReason(attachmentDTO.getLeaveReason());
        attachmentEntity.setLeaveEntity(tempLeaveEntity.get());

        //convert dto to employee entity
        Optional<EmployeeEntity> tempEmployeeEntity = employeeRepo.findByName(attachmentDTO.getEmployeeName());
        attachmentEntity.getLeaveEntity().setEmployeeEntity(tempEmployeeEntity.get());

        return attachmentEntity;
    }

//    public AttachmentEntity DTOToEntityCreate(AttachmentDTO attachmentDTO, byte[] file) throws ParseException {
//        AttachmentEntity entity = new AttachmentEntity();
//        entity.setFileName(attachmentDTO.getFileName());
//        entity.setFileType(attachmentDTO.getFileType());
//        entity.setDirectory(attachmentDTO.getDirectory());
//        entity.setFileData(file);
//
//        Optional<LeaveEntity> tempLeaveEntity = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
//                attachmentDTO.getEmployeeName(),
//                attachmentDTO.getStartDateLeave(),
//                attachmentDTO.getEndDateLeave()
//        );
//        entity.setLeaveEntity(leaveDTORepo.findById(tempLeaveEntity.get()).get());
//        return entity;
//    }
}
