package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.FileType;
import com.rbtsb.lms.constant.GlobalConstant;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.dto.VisibleAttachmentDTO;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.error.ErrorStatus;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.AttachmentPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.AttachmentService;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.util.DateTimeUtil;
import com.rbtsb.lms.util.FileUtil;
import com.rbtsb.lms.util.validation.FileValidation;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

//    @PostMapping("/postAll")
//    public ResponseEntity<?> insertAttachment(@RequestParam("leaveReason") String leaveReason,
//                                              @RequestParam("employeeName") String employeeName,
//                                              @RequestParam(value = "startDateLeave", required = false) String startDateLeave,
//                                              @RequestParam(value = "endDateLeave", required = false) String endDateLeave,
//                                              @RequestParam("file") MultipartFile[] files) {
//
//        AttachmentDTO attachmentDTO = new AttachmentDTO();
//        String message = "";
//        try {
//            List<String> fileNames = new ArrayList<>();
//
//            Arrays.asList(files).stream().forEach(file -> {
//                attachmentService.insertAttachment(attachmentDTO, file);
//                fileNames.add(file.getOriginalFilename());
//            });
//
//            message = "Uploaded the files successfully: " + fileNames;
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } catch (IllegalStateException e) {
//            return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
//        } catch (Exception exception) {
//            return new ResponseEntity(exception.toString(), HttpStatus.BAD_REQUEST);
//        }
//
//    }

    private String someErrorResponse() {
        String error = "";
        try {
            return error;
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @PostMapping("/post")
    public ResponseEntity<?> insertAttachments(//@RequestParam("fileName") String fileName,
                                               //@RequestParam("fileType") FileType fileType,
                                               //@RequestParam("directory") String directory,
                                               @RequestParam(value= "leaveReason") String leaveReason,
                                               @RequestParam(value= "employeeName") String employeeName,
                                               @RequestParam(value = "startDateLeave", required = false) String startDateLeave,
                                               @RequestParam(value = "endDateLeave", required = false) String endDateLeave,
                                               @RequestParam(value = "files") MultipartFile[] files)  {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        byte[] fileData;

        List<String> fileNames = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String extension = FileUtil.getFileExtension(file.getOriginalFilename());
                String sourcePath = GlobalConstant.ATTACHMENT_PATH + "\\" + file.getOriginalFilename();
                String sourceDirectory = GlobalConstant.ATTACHMENT_PATH;
                String targetDirectory = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\";
                String targetPath = GlobalConstant.ATTACHMENT_PATH + "\\insertBackup\\" + file.getOriginalFilename();

                File targetDirTemp = new File(targetDirectory);
                if (targetDirTemp.exists() && targetDirTemp.isDirectory()) {
                    log.info("The directory has been created");
                } else if (targetDirTemp.mkdir()) {
                    log.info("The directory has created successfully.");
                } else {
                    return new ResponseEntity<>("The directory cannot be created", HttpStatus.UNPROCESSABLE_ENTITY);
                }

                if (!leaveReason.equals(null)) {
                    if (!targetPath.equalsIgnoreCase("")) { //path
                        String fileName = file.getOriginalFilename();

                        if (!fileName.equalsIgnoreCase("") && (
                                !attachmentService.getAttachmentByFileName(fileName).isPresent() ||
                                        attachmentService.getAttachmentLength() == 0L
                        )) {
                            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                            String fileType = fileTypeMap.getContentType(file.getName());

                            if (!fileType.equalsIgnoreCase("")) {

                                if (attachmentDTO.getStartDateLeave() != null && startDateLeave != null) {

                                    if(attachmentDTO.getEndDateLeave() != null && endDateLeave != null){
                                        Optional<EmployeePojo> empName = employeeService.getEmployeeByName(employeeName);

                                        if (!employeeName.equalsIgnoreCase("") &&
                                                empName.isPresent()) {
                                            attachmentDTO.setFileName(fileName);
                                            attachmentDTO.setFileType(fileType);

                                            if (startDateLeave != null) {
                                                attachmentDTO.setStartDateLeave(DateTimeUtil.stringToDate(startDateLeave.toString()));
                                            }
                                            if(endDateLeave != null){
                                                attachmentDTO.setEndDateLeave(DateTimeUtil.stringToDate(endDateLeave.toString()));
                                            }

                                            attachmentDTO.setDirectory(targetPath);
                                            attachmentDTO.setLeaveReason(leaveReason);
                                            attachmentDTO.setEmployeeName(employeeName);

                                            if (!file.isEmpty()) {
                                                fileData = file.getBytes();
                                                boolean isPicture = FileValidation.isPicture(extension);
                                                boolean isZip = FileValidation.isZip(extension);

                                                if (isZip) {
                                                    //unzip files and assign the fileName to a list
                                                    List<String> unzipFileNames = FileUtil.unzipFiles(sourcePath, targetPath);

                                                    //save zip files into the database
                                                    attachmentService.insertAttachments(
                                                            attachmentDTO,
                                                            sourcePath,
                                                            FileUtil.readZipFileAndReturnBytes(sourcePath)
                                                    );
                                                    fileData = new byte[0];
                                                    fileNames.add(file.getOriginalFilename());

//                                                //compress a file
//                                                for(String unzipFileName : unzipFileNames){
//                                                    targetPath = unzipFileName;
//
//                                                    //save the file into the database
//                                                    attachmentService.insertAttachments(
//                                                            attachmentDTO,
//                                                            targetPath,
//                                                            FileUtil.readFileAndReturnBytes(targetPath)
//                                                    );
//                                                    //fileData = new byte[0];
//                                                    //fileNames.add(file.getOriginalFilename());
//                                                }


                                                } else {
                                                    if (isPicture) {
                                                        fileData = FileUtil.compressImage(file.getBytes());
                                                        //FileUtil.writeImage(file.getOriginalFilename(), fileData);
                                                    } else {
                                                        fileData = file.getBytes();
                                                        FileUtil.compressFile(file.getBytes(), targetPath);
                                                    }
                                                    attachmentService.insertAttachments(
                                                            attachmentDTO,
                                                            targetPath,
                                                            fileData
                                                    );
                                                    fileData = new byte[0];
                                                    fileNames.add(file.getOriginalFilename());
                                                }


                                            } else {
                                                return new ResponseEntity<>("the attachment is not attached to any leave.", HttpStatus.UNPROCESSABLE_ENTITY);
                                            }
                                        } else {
                                            return new ResponseEntity<>("employee cannot be null and must be the company employee.", HttpStatus.UNPROCESSABLE_ENTITY);
                                        }
                                    }
                                    else {
                                        return new ResponseEntity<>("end date leave cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                                    }
                                } else {
                                    return new ResponseEntity<>("start date leave cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                                }
                            } else {
                                return new ResponseEntity<>("file type cannot be null or duplicate", HttpStatus.UNPROCESSABLE_ENTITY);
                            }
                        } else {
                            return new ResponseEntity<>("file name cannot be null or duplicate", HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    } else {
                        return new ResponseEntity<>("attachment directory cannot be null.", HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                } else {
                    return new ResponseEntity<>("this attachment is not used in any leave application.", HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }

            return new ResponseEntity<>("Files: +" + fileNames.stream().collect(Collectors.joining(" ")) +
                    "\nInsert successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
        } catch (IOException ioEx){
            return new ResponseEntity<>(ioEx.toString(), HttpStatus.BAD_REQUEST);
        }catch (Exception exception) {
            return new ResponseEntity(exception.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllAttachment() {
        List<VisibleAttachmentDTO> empList = attachmentService.getAllAttachment();

        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        apiErrorPojo.setResponseStatus("200");
        apiErrorPojo.setResponseMessage(empList);

        return new ResponseEntity<>(
                apiErrorPojo.getResponseMessage(),
                ErrorStatus.codeMapResponse.get(apiErrorPojo.getResponseStatus())
        );
    }

    @PutMapping("/put")
    public ResponseEntity<?> updateAnAttachmentByAttachmentId(@RequestParam(value = "id", required = false) String id,
                                                    //@RequestParam("fileName") String fileName,
                                                    //@RequestParam("fileType") FileType fileType,
                                                    //@RequestParam("directory") String directory,
                                                    @RequestParam(value = "leaveReason", required = false) String leaveReason,
                                                    @RequestParam(value = "employeeName", required = false) String employeeName,
                                                    @RequestParam(value = "startDateLeave", required = false) String startDateLeave,
                                                    @RequestParam(value = "endDateLeave", required = false) String endDateLeave,
                                                    @RequestParam(value = "files", required = false) MultipartFile[] files) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        StringBuilder response = new StringBuilder();
        ApiErrorPojo responseObj = new ApiErrorPojo();

        try{
            if (files != null && Arrays.stream(files).count() > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        attachmentDTO.setFileName(file.getOriginalFilename());

                        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                        attachmentDTO.setFileType(fileTypeMap.getContentType(file.getName()));

                        attachmentDTO.setDirectory(file.getResource().getURL().getPath());
                        if (startDateLeave != null) {
                            attachmentDTO.setStartDateLeave(DateTimeUtil.stringToDate(startDateLeave));
                        }
                        if(endDateLeave != null){
                            attachmentDTO.setEndDateLeave(DateTimeUtil.stringToDate(endDateLeave));
                        }
                        if (!leaveReason.equalsIgnoreCase("")) {
                            attachmentDTO.setLeaveReason(leaveReason);
                        }
                        if (!employeeName.equalsIgnoreCase("")) {
                            attachmentDTO.setEmployeeName(employeeName);
                        }

                        responseObj = attachmentService.updateAttachmentById(id, attachmentDTO, file);

//                        if (!responseObj.getResponseStatus().equalsIgnoreCase("200")) {
//                            return new ResponseEntity<>(responseObj.getResponseMessage(),
//                                    ErrorStatus.codeMapResponse.get(responseObj.getResponseStatus()));
//                        } else {
//                            response.append(responseObj.getResponseMessage());
//                        }
                    } else {
                        responseObj = attachmentService.updateAttachmentById(null, null, null);
                    }
                }
            } else {
                if (startDateLeave !=null ) {
                    attachmentDTO.setStartDateLeave(DateTimeUtil.stringToDate(startDateLeave));
                }
                if (endDateLeave !=null ) {
                    attachmentDTO.setEndDateLeave(DateTimeUtil.stringToDate(endDateLeave));
                }
                if (leaveReason != null && !leaveReason.equalsIgnoreCase("")) {
                    attachmentDTO.setLeaveReason(leaveReason);
                }
                if (employeeName != null && !employeeName.equalsIgnoreCase("")) {
                    attachmentDTO.setEmployeeName(employeeName);
                }

                responseObj = attachmentService.updateAttachmentById(id, attachmentDTO, null);

//                if (!responseObj.getResponseStatus().equalsIgnoreCase("200")) {
//                    return new ResponseEntity<>(responseObj.getResponseMessage(),
//                            ErrorStatus.codeMapResponse.get(responseObj.getResponseStatus()));
//                } else {
//                    response.append(responseObj.getResponseMessage());
//                }
            }
            return new ResponseEntity<>(responseObj.getResponseMessage(),
                    ErrorStatus.codeMapResponse.get(responseObj.getResponseStatus()));
        }
        catch(IOException ioEx){
            return new ResponseEntity<>(ioEx.toString(), HttpStatus.BAD_REQUEST);
        }
        catch(ParseException paEx){
            return new ResponseEntity<>(paEx.toString(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnAttachmentById(@PathVariable("id") String id) {
        return new ResponseEntity<>(attachmentService.deleteAttachmentById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/post/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(//@RequestParam("fileName") String fileName,
//                                         @RequestParam("fileType") FileType fileType,
//                                         @RequestParam("directory") String directory,
                                        @RequestParam(value = "leaveId", required = false) String leaveId,
                                        @RequestParam(value = "leaveReason", required = false) String leaveReason,
                                        @RequestParam(value = "employeeName", required = false) String employeeName,
                                        @RequestParam(value = "startDateLeave", required = false) String startDateLeave,
                                        @RequestParam(value = "endDateLeave", required = false) String endDateLeave,
                                        @RequestParam(value = "files") MultipartFile file ) throws IOException {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        StringBuilder uploadImageMsg = new StringBuilder();

        try{
            if(!file.isEmpty()){
                if(leaveId != null && !leaveId.equalsIgnoreCase("")){
                    uploadImageMsg = uploadImageMsg.append(attachmentService.uploadFile(leaveId, file, null));
                }
                else{
                    attachmentDTO.setFileName(file.getOriginalFilename());

                    MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                    attachmentDTO.setFileType(fileTypeMap.getContentType(file.getName()));

                    attachmentDTO.setDirectory(file.getResource().getURI().toString());

                    if (startDateLeave != null) {
                        attachmentDTO.setStartDateLeave(DateTimeUtil.stringToDate(startDateLeave));
                    }
                    else{
                        uploadImageMsg = uploadImageMsg.append("The start date leave is null.");
                        return new ResponseEntity<>(uploadImageMsg, HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                    if(endDateLeave != null){
                        attachmentDTO.setEndDateLeave(DateTimeUtil.stringToDate(endDateLeave));
                    }
                    else{
                        uploadImageMsg = uploadImageMsg.append("The end date leave is null.");
                        return new ResponseEntity<>(uploadImageMsg, HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                    if(leaveReason != null && !leaveReason.equalsIgnoreCase("")){
                        attachmentDTO.setLeaveReason(leaveReason);
                    }
                    else{
                        uploadImageMsg = uploadImageMsg.append("The leave reason is null.");
                        return new ResponseEntity<>(uploadImageMsg, HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                    if(employeeName != null && !employeeName.equalsIgnoreCase("")){
                        attachmentDTO.setEmployeeName(employeeName);
                    }
                    else{
                        uploadImageMsg = uploadImageMsg.append("The employee name is null.");
                        return new ResponseEntity<>(uploadImageMsg, HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                    uploadImageMsg.setLength(0);
                    uploadImageMsg = uploadImageMsg.append(attachmentService.uploadFile(null, file, attachmentDTO));
                }
                return new ResponseEntity<>(uploadImageMsg, HttpStatus.OK);
            }
            else{
                uploadImageMsg.setLength(0);
                uploadImageMsg = uploadImageMsg.append("Cannot upload file without file uploaded");
                return new ResponseEntity<>(uploadImageMsg, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        catch(Exception ex){
            uploadImageMsg.setLength(0);
            uploadImageMsg = uploadImageMsg.append(ex.toString());
            return new ResponseEntity<>(uploadImageMsg, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(path = "/post/download", produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> downloadFile(
            @RequestParam("fileName") String fileName) { //with extension
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        // TODO: try catch for attachment
        try{
            String fileExtension = FileUtil.getFileExtension(fileName);
            boolean isPicture = FileValidation.isPicture(fileExtension);
            boolean isZip = FileValidation.isZip(fileExtension);
            byte[] data = new byte[0];
            if (isZip) {
                data = attachmentService.downloadFile(fileName);
//                return ResponseEntity.status(HttpStatus.OK)//.contentType(MediaType.valueOf("image/png"))
//                        .body(data);
            } else if (isPicture) {
                data = attachmentService.downloadFile(fileName);
                FileUtil.writeImage(fileName, data);
//                return ResponseEntity.status(HttpStatus.OK)//.contentType(MediaType.valueOf("image/png"))
//                        .body(data);
            } else {
                data = attachmentService.downloadFileFromDB(fileName);
//                return ResponseEntity.status(HttpStatus.OK)//.contentType(MediaType.valueOf("application/octet-stream"))//.contentType(MediaType.valueOf("text/plain"))
//                        .body(data); //FileUtil.convertByteToString(data)
            }
            apiErrorPojo.setResponseStatus("200");

//            Base64.Decoder decoder = Base64.getDecoder();
//            String encoded = data.toString();
            //decoder.decode(encoded)
            apiErrorPojo.setResponseMessage(data);
        }
        catch(Exception ex){
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(ex.toString());
        }


        return ResponseEntity.status(ErrorStatus.codeMapResponse.get(
                apiErrorPojo.getResponseStatus()
        ))//.contentType(MediaType.valueOf("image/png"))
                .body(apiErrorPojo.getResponseMessage());

    }

//    @PostMapping("/post/download/file")
//    public ResponseEntity<?> downloadFileToFolder(
//            @RequestParam("fileName") String fileName) {
//        String fileExtension = FileUtil.getFileExtension(fileName);
//
//        byte[] data = attachmentService.downloadFile(fileName);
//        return ResponseEntity.status(HttpStatus.OK)//.contentType(MediaType.valueOf("application/octet-stream"))//.contentType(MediaType.valueOf("text/plain"))
//                .body(FileUtil.convertByteToString(data)); //FileUtil.convertByteToString(data)
//    }

//    @PostMapping("/post/download/filefromdb")
//    public ResponseEntity<?> downloadFileFromDb(
//            @RequestParam("fileName") String fileName) {
//        String fileExtension = FileUtil.getFileExtension(fileName);
//
//        byte[] response = attachmentService.downloadFileFromDB(fileName);
//        return ResponseEntity.status(HttpStatus.OK)//.contentType(MediaType.valueOf("application/octet-stream"))//.contentType(MediaType.valueOf("text/plain"))
//                .body(response); //FileUtil.convertByteToString(data)
//    }

}
