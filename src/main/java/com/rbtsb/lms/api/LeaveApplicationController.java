package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.constant.LeaveType;
import com.rbtsb.lms.dto.EmailDTO;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.error.ErrorStatus;
import com.rbtsb.lms.pojo.*;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.*;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.util.DateTimeUtil;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.*;

@RequestMapping("/api/leave")
@RestController
public class LeaveApplicationController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private MailService emailService;

    @Autowired
    private BossService bossService;

    @Autowired
    private HRService hrService;

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationController.class);

    //@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ASSIGNER', 'ROLE_BOSS', 'ROLE_HR')")
    @PostMapping("/post")
    public ResponseEntity<?> insertLeaveApplication(@RequestParam(value = "leaveStatus", required = false) String leaveStatus,
                                                    @RequestParam(value = "reason", required = false) String reason,
                                                    @RequestParam(value = "description", required = false) String description,
                                                    @RequestParam(value = "leaveType", required = false) String leaveType,
                                                    @RequestParam(value = "startDateLeave", required = false) String startDateLeave,
                                                    @RequestParam(value = "endDateLeave", required = false) String endDateLeave,
                                                    @RequestParam(value = "empId", required = false) String employeeId){
        try{
            LeaveDTO leaveDTO = new LeaveDTO();
            if(reason != null && !reason.equalsIgnoreCase("")){
                // TODO: check on duplicate reason for this employee (might not needed)

                if(leaveStatus!= null && !leaveStatus.equalsIgnoreCase("")){
                    if(leaveType != null && !leaveType.equalsIgnoreCase("")){
                        if(employeeId != null && !employeeId.equalsIgnoreCase("")){
                            Optional<EmployeePojo> emp  = employeeService.getEmployeeById(employeeId);
                            log.debug(emp.get().toString());
                            if(emp.isPresent()){
                                leaveDTO.setLeaveStatus(LeaveStatus.valueOf(leaveStatus));
                                leaveDTO.setReason(reason);
                                if(description != null && !description.equalsIgnoreCase("")){
                                    leaveDTO.setDescription(description);
                                }
                                leaveDTO.setLeaveType(LeaveType.valueOf(leaveType));
                                leaveDTO.setEmployeeId(employeeId);
                                if(startDateLeave != null && !startDateLeave.equalsIgnoreCase("")){
                                    leaveDTO.setStartDateLeave(DateTimeUtil.stringToDate(startDateLeave));
                                }
                                else{
                                    return new ResponseEntity<>("start date leave is null", HttpStatus.UNPROCESSABLE_ENTITY);
                                }
                                if(endDateLeave != null && !endDateLeave.equalsIgnoreCase("")){
                                    leaveDTO.setEndDateLeave(DateTimeUtil.stringToDate(endDateLeave));
                                }
                                else{
                                    return new ResponseEntity<>("end date leave is null", HttpStatus.UNPROCESSABLE_ENTITY);
                                }
                                ApiErrorPojo apiErrorPojo = leaveService.insertLeave(leaveDTO);
                                return new ResponseEntity<>(
                                        apiErrorPojo.getResponseMessage(),
                                        ErrorStatus.codeMapResponse.get(
                                                apiErrorPojo.getResponseStatus()
                                        ));
                            }
                            else{
                                return new ResponseEntity<>("The employee name must be registered first.",HttpStatus.UNPROCESSABLE_ENTITY);
                            }
                        }
                        else{
                            return new ResponseEntity<>("employee name cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    else{
                        return new ResponseEntity<>("leave type cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
                else{
                    return new ResponseEntity<>("leave status cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            else{
                return new ResponseEntity<>("reason cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_BOSS')")
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllLeaveApplication(){
        return new ResponseEntity<>(leaveService.getAllLeave(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/get")
    public ResponseEntity<?> getLeaveApplicationByEmpId(@RequestParam(value = "id", required = false) String empId){
        if(empId != null && !empId.equalsIgnoreCase("")){
            return new ResponseEntity<>(leaveService.getLeaveApplicationByEmpId(empId), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("id cannot be null.", HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_ASSIGNER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getLeaveApplicationByHRId(@RequestParam(value = "id", required = false) String hrId){

        if(hrId != null && !hrId.equalsIgnoreCase("")){
            List<LeaveDTO> leaveDTOList = leaveService.getAssignedLeaveApplicationByHRId(hrId);
            return new ResponseEntity<>(leaveDTOList, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("id cannot be null.", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PatchMapping("/put/{id}")
    public ResponseEntity<?> updateLeaveApplicationByLeaveId(@PathVariable("id") String leaveId,
                                                             @RequestParam(value = "leaveStatus") String leaveStatus,
                                                             @RequestParam(value = "reason") String reason,
                                                             @RequestParam(value = "description") String description,
                                                             @RequestParam(value = "leaveType") String leaveType,
                                                             @RequestParam(value = "startDateLeave") String startDateLeave,
                                                             @RequestParam(value = "endDateLeave") String endDateLeave,
                                                             @RequestParam(value = "empId") String employeeId){
        ApiErrorPojo apiErrorPojo = leaveService.updateLeaveApplication(leaveId, leaveStatus, reason, description, leaveType, startDateLeave, endDateLeave, employeeId);
        return new ResponseEntity<>(apiErrorPojo.getResponseMessage(), ErrorStatus.codeMapResponse.get(
                apiErrorPojo.getResponseStatus()
        ));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ASSIGNER', 'ROLE_BOSS', 'ROLE_HR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLeaveApplicationById(@PathVariable("id") String id){
        String response = leaveService.deleteLeaveById(id);
        if(response.equalsIgnoreCase("Deleted successfully")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //for manager
    @PreAuthorize("hasAnyAuthority('ROLE_ASSIGNER')")
    @PatchMapping("/assign")
    public ResponseEntity<?> assignHR(@RequestParam(value = "leaveId", required = false) String leaveId,
                                      @RequestParam(value = "assignerId", required = false) String assignerId,
                                      @RequestParam(value = "HRId", required = false) String HRId
                                      ){
        try{
            if(leaveId != null && !leaveId.equalsIgnoreCase("")){
                if(assignerId != null && !assignerId.equalsIgnoreCase("")){
                    if(HRId != null && !HRId.equalsIgnoreCase("")){
                        //TODO send email to HR maybe
                        return new ResponseEntity<>(leaveService.assignHR(leaveId, assignerId, HRId), HttpStatus.OK);
                    }
                    else{
                        return new ResponseEntity<>("hr id cannot be null", HttpStatus.BAD_REQUEST);
                    }
                }
                else{
                    return new ResponseEntity<>("assigner id cannot be null", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("leave id cannot be null", HttpStatus.BAD_REQUEST);
            }
        }
        catch(NullPointerException ex){
            return new ResponseEntity<>("Invalid input on certain field", HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    //TODO check for the date so that it is not expired (max 5 working days) Done
    // for HR
    @PreAuthorize("hasAnyAuthority('ROLE_HR')")
    @PatchMapping("/validate")
    public ResponseEntity<?> validateLeave(@RequestParam(value = "leaveId", required = false) String leaveId,
                                           @RequestParam(value = "assignerId", required = false) String assignerId,
                                           @RequestParam(value = "HRId", required = false) String HRId,
                                           @RequestParam(value = "fromEmail", required = false) String fromEmail,
                                           @RequestParam(value = "toEmail", required = false) String toEmail,
                                           @RequestParam(value = "subject", required = false) String subject,
                                           @RequestParam(value = "body", required = false) String body){
//@RequestParam(value = "rejectedOrValidated", required = false) String rejectOrValidated
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
        String mailResponse = "";
        try{
            if(leaveId != null && !leaveId.equalsIgnoreCase("")){
                if(assignerId != null && !assignerId.equalsIgnoreCase("")){
                    if(HRId != null && !HRId.equalsIgnoreCase("")){
//                        if(rejectOrValidated != null && (rejectOrValidated.equalsIgnoreCase(LeaveStatus.Rejected.toString()) || rejectOrValidated.equalsIgnoreCase(LeaveStatus.Validated.toString()))){
//                            rejectOrValidated = rejectOrValidated.substring(0,1).toUpperCase() + rejectOrValidated.substring(1);
                            String response = leaveService.validateLeave(leaveId, assignerId, HRId, LeaveStatus.Validated.toString());
                            //TODO add email
                        EmailDTO emailDTO = new EmailDTO();
                        if(fromEmail != null && !fromEmail.equalsIgnoreCase("")){
                            Optional<HRPojo> hr = hrService.getHRByEmail(fromEmail);

                            if(hr.isPresent()){

                                if(toEmail != null && !toEmail.equalsIgnoreCase("")){
                                    Optional<EmployeePojo> emp = employeeService.getEmployeeByEmail(toEmail);

                                    if(emp.isPresent()){
                                        if(subject != null && subject.equalsIgnoreCase("")){
                                            if(body != null && body.equalsIgnoreCase("")){
                                                emailDTO.setFromEmail(fromEmail);
                                                emailDTO.setToEmail(toEmail);
                                                emailDTO.setSubject(subject);
                                                emailDTO.setBody(body);

                                                apiErrorPojo.setResponseMessage(leaveService.validateLeave(leaveId, assignerId, HRId, LeaveStatus.Validated.toString()));
                                                apiErrorPojo.setResponseStatus("200");
                                                mailResponse = emailService.sendSimpleEmail(emailDTO.getToEmail(),  //TODO complete email
                                                        emailDTO.getBody(),
                                                        emailDTO.getSubject())
                                                        .toString();
                                            }
                                            else{
                                                apiErrorPojo.setResponseStatus("404");
                                                apiErrorPojo.setResponseMessage("email body is null");
                                            }
                                        }
                                        else{
                                            apiErrorPojo.setResponseStatus("404");
                                            apiErrorPojo.setResponseMessage("email subject is null");
                                        }
                                    }
                                    else{
                                        apiErrorPojo.setResponseStatus("404");
                                        apiErrorPojo.setResponseMessage("to email is not exist");
                                    }
                                }
                                else{
                                    apiErrorPojo.setResponseStatus("404");
                                    apiErrorPojo.setResponseMessage("to email is null");
                                }
                            }
                            else{
                                apiErrorPojo.setResponseStatus("404");
                                apiErrorPojo.setResponseMessage("from email is not exist");
                            }

                        }
                        else{
                            apiErrorPojo.setResponseStatus("404");
                            apiErrorPojo.setResponseMessage("from email is null");
                        }
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        //}
//                        else{
//                            return new ResponseEntity<>("leave status cannot be null", HttpStatus.BAD_REQUEST);
//                        }
                    }
                    else{
                        return new ResponseEntity<>("hr id cannot be null", HttpStatus.BAD_REQUEST);
                    }
                }
                else{
                    return new ResponseEntity<>("assigner id cannot be null", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("leave id cannot be null", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(),HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAnyAuthority('ROLE_BOSS')")
    @PatchMapping("/approve/{id}")
    public ResponseEntity<?> approveLeaveApplication(@PathVariable("id") String id,
                                                     @RequestParam(value = "bossId") String bossId,
                                                     @RequestParam(value = "fromEmail", required = false) String fromEmail,
                                                     @RequestParam(value = "toEmail", required = false) String toEmail,
                                                     @RequestParam(value = "subject", required = false) String subject,
                                                     @RequestParam(value = "body", required = false) String body,
                                                     @RequestParam(value = "attachments", required = false) String[] attachments){


        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
        try{
            EmailDTO emailDTO = new EmailDTO();
            String mailResponse = null;

            if(id != null && !id.equalsIgnoreCase("")){

                if(bossId != null && !bossId.equalsIgnoreCase("")){

                    if(fromEmail != null && !fromEmail.equalsIgnoreCase("")){
                        Optional<BossPojo> boss = bossService.getBossByEmail(fromEmail);
                        //Optional<HRPojo> hr = hrService.getHRByEmail(fromEmail);

                        if(boss.isPresent()){

                            if(toEmail != null && !toEmail.equalsIgnoreCase("")){
                                Optional<EmployeePojo> emp = employeeService.getEmployeeByEmail(toEmail);

                                if(emp.isPresent()){
                                    if(subject != null && subject.equalsIgnoreCase("")){
                                        if(body != null && body.equalsIgnoreCase("")){
                                            emailDTO.setFromEmail(fromEmail);
                                            emailDTO.setToEmail(toEmail);
                                            emailDTO.setSubject(subject);
                                            emailDTO.setBody(body);

                                            apiErrorPojo = leaveService.approveLeaveStatus(id, bossId);
                                            if(attachments.length == 0){
                                                mailResponse = emailService.sendSimpleEmail(emailDTO.getToEmail(),  //TODO complete email
                                                        emailDTO.getBody(),
                                                        emailDTO.getSubject())
                                                        .toString();
                                            }
                                            else{
                                                emailDTO.setAttachments(attachments);
                                                emailService.sendSimpleEmailWithAttachment(emailDTO.getToEmail(), emailDTO.getSubject(), emailDTO.getBody(), emailDTO.getAttachments());
                                            }
                                        }
                                        else{
                                            apiErrorPojo.setResponseStatus("404");
                                            apiErrorPojo.setResponseMessage("email body is null");
                                        }
                                    }
                                    else{
                                        apiErrorPojo.setResponseStatus("404");
                                        apiErrorPojo.setResponseMessage("email subject is null");
                                    }
                                }
                                else{
                                    apiErrorPojo.setResponseStatus("404");
                                    apiErrorPojo.setResponseMessage("to email is not exist");
                                }
                            }
                            else{
                                apiErrorPojo.setResponseStatus("404");
                                apiErrorPojo.setResponseMessage("to email is null");
                            }
                        }
                        else{
                            apiErrorPojo.setResponseStatus("404");
                            apiErrorPojo.setResponseMessage("from email is not exist");
                        }

                    }
                    else{
                        apiErrorPojo.setResponseStatus("404");
                        apiErrorPojo.setResponseMessage("from email is null");
                    }
                }
                else{
                    apiErrorPojo.setResponseStatus("404");
                    apiErrorPojo.setResponseMessage("boss id is null");
                }
            }
            else{
                apiErrorPojo.setResponseStatus("404");
                apiErrorPojo.setResponseMessage("id is null");
            }
        }
        catch(Exception ex){
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(ex.toString());
        }
        return new ResponseEntity<>(
                apiErrorPojo.getResponseMessage(),
                ErrorStatus.codeMapResponse.get(
                        apiErrorPojo.getResponseStatus()
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_BOSS')")
    @PatchMapping(path = "/reject/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rejectLeaveApplication(@PathVariable("id") String id,
                                                    @RequestParam(value = "bossId", required = false) String bossId,
                                                    @RequestParam(value = "fromEmail", required = false) String fromEmail,
                                                    @RequestParam(value = "toEmail", required = false) String toEmail,
                                                    @RequestParam(value = "subject", required = false) String subject,
                                                    @RequestParam(value = "body", required = false) String body,
                                                    @RequestParam(value = "attachments", required = false) String[] attachments
                                                    ){

        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
        try{
            EmailDTO emailDTO = new EmailDTO();
            String mailResponse = null;

            if(id != null && !id.equalsIgnoreCase("")){

                if(bossId != null && !bossId.equalsIgnoreCase("")){

                    if(fromEmail != null && !fromEmail.equalsIgnoreCase("")){
                        Optional<BossPojo> boss = bossService.getBossByEmail(fromEmail);
                        //Optional<HRPojo> hr = hrService.getHRByEmail(fromEmail);

                        if(boss.isPresent()){

                            if(toEmail != null && !toEmail.equalsIgnoreCase("")){
                                Optional<EmployeePojo> emp = employeeService.getEmployeeByEmail(toEmail);

                                if(emp.isPresent()){
                                    if(subject != null && subject.equalsIgnoreCase("")){
                                        if(body != null && body.equalsIgnoreCase("")){
                                            emailDTO.setFromEmail(fromEmail);
                                            emailDTO.setToEmail(toEmail);
                                            emailDTO.setSubject(subject);
                                            emailDTO.setBody(body);

                                            apiErrorPojo = leaveService.rejectLeaveStatus(id, bossId);
                                            if(attachments.length == 0){
                                                mailResponse = emailService.sendSimpleEmail(emailDTO.getToEmail(),  //TODO complete email
                                                        emailDTO.getBody(),
                                                        emailDTO.getSubject())
                                                        .toString();
                                            }
                                            else{
                                                emailDTO.setAttachments(attachments);
                                                emailService.sendSimpleEmailWithAttachment(emailDTO.getToEmail(), emailDTO.getSubject(), emailDTO.getBody(), emailDTO.getAttachments());
                                            }
                                        }
                                        else{
                                            apiErrorPojo.setResponseStatus("404");
                                            apiErrorPojo.setResponseMessage("email body is null");
                                        }
                                    }
                                    else{
                                        apiErrorPojo.setResponseStatus("404");
                                        apiErrorPojo.setResponseMessage("email subject is null");
                                    }
                                }
                                else{
                                    apiErrorPojo.setResponseStatus("404");
                                    apiErrorPojo.setResponseMessage("to email is not exist");
                                }
                            }
                            else{
                                apiErrorPojo.setResponseStatus("404");
                                apiErrorPojo.setResponseMessage("to email is null");
                            }
                        }
                        else{
                            apiErrorPojo.setResponseStatus("404");
                            apiErrorPojo.setResponseMessage("from email is not exist");
                        }

                    }
                    else{
                        apiErrorPojo.setResponseStatus("404");
                        apiErrorPojo.setResponseMessage("from email is null");
                    }
                }
                else{
                    apiErrorPojo.setResponseStatus("404");
                    apiErrorPojo.setResponseMessage("boss id is null");
                }
            }
            else{
                apiErrorPojo.setResponseStatus("404");
                apiErrorPojo.setResponseMessage("id is null");
            }
        }
        catch(Exception ex){
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(ex.toString());
        }
        return new ResponseEntity<>(
                apiErrorPojo.getResponseMessage(),
                ErrorStatus.codeMapResponse.get(
                        apiErrorPojo.getResponseStatus()
                )
        );
    }
}
