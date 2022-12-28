package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.FileType;
import com.rbtsb.lms.constant.GlobalConstant;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.AttachmentService;
import com.rbtsb.lms.service.EmployeeService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RequestMapping("/api/attachment")
@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EmployeeService employeeService;

    private Logger log = LoggerFactory.getLogger(AttachmentController.class);

    //@Autowired
    //private MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();

    @PostMapping("/post")
    public ResponseEntity<?> insertAttachment(//@RequestParam("fileName") String fileName,
                                              //@RequestParam("fileType") FileType fileType,
                                              //@RequestParam("directory") String directory,
                                              @RequestParam("leaveReason") String leaveReason,
                                              @RequestParam("employeeName") String employeeName,
                                              @RequestParam(value = "dateLeave", required = false) String dateLeave,
                                              @RequestParam("image") MultipartFile file) throws IOException{
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        try{
            if(!leaveReason.equals(null)){
                if(!file.isEmpty()){
                    File tempFile = new File(GlobalConstant.ATTACHMENT_PATH +"\\"+file.getOriginalFilename());
                    file.transferTo(tempFile);
                    String directory = tempFile.getAbsolutePath();
                    if(!directory.equalsIgnoreCase("")){ //directory
                        String fileName = file.getOriginalFilename();
                        //long num =  attachmentService.getAttachmentLength();
                        //Optional<AttachmentEntity> ety = attachmentService.getAttachmentByFileName(fileName);
                        if(!fileName.equalsIgnoreCase("") && (
                                !attachmentService.getAttachmentByFileName(fileName).isPresent() ||
                                        attachmentService.getAttachmentLength()==0L
                        )){
                            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                            String fileType = fileTypeMap.getContentType(file.getName());
                            if(!fileType.equalsIgnoreCase("")){
                                if(!attachmentDTO.getDateLeave().equals(null) || !dateLeave.equals(null)){
                                    Optional<EmployeeEntity> empName = employeeService.getEmployeeByEmployeeName(employeeName);
                                    if(!employeeName.equalsIgnoreCase("") &&
                                            empName.isPresent()){

                                        attachmentDTO.setFileName(fileName);
                                        attachmentDTO.setFileType(fileType);

                                        if(dateLeave!= null){
                                            attachmentDTO.setDateLeave(new SimpleDateFormat("yyyy-MM-dd").parse(dateLeave.toString()));
                                        }

                                        attachmentDTO.setDirectory(directory);
                                        attachmentDTO.setLeaveReason(leaveReason);
                                        attachmentDTO.setEmployeeName(employeeName);
                                        return new ResponseEntity<>(
                                                attachmentService.insertAttachment(
                                                        attachmentDTO,
                                                        file
                                                ), HttpStatus.OK);
//                                        }

                                    }
                                    else{
                                        return new ResponseEntity<>("employee cannot be null and must be the company employee.", HttpStatus.UNPROCESSABLE_ENTITY);
                                    }
                                }
                                else{
                                    return new ResponseEntity<>("date leave cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                                }
                            }
                            else{
                                return new ResponseEntity<>("file type cannot be null or duplicate", HttpStatus.UNPROCESSABLE_ENTITY);
                            }
                        }
                        else{
                            return new ResponseEntity<>( "file name cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    else{
                        return new ResponseEntity<>( "attachment directory cannot be null.", HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
                else{
                    return new ResponseEntity<>("the attachment is not attached to any leave.",HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            else{
                return new ResponseEntity<>( "this attachment is not used in any leave application.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        catch (IllegalStateException | IOException e) {
            return new ResponseEntity(e.toString(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception exception){
            return new ResponseEntity(exception.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllAttachment(){
        return new ResponseEntity<>(attachmentService.getAllAttachment(), HttpStatus.OK);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateAttachmentById(@PathVariable("id") String id,
                                                  //@RequestParam("fileName") String fileName,
                                                  //@RequestParam("fileType") FileType fileType,
                                                  //@RequestParam("directory") String directory,
                                                  @RequestParam("leaveReason") String leaveReason,
                                                  @RequestParam("employeeName") String employeeName,
                                                  @RequestParam(value = "dateLeave", required = false) Date dateLeave,
                                                  @RequestParam("image") MultipartFile file) throws IOException{
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(file.getOriginalFilename());

        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        attachmentDTO.setFileType(fileTypeMap.getContentType(file.getName()));

        attachmentDTO.setDirectory(file.getResource().getURL().getPath());
        if(!dateLeave.equals(null)){
            attachmentDTO.setDateLeave(dateLeave);
        }
        attachmentDTO.setLeaveReason(leaveReason);
        attachmentDTO.setEmployeeName(employeeName);
        return new ResponseEntity<>(attachmentService.updateAttachmentById(id, attachmentDTO, file), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAttachmentById(@PathVariable("id") String id){
        return new ResponseEntity<>(attachmentService.deleteAttachmentById(id), HttpStatus.OK);
    }

    @PostMapping("/post/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file,
//                                         @RequestParam("fileName") String fileName,
//                                         @RequestParam("fileType") FileType fileType,
//                                         @RequestParam("directory") String directory,
                                         @RequestParam("leaveReason") String leaveReason,
                                         @RequestParam("employeeName") String employeeName,
                                         @RequestParam(value = "dateLeave", required = false) Date dateLeave) throws IOException {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(file.getOriginalFilename());

        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        attachmentDTO.setFileType(fileTypeMap.getContentType(file.getName()));

        attachmentDTO.setDirectory(file.getResource().getURI().toString());

        if(!dateLeave.equals(null)){
            attachmentDTO.setDateLeave(dateLeave);
        }

        attachmentDTO.setLeaveReason(leaveReason);
        attachmentDTO.setEmployeeName(employeeName);
        String uploadImage = attachmentService.uploadFile(file, attachmentDTO);
        return new ResponseEntity<>(uploadImage, HttpStatus.OK);
    }

    @PostMapping("/post/download")
    public ResponseEntity<?> downloadImage(
            @RequestParam("fileName") String fileName){
        byte[] data = attachmentService.downloadFile(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                .body(data);
    }
}
