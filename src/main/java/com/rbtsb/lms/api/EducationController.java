package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.Course;
import com.rbtsb.lms.constant.Qualification;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.EducationService;
import com.rbtsb.lms.service.EmployeeService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/api/education")
@RestController
public class EducationController {
    private final EducationService educationService;

    private final EmployeeService employeeService;

    private Logger log = LoggerFactory.getLogger(EducationController.class);

    @Autowired
    public EducationController(EducationService educationService, EmployeeService employeeService){
        this.educationService = educationService;
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyAuthority('HR', 'USER')")
    @PostMapping("/post")
    public ResponseEntity<?> insertEducation(@RequestParam(value = "qualification", required = false) String qualification,
                                             @RequestParam(value = "institute", required = false) String institute,
                                             @RequestParam(value = "course", required = false) String course,
                                             @RequestParam(value = "employeeId", required = false) String employeeId){
        //try{
            EducationPojo educationPojo = new EducationPojo();
            if(qualification != null && !qualification.equalsIgnoreCase("")){
                if(institute != null && !institute.equalsIgnoreCase("")){
                    if(course != null && !course.equalsIgnoreCase("")){
                        Optional<EmployeePojo> empPojo = employeeService.getEmployeeById(employeeId);

                        if(empPojo.isPresent()){
                            educationPojo.setQualification(Qualification.valueOf(qualification));
                            educationPojo.setInstitute(institute);
                            educationPojo.setCourse(Course.valueOf(course));
                            educationPojo.setEmployeePojo(empPojo.get());
                            return new ResponseEntity<>(educationService.insertEducationByEmpId(educationPojo), HttpStatus.OK);
                        }
                        else{
                            return new ResponseEntity<>("Invalid employee pojo", HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    else{
                        return new ResponseEntity<>("Invalid course", HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
                else{
                    return new ResponseEntity<>( "Invalid institute", HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            else{
                return new ResponseEntity<>( "Invalid qualification", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        //}
        //catch(TransientPropertyValueException tex){
        //  return new ResponseEntity(tex.toString(), HttpStatus.BAD_REQUEST);
        // }
        //catch(Exception ex){
            //return new ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST);
        //}


    }

    @PreAuthorize("hasAnyAuthority('HR', 'USER')")
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllEducation(){
        return new ResponseEntity<>(educationService.getAllEducation(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('HR', 'USER')")
    @GetMapping("/get")
    public ResponseEntity<?> getEducationByEmpId(@RequestParam("id") String empId){
        return new ResponseEntity<>(educationService.getEducationByEmpId(empId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('HR', 'USER')")
    @PatchMapping("/put/{id}")
    public ResponseEntity<?> updateEducationById(@PathVariable("id") String eduId,
                                                 @RequestParam(value = "qualification", required = false) String qualification,
                                                 @RequestParam(value = "institute", required = false) String institute,
                                                 @RequestParam(value = "course", required = false) String course,
                                                 @RequestParam(value = "employeeId", required = false) String employeeId
                                                 ){
        return new ResponseEntity<>(educationService.updateEducationByEmpId(eduId, qualification, institute, course, employeeId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('HR', 'USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEducationById(@PathVariable("id") String id){
        return new ResponseEntity<>(educationService.deleteEducationById(id), HttpStatus.OK);
    }
}
