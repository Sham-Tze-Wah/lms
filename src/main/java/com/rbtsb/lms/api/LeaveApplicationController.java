package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.constant.LeaveType;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.error.ErrorStatus;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.LeaveService;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.util.DateTimeUtil;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/api/leave")
@RestController
public class LeaveApplicationController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationController.class);

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

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllLeaveApplication(){
        return new ResponseEntity<>(leaveService.getAllLeave(), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getLeaveApplicationByEmpId(@RequestParam("id") String empId){
        return new ResponseEntity<>(leaveService.getLeaveApplicationByEmpId(empId), HttpStatus.OK);
    }

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

    @PatchMapping("/approve/{id}")
    public ResponseEntity<?> approveLeaveApplication(@PathVariable("id") String id){
        ApiErrorPojo apiErrorPojo = leaveService.approveLeaveStatus(id);
        return new ResponseEntity<>(
                apiErrorPojo.getResponseMessage(),
                ErrorStatus.codeMapResponse.get(
                        apiErrorPojo.getResponseStatus()
                )
        );
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<?> rejectLeaveApplication(@PathVariable("id") String id){
        ApiErrorPojo apiErrorPojo = leaveService.rejectLeaveStatus(id);
        return new ResponseEntity<>(
                apiErrorPojo.getResponseMessage(),
                ErrorStatus.codeMapResponse.get(
                        apiErrorPojo.getResponseStatus()
                )
        );
    }
}
