package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.GlobalConstant;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.error.ErrorAction;
import com.rbtsb.lms.repo.AttachmentRepo;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.AttachmentService;
import com.rbtsb.lms.service.mapper.AttachmentMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.util.DateTimeUtil;
import com.rbtsb.lms.util.FileUtil;
import com.rbtsb.lms.util.validation.FileValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepo attachmentRepo;

    @Autowired
    private LeaveDTORepo leaveDTORepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AttachmentMapper attachmentMapper;

    private Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final Path rootInsert = Paths.get(GlobalConstant.ATTACHMENT_PATH + "//insertBackup//");

    @Override
    public void insertAttachment(AttachmentDTO attachmentDTO, MultipartFile file) {

        try {
            Files.copy(file.getInputStream(), this.rootInsert.resolve(file.getOriginalFilename()));

            // TODO: encrypt the file
            byte[] fileData = FileUtil.compressFile(file.getBytes(),rootInsert + file.getOriginalFilename());
            AttachmentEntity attachmentEntity = attachmentMapper.DTOToEntityCreate(
                    attachmentDTO, fileData);
            attachmentRepo.saveAndFlush(attachmentEntity);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public String insertAttachments(AttachmentDTO attachmentDTO, String directory, byte[] file) {

        try{
            String fileName = attachmentDTO.getFileName();
            String fileExtension = FileUtil.getFileExtension(fileName);
            File fileDirectory = new File(directory);
            if(!fileExtension.equalsIgnoreCase("")){
                if(fileDirectory.exists() && !fileDirectory.isDirectory()){
                    AttachmentEntity attachmentEntity = attachmentMapper.DTOToEntityCreate(
                            attachmentDTO, file);
                    attachmentRepo.saveAndFlush(attachmentEntity);
                }
                else{
                    return "Your file has corrupted or missing. " + ErrorAction.ERROR_ACTION;
//                    boolean isPicture = FileValidation.isPicture(fileExtension);
//                    if(isPicture){
//                        byte[] files = FileUtil.compressImage(file);
//                        AttachmentEntity attachmentEntity = attachmentMapper.DTOToEntityCreate(
//                                attachmentDTO,files);
//                        attachmentRepo.saveAndFlush(attachmentEntity);
//                    }
//                    else{
//                        byte[] files = FileUtil.compressFile(file);
//                        AttachmentEntity attachmentEntity = attachmentMapper.DTOToEntityCreate(
//                                attachmentDTO, files);
//                        attachmentRepo.saveAndFlush(attachmentEntity);
//                    }
                }
            }
            else{
                return "file extension should not be null";
            }
        }
        catch(ParseException parseExp){
            log.error(parseExp.toString());
            return parseExp.toString();
        }
//        catch(IOException ioExp){
//            log.error(ioExp.toString());
//            return ioExp.toString();
//        }
        catch(Exception ex){
            log.error(ex.toString());
            return ex.toString();
        }
        return "insert successfully";
    }

    @Override
    public List<AttachmentDTO> getAllAttachment() {
        List<AttachmentEntity> attachmentEntities = attachmentRepo.findAll();
        List<AttachmentDTO> dtoList = new ArrayList<>();
        attachmentEntities.forEach(attachmentEntity -> {
            dtoList.add(attachmentMapper.entityToDTO(attachmentEntity));
        });

        if(!dtoList.isEmpty()){
            return dtoList;
        }
        else{
            return null;
        }
    }

    @Override
    public String updateAttachmentById(String id, AttachmentDTO attachmentDTO, MultipartFile file) {
        Optional<AttachmentEntity> attachment = attachmentRepo.findById(id);
        StringBuilder response = new StringBuilder();
        try{
            if(attachment.isPresent()){
                if(!attachmentDTO.getDirectory().equalsIgnoreCase("")){
                    if(!attachmentDTO.getFileName().equalsIgnoreCase("")){
                        if(!attachmentDTO.getFileType().equals(null)){
                            if(!attachmentDTO.getDateLeave().equals(null)){
                                Optional<EmployeeEntity> emp = employeeRepo.getEmployeeByName(attachmentDTO.getEmployeeName());
                                if(emp.isPresent()){
                                    Optional<String> leave_id = leaveDTORepo.findByReasonAndEmployeeAndDate(
                                            attachmentDTO.getLeaveReason(),
                                            attachmentDTO.getEmployeeName(),
                                            DateTimeUtil.yyyyMMddDate(attachmentDTO.getDateLeave())
                                    );
                                    if(leave_id.isPresent()){
                                        attachment.get().setFileId(id);
                                        attachment.get().setFileName(attachmentDTO.getFileName());
                                        attachment.get().setFileType(attachmentDTO.getFileType());
                                        attachment.get().setDirectory(attachmentDTO.getDirectory());
                                        attachment.get().setFileData(FileUtil.compressImage(file.getBytes()));
                                        Optional<LeaveEntity> leaveEntity = leaveDTORepo.findById(leave_id.get());

                                        if(leaveEntity.isPresent()){
                                            attachment.get().setLeaveEntity(leaveEntity.get());
                                            attachmentRepo.saveAndFlush(attachment.get());
                                            response.append("insert successfully");
                                            //return "insert successfully"
                                        }
                                        else{
                                            //return "internal error occurs.";
                                            response.setLength(0);
                                            response.append("internal error occurs.");
                                        }
                                    }
                                    else{
                                        //return "the leave must be create first before attachment is uploaded";
                                        response.setLength(0);
                                        response.append("the leave must be create first before attachment is uploaded");
                                    }
                                }
                                else {
                                    //return "The attachment must belongs to an employee";
                                    response.setLength(0);
                                    response.append("The attachment must belongs to an employee");
                                }
                            }
                            else{
                                //return "the attachment must consist of date leave";
                                response.setLength(0);
                                response.append("the attachment must consist of date leave");
                            }
                        }
                        else{
                            //return "file type cannot be null";
                            response.setLength(0);
                            response.append("file type cannot be null");
                        }
                    }
                    else{
                        //return "file name cannot be null";
                        response.setLength(0);
                        response.append("file name cannot be null");
                    }
                }
                else{
                    //return "attachment directory cannot be null.";
                    response.setLength(0);
                    response.append("attachment directory cannot be null.");
                }
            }
            else{
                //return "Update unsuccessfully due to id not exist";
                response.setLength(0);
                response.append("Update unsuccessfully due to id not exist");
            }
        }
        catch(IOException ioEx){
            return ioEx.toString();
        }
        catch(ParseException paEx){
            return paEx.toString();
        }
        catch(Exception ex){
            return ex.toString();
        }
        return response.toString();
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
    public String uploadImage(MultipartFile file, AttachmentDTO attachmentDTO) throws IOException {
        AttachmentEntity attachment = new AttachmentEntity();
        StringBuilder response = new StringBuilder();
        try{
            if(!attachmentDTO.equals(null)){
                attachment.setFileName(file.getOriginalFilename());
                attachment.setDirectory(attachmentDTO.getDirectory());
                attachment.setFileType(file.getContentType());
                Optional<String> leave = leaveDTORepo.findByReasonAndEmployeeAndDate(
                        attachmentDTO.getLeaveReason(),
                        attachmentDTO.getEmployeeName(),
                        DateTimeUtil.yyyyMMddDate(attachmentDTO.getDateLeave()));
                if(leave.isPresent()){
                    attachment.setLeaveEntity(leaveDTORepo.findById(leave.get()).get());
                }
                attachment.setFileData(FileUtil.compressImage(file.getBytes()));
                attachmentRepo.saveAndFlush(attachment);
                response.append("Upload image successfully.");
            }
            else{
                response.setLength(0);
                response.append("Upload image unsuccessfully due to empty requested body.");
            }
        }
        catch(IOException ioEx){
            return ioEx.toString();
        }
        catch(ParseException paEx){
            return paEx.toString();
        }
        catch(Exception ex){
            return ex.toString();
        }
        return response.toString();
    }

    @Override
    public String uploadFile(MultipartFile file, AttachmentDTO attachmentDTO) {
        AttachmentEntity attachment = new AttachmentEntity();
        StringBuilder response = new StringBuilder();
        try{
            if(!attachmentDTO.equals(null)){
                attachment.setFileName(file.getOriginalFilename());
                attachment.setDirectory(attachmentDTO.getDirectory());
                attachment.setFileType(file.getContentType());
                Optional<String> leave = leaveDTORepo.findByReasonAndEmployeeAndDate(
                        attachmentDTO.getLeaveReason(),
                        attachmentDTO.getEmployeeName(),
                        DateTimeUtil.yyyyMMddDate(attachmentDTO.getDateLeave()));
                if(leave.isPresent()){
                    attachment.setLeaveEntity(leaveDTORepo.findById(leave.get()).get());
                }
                attachment.setFileData(FileUtil.compressImage(file.getBytes()));
                attachmentRepo.saveAndFlush(attachment);
                response.append("Upload file successfully.");
            }
            else{
                response.setLength(0);
                response.append("Upload file unsuccessfully due to empty requested body.");
            }
        }
        catch(IOException ioEx){
            return ioEx.toString();
        }
        catch(ParseException paEx){
            return paEx.toString();
        }
        catch(Exception ex){
            return ex.toString();
        }
        return response.toString();
    }


    @Override
    public byte[] downloadFile(String fileName) {
        Optional<AttachmentEntity> dbAttachmentEntity = attachmentRepo.findByName(fileName);

        if(dbAttachmentEntity.isPresent()){
            String fileExtension = FileUtil.getFileExtension(dbAttachmentEntity.get().getFileName());
            boolean isPicture = FileValidation.isPicture(fileExtension);
            if(isPicture){
                return FileUtil.decompressImage(dbAttachmentEntity.get().getFileData());
            }
            else{
                //return FileUtil.decompressImage(dbAttachmentEntity.get().getFileData());
                return FileUtil.decompressFileFromDB(dbAttachmentEntity.get().getFileName(),
                        dbAttachmentEntity.get().getFileData());
            }
        }
        else{
            return null;
        }

    }

    @Deprecated
    @Override
    public byte[] downloadFileFromDB(String fileName) {
        Optional<AttachmentEntity> dbAttachmentEntity = attachmentRepo.findByName(fileName);
        if(dbAttachmentEntity.isPresent()){
            String fileExtension = FileUtil.getFileExtension(dbAttachmentEntity.get().getFileName());
            byte[] response = FileUtil.decompressFileFromDB(fileName, dbAttachmentEntity.get().getFileData());
            return response;
        }
        else{
            return null;
        }
    }

    @Override
    public Optional<AttachmentEntity> getAttachmentByFileName(String fileName) {
        return attachmentRepo.findByName(fileName);
    }

    @Override
    public long getAttachmentLength() {
        return attachmentRepo.count();
    }
}