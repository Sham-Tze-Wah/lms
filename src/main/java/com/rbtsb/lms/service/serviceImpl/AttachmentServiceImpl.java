package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.repo.AttachmentDTORepo;
import com.rbtsb.lms.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentDTORepo attachmentDTORepo;

    @Override
    public String insertAttachment(AttachmentDTO attachmentDTO) {
        if(!attachmentDTO.getEmployeePojo().equals(null)){
            if(!attachmentDTO.getDirectory().equalsIgnoreCase("")){
                if(!attachmentDTO.getFileName().equalsIgnoreCase("")){
                    if(!attachmentDTO.getFiles().isEmpty()){
                        attachmentDTORepo.saveAndFlush(attachmentDTO);
                        return "insert successfully";
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
            return "this attachment is not belong to any employee";
        }
    }

    @Override
    public List<AttachmentDTO> getAllAttachmnet() {
        return attachmentDTORepo.findAll();
    }

    @Override
    public String updateAttachmentById(String id, AttachmentDTO attachmentDTO) {
        Optional<AttachmentDTO> attachment = attachmentDTORepo.findById(id);
        if(attachment.isPresent()){
            if(!attachmentDTO.getEmployeePojo().equals(null)){
                if(!attachmentDTO.getDirectory().equalsIgnoreCase("")){
                    if(!attachmentDTO.getFileName().equalsIgnoreCase("")){
                        if(!attachmentDTO.getFiles().isEmpty()){
                            attachment.get().setFileName(attachmentDTO.getFileName());
                            attachment.get().setFiles(attachmentDTO.getFiles());
                            attachment.get().setDirectory(attachmentDTO.getDirectory());
                            attachment.get().setEmployeePojo(attachmentDTO.getEmployeePojo());
                            attachmentDTORepo.saveAndFlush(attachment.get());
                            return "insert successfully";
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
                return "this attachment is not belong to any employee";
            }
        }
        else{
            return "Update unsuccessfully due to id not exist";
        }
    }

    @Override
    public String deleteAttachmentById(String id) {
        Optional<AttachmentDTO> attachment = attachmentDTORepo.findById(id);
        if(attachment.isPresent()){
            attachmentDTORepo.saveAndFlush(attachment.get());
            return "delete successfully";
        }
        else{
            return "delete failed due to id not exist";
        }
    }
}
