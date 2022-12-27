package com.rbtsb.lms.api;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.LeaveService;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
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
    public ResponseEntity<?> insertLeaveApplication(@RequestBody @Valid @NonNull LeaveDTO leaveDTO){
        try{
            if(!leaveDTO.getReason().equalsIgnoreCase("")){
                if(!leaveDTO.getLeaveStatus().equals(null)){
                    if(!leaveDTO.getEmployeeName().equalsIgnoreCase("")){
                        log.debug(leaveDTO.getEmployeeName());
                        Optional<EmployeePojo> emp  = employeeService.getEmployeeByName(leaveDTO.getEmployeeName());
                        log.debug(emp.get().toString());
                        if(emp.isPresent()){
                            return new ResponseEntity<>(
                                    leaveService.insertLeave(leaveDTO),
                                    HttpStatus.OK);
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

    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateLeaveApplicationById(@PathVariable("id") int id, @RequestBody @Valid @NonNull LeaveDTO leaveDTO){
        return new ResponseEntity<>(leaveService.updateLeaveStatus(id, leaveDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLeaveApplicationById(@PathVariable("id") int id){
        return new ResponseEntity<>(leaveService.deleteLeaveById(id), HttpStatus.OK);
    }
}