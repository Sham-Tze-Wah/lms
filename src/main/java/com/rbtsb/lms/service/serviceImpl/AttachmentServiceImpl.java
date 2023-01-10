package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.GlobalConstant;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.dto.VisibleAttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.error.ErrorAction;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.AttachmentPojo;
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
import java.util.Date;
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
            AttachmentEntity attachmentEntity = attachmentMapper.DTOToEntity("",
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
            boolean isPicture = FileValidation.isPicture(fileExtension);

            File fileDirectory = new File(directory);
            if(!fileExtension.equalsIgnoreCase("")){
                if((fileDirectory.exists() && !fileDirectory.isDirectory()) || isPicture){
                    AttachmentEntity attachmentEntity = attachmentMapper.DTOToEntity("",
                            attachmentDTO, file);
                    attachmentRepo.saveAndFlush(attachmentEntity);
                }
                else{
                    return "Your file has corrupted or missing. " + ErrorAction.ERROR_ACTION;

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
    public List<VisibleAttachmentDTO> getAllAttachment() {
        List<AttachmentEntity> attachmentEntities = attachmentRepo.findAll();
        List<VisibleAttachmentDTO> dtoList = new ArrayList<>();
        attachmentEntities.forEach(attachmentEntity -> {
            try {
                dtoList.add(attachmentMapper.entityToVisibleDTO(attachmentEntity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if(!dtoList.isEmpty()){
            return dtoList;
        }
        else{
            return null;
        }
    }

    @Override
    public ApiErrorPojo updateAttachmentById(String id, AttachmentDTO attachmentDTO, MultipartFile file) {
        Optional<AttachmentEntity> attachment = attachmentRepo.findById(id);

        StringBuilder response = new StringBuilder();
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        List<String> fileNames = new ArrayList<>();
        String absolutePath = attachment.get().getDirectory();
        String destDir = "";

        try{
            if(attachmentDTO!=null && attachment.isPresent()){
                if(attachmentDTO.getDirectory() !=null && !attachmentDTO.getDirectory().equalsIgnoreCase("")){
                    attachment.get().setDirectory(attachmentDTO.getDirectory());
                    absolutePath = attachmentDTO.getDirectory();
                    destDir = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + file.getOriginalFilename();
                }

                Optional<AttachmentEntity> attachmentEntity = attachmentRepo.findByFileName(attachmentDTO.getFileName());
                if(attachmentDTO.getFileName() == null ||
                        attachmentDTO.getFileName().equalsIgnoreCase("")){
                    attachment.get().setFileName(attachment.get().getFileName());
                }
                else if(attachmentDTO.getFileName() != null &&
                        !attachmentDTO.getFileName().equalsIgnoreCase("") && !attachmentEntity.isPresent()){
                    attachment.get().setFileName(attachmentDTO.getFileName());
                }
                else{
                    apiErrorPojo.setResponseStatus("422");
                    apiErrorPojo.setResponseMessage("File name is duplicated. Please rename your file.");
                    return apiErrorPojo;
                }

                if(attachmentDTO.getFileType() != null &&
                        !attachmentDTO.getFileType().equals(null)){
                    attachment.get().setFileType(attachmentDTO.getFileType());
                }

                Date startDateLeave;
                if(attachmentDTO.getStartDateLeave() != null &&
                        !attachmentDTO.getStartDateLeave().equals(null)){
                    startDateLeave = DateTimeUtil.yyyyMMddDate(attachmentDTO.getStartDateLeave());
                }
                else{
                    startDateLeave = DateTimeUtil.yyyyMMddDate(attachment.get().getLeaveEntity().getStartDateLeave());
                }
                attachment.get().getLeaveEntity().setStartDateLeave(startDateLeave);

                Date endDateLeave;
                if(attachmentDTO.getEndDateLeave() != null &&
                        !attachmentDTO.getEndDateLeave().equals(null)){
                    endDateLeave = DateTimeUtil.yyyyMMddDate(attachmentDTO.getEndDateLeave());
                }
                else{
                    endDateLeave = DateTimeUtil.yyyyMMddDate(attachment.get().getLeaveEntity().getEndDateLeave());
                }
                attachment.get().getLeaveEntity().setEndDateLeave(endDateLeave);


                StringBuilder empName = new StringBuilder();
                Optional<EmployeeEntity> emp;
                if(attachmentDTO.getEmployeeName() == null ||
                        attachmentDTO.getEmployeeName().equalsIgnoreCase("")){
                    emp = Optional.of(attachment.get().getLeaveEntity().getEmployeeEntity());
                    empName.setLength(0);
                    empName.append(attachment.get().getLeaveEntity().getEmployeeEntity().getName());
                }
                else if(attachmentDTO.getEmployeeName() != null &&
                        !attachmentDTO.getEmployeeName().equalsIgnoreCase("")){
                    emp = employeeRepo.findByName(attachmentDTO.getEmployeeName());
                    if(emp.isPresent()){
                        empName.setLength(0);
                        empName.append(attachmentDTO.getEmployeeName());
                    }else{
                        empName.setLength(0);
                        empName.append(attachment.get().getLeaveEntity().getEmployeeEntity().getName());
                    }
                }
                else{
                    apiErrorPojo.setResponseStatus("422");
                    apiErrorPojo.setResponseMessage("Provided account is not exist.");
                    return apiErrorPojo;
                }
                attachment.get().getLeaveEntity().setEmployeeEntity(emp.get());

                StringBuilder leaveReason = new StringBuilder();
                Optional<LeaveEntity> leave = Optional.of(attachment.get().getLeaveEntity());
                if(attachmentDTO.getLeaveReason() == null ||
                        attachmentDTO.getLeaveReason().equalsIgnoreCase("")){
                    leaveReason.setLength(0);
                    leaveReason.append(attachment.get().getLeaveEntity().getReason());
                }
                else if(attachmentDTO.getLeaveReason() != null &&
                        !attachmentDTO.getLeaveReason().equalsIgnoreCase("")){
                    leave = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
                            empName.toString(),
                            startDateLeave,
                            endDateLeave);
                    if(leave.isPresent()){
                        leaveReason.setLength(0);
                        leaveReason.append(attachmentDTO.getLeaveReason());
                    }
                    else{
                        leave = Optional.of(attachment.get().getLeaveEntity());
                        leaveReason.setLength(0);
                        leaveReason.append(attachment.get().getLeaveEntity().getReason());
                    }
                }
                else{
                    apiErrorPojo.setResponseStatus("422");
                    apiErrorPojo.setResponseMessage("The applied leave is not exist.");
                    return apiErrorPojo;
                }
                attachment.get().setLeaveEntity(leave.get());

                if(file != null && !file.isEmpty()){
                    byte[] fileData = file.getBytes();
                    String extension = FileUtil.getFileExtension(file.getOriginalFilename());


                    boolean isPicture = FileValidation.isPicture(extension);
                    boolean isZip = FileValidation.isZip(extension);

                    if (isZip) {
                        //unzip files and assign the fileName to a list
                        List<String> unzipFileNames = FileUtil.unzipFiles(absolutePath, destDir);
                        fileData = FileUtil.readZipFileAndReturnBytes(absolutePath);
                    } else {
                        if (isPicture) {
                            fileData = FileUtil.compressImage(file.getBytes());
                            FileUtil.writeImage(file.getOriginalFilename(), fileData);
                        } else {
                            fileData = FileUtil.compressFile(file.getBytes(), destDir);
                        }
                    }
                    attachment.get().setFileData(fileData);
                    response.append("File: " + attachmentDTO.getFileName() + "\n");
                }


                leaveDTORepo.saveAndFlush(attachment.get().getLeaveEntity());

                attachmentRepo.saveAndFlush(attachment.get());
                response.append("Updated successfully.");
                apiErrorPojo.setResponseStatus("200");
                apiErrorPojo.setResponseMessage(response.toString());
                return apiErrorPojo;
            }
            else{
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("Attachment id is not exist.");
                return apiErrorPojo;
            }
        }
        catch(IOException ioEx){
            return new ApiErrorPojo("400",ioEx.toString());
        }
        catch(ParseException paEx){
            return new ApiErrorPojo("400", paEx.toString());
        }
        catch(Exception ex){
            return new ApiErrorPojo("400",ex.toString());
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
    public String uploadImage(MultipartFile file, AttachmentDTO attachmentDTO) throws IOException {
        AttachmentEntity attachment = new AttachmentEntity();
        StringBuilder response = new StringBuilder();
        try{
            if(!attachmentDTO.equals(null)){
                attachment.setFileName(file.getOriginalFilename());
                attachment.setDirectory(attachmentDTO.getDirectory());
                attachment.setFileType(file.getContentType());
                Optional<LeaveEntity> leave = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
                        attachmentDTO.getEmployeeName(),
                        DateTimeUtil.yyyyMMddDate(attachmentDTO.getStartDateLeave()),
                        DateTimeUtil.yyyyMMddDate(attachmentDTO.getEndDateLeave())
                        );
                if(leave.isPresent()){
                    attachment.setLeaveEntity(leaveDTORepo.findById(leave.get().getLeaveId()).get());
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
    public String uploadFile(String leaveId, MultipartFile file, AttachmentDTO attachmentDTO) {
        AttachmentEntity attachment = new AttachmentEntity();
        LeaveEntity leave = new LeaveEntity();
        EmployeeEntity employee = new EmployeeEntity();

        StringBuilder response = new StringBuilder();
        try{
            if(!file.isEmpty() && !attachmentDTO.equals(null)){
                attachment.setFileName(file.getOriginalFilename());
                attachment.setDirectory(attachmentDTO.getDirectory());
                attachment.setFileType(file.getContentType());

                String extension = FileUtil.getFileExtension(file.getOriginalFilename());
                boolean isZip = FileValidation.isZip(extension);
                boolean isPicture = FileValidation.isPicture(extension);
                if(isZip){
                    attachment.setFileData(file.getBytes());
                }
                else if(isPicture){
                    attachment.setFileData(FileUtil.compressImage(file.getBytes()));
                }
                else{
                    attachment.setFileData(file.getBytes());
                }


                if(leaveId != null && !leaveId.equalsIgnoreCase("")){
                    Optional<LeaveEntity> leaveEntity = leaveDTORepo.findById(leaveId);
                    if(leaveEntity.isPresent()){
                        attachment.setLeaveEntity(leaveEntity.get());
                        attachmentRepo.saveAndFlush(attachment);
                        response.append("Upload file successfully.");
                        return response.toString();
                    }
                }

                if(attachmentDTO.getEmployeeName() != null && !attachmentDTO.getEmployeeName().equalsIgnoreCase("")){
                    Optional<EmployeeEntity> tempEmp = employeeRepo.findByName(attachmentDTO.getEmployeeName());

                    if(tempEmp.isPresent()){
                        if(attachmentDTO.getStartDateLeave() != null){
                            if(attachmentDTO.getEndDateLeave() != null){
                                Optional<LeaveEntity> tempLeave = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
                                        attachmentDTO.getEmployeeName(),
                                        DateTimeUtil.yyyyMMddDate(attachmentDTO.getStartDateLeave()),
                                        DateTimeUtil.yyyyMMddDate(attachmentDTO.getEndDateLeave())
                                );
                                if(tempLeave.isPresent()){
                                    tempLeave.get().setEmployeeEntity(tempEmp.get());
                                    attachment.setLeaveEntity(tempLeave.get());
                                }
                                attachmentRepo.saveAndFlush(attachment);
                                leaveDTORepo.saveAndFlush(tempLeave.get());
                                response.setLength(0);
                                response.append("Upload file successfully.");
                            }
                            else{
                                response.setLength(0);
                                response.append("Upload file unsuccessfully due to empty start date leave.");
                            }
                        }
                        else{
                            response.setLength(0);
                            response.append("Upload file unsuccessfully due to empty end date leave.");
                        }
                    }
                }

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
        Optional<AttachmentEntity> dbAttachmentEntity = attachmentRepo.findByFileName(fileName);

        if(dbAttachmentEntity.isPresent()){
            String fileExtension = FileUtil.getFileExtension(dbAttachmentEntity.get().getFileName());
            boolean isPicture = FileValidation.isPicture(fileExtension);
            boolean isZip = FileValidation.isZip(fileExtension);

            if(isZip){
                return FileUtil.decompressZipFileFromDB(dbAttachmentEntity.get().getFileName(),
                        dbAttachmentEntity.get().getFileData());
            }
            else if(isPicture){
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
        Optional<AttachmentEntity> dbAttachmentEntity = attachmentRepo.findByFileName(fileName);
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
        return attachmentRepo.findByFileName(fileName);
    }

    @Override
    public long getAttachmentLength() {
        return attachmentRepo.count();
    }

    @Override
    public List<AttachmentDTO> getAttachmentByEmpIdAndDate(String empId, String startLeaveDate, String endLeaveDate) {

        try{
            List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
            List<AttachmentEntity> attachmentEntityList = attachmentRepo.findByEmpIdAndStartDateLeaveAndEndDateLeave(
                    empId,
                    DateTimeUtil.stringToDate(startLeaveDate),
                    DateTimeUtil.stringToDate(endLeaveDate));

            for(AttachmentEntity attachmentEntity : attachmentEntityList){
                AttachmentDTO attachmentDTO = attachmentMapper.entityToDTO(attachmentEntity);
                attachmentDTOList.add(attachmentDTO);
            }

            if(attachmentDTOList!=null && !attachmentDTOList.isEmpty()){
                return attachmentDTOList;
            }
            else{
                return null;
            }
        }
        catch(ParseException ex){
            return null;
        }
        catch(Exception ex){
            return null;
        }

    }
}