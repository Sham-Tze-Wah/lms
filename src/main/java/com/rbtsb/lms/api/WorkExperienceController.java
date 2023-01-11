package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.pojo.EducationPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import com.rbtsb.lms.service.EducationService;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.WorkExperienceService;
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

@RequestMapping("/api/workexp")
@RestController
public class WorkExperienceController {
    @Autowired
    private WorkExperienceService workExperienceService;

    @Autowired
    private EmployeeService employeeService;

    private Logger log = LoggerFactory.getLogger(WorkExperienceController.class);

    @PostMapping("/post")
    public ResponseEntity<?> insertWorkExperience(@RequestParam(value = "workTitle") String workTitle,
                                                  @RequestParam(value = "yearsOfExperience") String yearsOfExperience,
                                                  @RequestParam(value = "companyName") String companyName,
                                                  @RequestParam(value = "dateJoined") String dateJoined,
                                                  @RequestParam(value = "dateResign") String dateResign,
                                                  @RequestParam(value = "empId") String empId
                                                  ){
        try{
            WorkExperiencePojo workExperiencePojo = new WorkExperiencePojo();
                if(workTitle != null && !workTitle.equalsIgnoreCase("")){
                    if(companyName != null && !companyName.equalsIgnoreCase("")){
                        if(yearsOfExperience != null && !yearsOfExperience.equalsIgnoreCase("")){
                            if(dateJoined != null && !dateJoined.equalsIgnoreCase("")){
                                if(empId != null && !empId.equalsIgnoreCase("")){
                                    Optional<EmployeePojo> empPojo = Optional.ofNullable(employeeService.getEmployeeById(empId))
                                            .orElse(null);
                                    if(empPojo.isPresent()){
                                        workExperiencePojo.setWorkTitle(Position.valueOf(workTitle));
                                        workExperiencePojo.setYearsOfExperience(yearsOfExperience);
                                        workExperiencePojo.setCompanyName(companyName);
                                        workExperiencePojo.setDateJoined(DateTimeUtil.stringToDate(dateJoined));
                                        workExperiencePojo.setDateResign(DateTimeUtil.stringToDate(dateResign));
                                        workExperiencePojo.setEmployeePojo(empPojo.get());
                                        return new ResponseEntity<>(workExperienceService.insertWorkExperience(workExperiencePojo), HttpStatus.OK);
                                    }
                                    else{
                                        return new ResponseEntity<>("emp id is not exist", HttpStatus.UNPROCESSABLE_ENTITY);
                                    }
                                }
                                else{
                                    return new ResponseEntity<>("empId is null", HttpStatus.UNPROCESSABLE_ENTITY);
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
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllWorkExperience(){
        return new ResponseEntity<>(workExperienceService.getAllWorkExperience(),HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getWorkExperienceById(@RequestParam("id") String empId){
        return new ResponseEntity<>(workExperienceService.getWorkExperienceByEmpId(empId), HttpStatus.OK);
    }

    @PatchMapping("/put/{id}")
    public ResponseEntity<?> updateWorkExperience(@PathVariable("id") String workId,
                                                         @RequestParam(value = "workTitle") String workTitle,
                                                         @RequestParam(value = "yearsOfExperience") String yearsOfExperience,
                                                         @RequestParam(value = "companyName") String companyName,
                                                         @RequestParam(value = "dateJoined") String dateJoined,
                                                         @RequestParam(value = "dateResign") String dateResign,
                                                         @RequestParam(value = "empId") String empId ){
        return new ResponseEntity<>(workExperienceService.updateWorkExperience(workId, workTitle, yearsOfExperience, companyName, dateJoined, dateResign, empId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkExperienceById(@PathVariable("id") String id){
        return new ResponseEntity<>(workExperienceService.deleteWorkExperienceById(id), HttpStatus.OK);
    }
}
