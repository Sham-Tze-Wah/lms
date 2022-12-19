package com.rbtsb.lms.api;

import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.service.EducationService;
import com.rbtsb.lms.service.WorkExperienceService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/workexp")
@RestController
public class WorkExperienceController {
    @Autowired
    private WorkExperienceService workExperienceService;

    private Logger log = LoggerFactory.getLogger(WorkExperienceController.class);

    @PostMapping("/post")
    public ResponseEntity<?> insertWorkExperience(@RequestBody @Valid @NonNull WorkExperiencePojo workExperiencePojo){
        return new ResponseEntity<>(workExperienceService.insertWorkExperience(workExperiencePojo), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllWorkExperience(){
        return new ResponseEntity<>(workExperienceService.getAllWorkExperience(),HttpStatus.OK);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateWorkExperienceById(@PathVariable("id") String id,
            @RequestBody @Valid @NonNull WorkExperiencePojo workExperiencePojo){
        return new ResponseEntity<>(workExperienceService.updateWorkExperienceByEmpId(id, workExperiencePojo)
        ,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkExperienceById(@PathVariable("id") String id){
        return new ResponseEntity<>(workExperienceService.deleteWorkExperienceById(id), HttpStatus.OK);
    }
}
