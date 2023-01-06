package com.rbtsb.lms.api;

import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.serviceImpl.EmployeeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/emp")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    
    private Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid @NonNull EmployeePojo employeePojo){
        try{
            if(!employeePojo.getName().equalsIgnoreCase("")){
                if(!employeePojo.getPhoneNo().equalsIgnoreCase("")){
                    if(!employeePojo.getEmail().equalsIgnoreCase("")){
                        if(!employeePojo.getPosition().equals(null)){
//                            if(!employeePojo.getRole().equals(null)){
                                //if(!employeePojo.getDateJoined().equals(null)){
                                return new ResponseEntity<>(employeeService.insertEmployee(employeePojo),HttpStatus.CREATED);
                                //}
//                            else{
//                                employeePojo.setDateJoined(new Date());
//                                employeeRepo.saveAndFlush(EmployeeMapper.pojoToEntity(employeePojo));
//                                return "Insert successfully.";
//                            }
//                            }
//                            else{
//                                return new ResponseEntity<> ( "role cannot be null.",HttpStatus.UNPROCESSABLE_ENTITY);
//                            }
                        }
                        else{
                            return new ResponseEntity<> ( "position cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    else {
                        return new ResponseEntity<> ( "email cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
                else{
                    return new ResponseEntity<> ("phone no cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            else{
                return new ResponseEntity<> ("name cannot be null.",HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        catch(Exception ex){
            return new ResponseEntity<>(ex.toString(),HttpStatus.BAD_REQUEST);
        }

        //return new ResponseEntity<>(employeeService.insertEmployee(employeePojo),HttpStatus.CREATED);
    }
    
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllEmployee(){
        return new ResponseEntity<>(employeeService.getAllEmployee(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getEmployeeByName(@RequestParam(value="empName") String name){
        return new ResponseEntity<>(employeeService.getEmployeeByName(name), HttpStatus.OK);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable("id") String id,
                                                @RequestBody @Valid @NonNull EmployeePojo employeePojo){
        return new ResponseEntity<>(employeeService.updateEmployeeById(id, employeePojo), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id){
        return new ResponseEntity<>(employeeService.deleteEmployeeById(id), HttpStatus.OK);
    }


}
