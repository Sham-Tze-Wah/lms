package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.repo.AttachmentRepo;
import com.rbtsb.lms.service.AttachmentService;
import com.rbtsb.lms.service.mapper.AttachmentMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepo attachmentRepo;

    @Override
    public String insertAttachment(AttachmentDTO attachmentDTO) {
        attachmentRepo.saveAndFlush(AttachmentMapper.DTOToEntity(attachmentDTO));
        return "insert successfully";


    }

    @Override
    public List<AttachmentDTO> getAllAttachment() {
        List<AttachmentEntity> attachmentEntities = attachmentRepo.findAll();
        List<AttachmentDTO> dtoList = new ArrayList<>();
        attachmentEntities.forEach(attachmentEntity -> {
            dtoList.add(AttachmentMapper.entityToDTO(attachmentEntity));
        });

        if(!dtoList.isEmpty()){
            return dtoList;
        }
        else{
            return null;
        }
    }

    @Override
    public String updateAttachmentById(String id, AttachmentDTO attachmentDTO) {
        Optional<AttachmentEntity> attachment = attachmentRepo.findById(id);
        if(attachment.isPresent()){
            if(!attachmentDTO.getDirectory().equalsIgnoreCase("")){
                if(!attachmentDTO.getFileName().equalsIgnoreCase("")){
                    if(!attachmentDTO.getFileData().equals(null)){
                        if(!attachmentDTO.getLeaveDTO().equals(null)){
                            attachment.get().setFileName(attachmentDTO.getFileName());
                            attachment.get().setFileData(attachmentDTO.getFileData());
                            attachment.get().setDirectory(attachmentDTO.getDirectory());
                            attachment.get().setLeaveEntity(LeaveMapper.DTOToEntity(attachmentDTO.getLeaveDTO()));
                            attachmentRepo.saveAndFlush(attachment.get());
                            return "insert successfully";
                        }
                        else{
                            return "attachment must belongs to a leave application.";
                        }
                    }
                    else{
                        return "attachment cannot be empty";
                    }
                }
                else{
                    return "file name cannot be null";
                }
            }
            else{
                return "attachment directory cannot be null.";
            }
        }
        else{
            return "Update unsuccessfully due to id not exist";
        }
    }

    @Override
    public String deleteAttachmentById(String id) {
        Optional<AttachmentEntity> attachment = attachmentRepo.findById(id);
        if(attachment.isPresent()){
            attachmentRepo.saveAndFlush(attachment.get());
            return "delete successfully";
        }
        else{
            return "delete failed due to id not exist";
        }
    }

    @Override
    public String uploadFile(MultipartFile file, AttachmentDTO attachmentDTO) throws IOException {
        AttachmentDTO attachment = new AttachmentDTO();

        if(!attachmentDTO.equals(null)){
            attachment.setFileId(attachmentDTO.getFileId());
            attachment.setFileName(file.getOriginalFilename());
            attachment.setDirectory(attachmentDTO.getDirectory());
            attachment.setFileType(file.getContentType());
            attachment.setLeaveDTO(attachmentDTO.getLeaveDTO());

            attachment.setFileData(FileUtil.compressImage(file.getBytes()));
            attachmentRepo.saveAndFlush(AttachmentMapper.DTOToEntity(attachment));
            return "Upload file successfully.";
        }
        else{
            return "Upload file unsuccessfully due to empty requested body.";
        }

    }

    @Override
    public byte[] downloadFile(String fileName) {
        Optional<AttachmentEntity> dbAttachmentEntity = attachmentRepo.findByName(fileName);

        if(dbAttachmentEntity.isPresent()){
            AttachmentDTO dto = AttachmentMapper.entityToDTO(dbAttachmentEntity.get());
            return FileUtil.decompressImage(dto.getFileData());
        }
        else{
            return null;
        }

    }
}
