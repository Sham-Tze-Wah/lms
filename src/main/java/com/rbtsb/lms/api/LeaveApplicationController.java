package com.rbtsb.lms.api;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.LeaveService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/leave")
@RestController
public class LeaveApplicationController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/post")
    public ResponseEntity<?> insertLeaveApplication(@RequestBody @Valid @NonNull LeaveDTO leaveDTO){
        return new ResponseEntity<>(leaveService.insertLeave(leaveDTO), HttpStatus.OK);
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
