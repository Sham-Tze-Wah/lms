package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.dto.VisibleAttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.pojo.AttachmentPojo;
import com.rbtsb.lms.repo.AttachmentRepo;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.util.DateTimeUtil;
import com.rbtsb.lms.util.FileUtil;
import com.rbtsb.lms.util.validation.FileValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.text.ParseException;
import java.util.Date;
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

    public AttachmentDTO entityToDTO(AttachmentEntity attachmentEntity) throws ParseException {
        AttachmentDTO dto = new AttachmentDTO();
        dto.setFileName(attachmentEntity.getFileName());
        dto.setFileType(attachmentEntity.getFileType());
        dto.setDirectory(attachmentEntity.getDirectory());
        dto.setLeaveReason(attachmentEntity.getLeaveEntity().getReason());
        //System.out.println(DateTimeUtil.yyyyMMddDate(attachmentEntity.getLeaveEntity().getStartDateLeave()));

        dto.setStartDateLeave(attachmentEntity.getLeaveEntity().getStartDateLeave());

        //System.out.println(DateTimeUtil.yyyyMMddDate(attachmentEntity.getLeaveEntity().getEndDateLeave()));
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
        //Date startDate = DateTimeUtil.yyyyMMddDate(attachmentDTO.getStartDateLeave());
        //Date endDate = DateTimeUtil.yyyyMMddDate(attachmentDTO.getEndDateLeave());
        Optional<LeaveEntity> tempLeaveEntity = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
            attachmentDTO.getEmployeeName(),
                attachmentDTO.getStartDateLeave(),
                attachmentDTO.getEndDateLeave()
        );
        if(tempLeaveEntity.isPresent()){
            tempLeaveEntity.get().setStartDateLeave(attachmentDTO.getStartDateLeave());
            tempLeaveEntity.get().setEndDateLeave(attachmentDTO.getEndDateLeave());
            tempLeaveEntity.get().setReason(attachmentDTO.getLeaveReason());
            attachmentEntity.setLeaveEntity(tempLeaveEntity.get());

            //convert dto to employee entity
            Optional<EmployeeEntity> tempEmployeeEntity = employeeRepo.findByName(attachmentDTO.getEmployeeName());
            if(tempEmployeeEntity.isPresent()){
                attachmentEntity.getLeaveEntity().setEmployeeEntity(tempEmployeeEntity.get());
                return attachmentEntity;
            }
            else{
                return null;
            }

        }
        else{
            return null;
        }
    }

//    public AttachmentPojo entityToPojo(AttachmentEntity attachmentEntity) {
//        AttachmentPojo pojo = new AttachmentPojo();
//        pojo.setFileName(attachmentEntity.getFileName());
//        pojo.setFileType(attachmentEntity.getFileType());
//        pojo.setDirectory(attachmentEntity.getDirectory());
//        pojo.setFileData(attachmentEntity.getFileData());
//        pojo.setFileId(attachmentEntity.getFileId());
//        //System.out.println(DateTimeUtil.yyyyMMddDate(attachmentEntity.getLeaveEntity().getStartDateLeave()));
//        return pojo;
//    }

    public VisibleAttachmentDTO entityToVisibleDTO(AttachmentEntity attachmentEntity) {
        VisibleAttachmentDTO dto = new VisibleAttachmentDTO();
        dto.setFileName(attachmentEntity.getFileName());
        dto.setFileType(attachmentEntity.getFileType());
        dto.setDirectory(attachmentEntity.getDirectory());

        String extension = FileUtil.getFileExtension(attachmentEntity.getFileName());
        boolean isZip = FileValidation.isZip(extension);
        boolean isPicture = FileValidation.isPicture(extension);
        StringBuilder fileData = new StringBuilder();
        byte[] img = new byte[0];

        if(isZip){
            byte[] data = FileUtil.decompressZipFileFromDBWithoutCreatingFile(attachmentEntity.getFileData());
            fileData = fileData.append(new String(data, StandardCharsets.UTF_8));
            dto.setFileData(fileData.toString());
        }
        else if(isPicture){
            img = FileUtil.decompressImage(attachmentEntity.getFileData());
            dto.setFileData(img);
        }
        else{

            fileData = fileData.append(new String(attachmentEntity.getFileData(), StandardCharsets.UTF_8));
            dto.setFileData(fileData.toString());
        }

        dto.setFileId(attachmentEntity.getFileId());
        //System.out.println(DateTimeUtil.yyyyMMddDate(attachmentEntity.getLeaveEntity().getStartDateLeave()));
        return dto;
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
