package com.rbtsb.lms.api;

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

    @PostMapping("/post")
    public ResponseEntity<?> insertEducation(@RequestBody @Valid @NonNull EducationPojo educationPojo){
        //try{

            if(!educationPojo.getQualification().toString().equalsIgnoreCase("")){
                if(!educationPojo.getInstitute().equalsIgnoreCase("")){
                    if(!educationPojo.getCourse().toString().equalsIgnoreCase("")){
                        Optional<EmployeePojo> empPojo = employeeService.getEmployeeByName(educationPojo.getEmployeePojo().getName());

                        if(educationPojo.getEmployeePojo() != null &&
                                empPojo.isPresent()){
                            educationPojo.getEmployeePojo().setEmpId(empPojo.get().getEmpId());
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
