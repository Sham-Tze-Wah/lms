package com.rbtsb.lms.api;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.service.EducationService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/education")
@RestController
public class EducationController {
    @Autowired
    private EducationService educationService;

    private Logger log = LoggerFactory.getLogger(EducationController.class);

    @PostMapping("/post")
    public ResponseEntity<?> insertEducation(@RequestBody @Valid @NonNull EducationPojo educationPojo){
        return new ResponseEntity<>(educationService.insertEducationByEmpId(educationPojo), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllEducation(){
        return new ResponseEntity<>(educationService.getAllEducation(), HttpStatus.OK);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateEducationById(@PathVariable("id") String empId,
                                                 @RequestBody @Valid @NonNull EducationPojo educationPojo){
        return new ResponseEntity<>(educationService.updateEducationByEmpId(empId, educationPojo), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEducationById(@PathVariable("id") String id){
        return new ResponseEntity<>(educationService.deleteEducationById(id), HttpStatus.OK);
    }
}
