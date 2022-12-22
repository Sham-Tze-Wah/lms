package com.rbtsb.lms.api;

import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.service.EducationService;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.WorkExperienceService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/api/workexp")
@RestController
public class WorkExperienceController {
    @Autowired
    private WorkExperienceService workExperienceService;

    @Autowired
    private EmployeeService employeeService;

    private Logger log = LoggerFactory.getLogger(WorkExperienceController.class);

    @PostMapping("/post")
    public ResponseEntity<?> insertWorkExperience(@RequestBody @Valid @NonNull WorkExperiencePojo workExperiencePojo){
        try{
            if(!workExperiencePojo.equals(null)){
                if(!workExperiencePojo.getWorkTitle().equals(null)){
                    if(!workExperiencePojo.getCompanyName().equalsIgnoreCase("")){
                        if(!workExperiencePojo.getYearsOfExperience().equalsIgnoreCase("")){
                            if(!workExperiencePojo.getDateJoined().equals(null)){
                                Optional<EmployeePojo> empPojo = Optional.ofNullable(employeeService.getEmployeeByName(workExperiencePojo.getEmployeePojo().getName()))
                                        .orElse(null);
                                workExperiencePojo.getEmployeePojo().setEmpId(empPojo.get().getEmpId());
                                if(!workExperiencePojo.getEmployeePojo().equals(null)){
                                    return new ResponseEntity<>(workExperienceService.insertWorkExperience(workExperiencePojo), HttpStatus.OK);
                                }
                                else{
                                    return new ResponseEntity<>("The work experience does not belongs to any employee", HttpStatus.UNPROCESSABLE_ENTITY);
                                }
                            }
                            else{
                                return new ResponseEntity<>("date joined cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
                            }
                        }
                        else{
                            return new ResponseEntity<> ("work experience cannot be null. If work less than 1 year, just write 1.",HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    else{
                        return new ResponseEntity<> ("company name cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                }
                else{
                    return new ResponseEntity<> ("work title cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            else{
                return new ResponseEntity<> ("requested body cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(),HttpStatus.BAD_REQUEST);
        }

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
