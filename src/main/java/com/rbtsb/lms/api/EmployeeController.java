package com.rbtsb.lms.api;

import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.EmployeeService;
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
    
    @Autowired
    private EmployeeService employeeService;
    
    private Logger log = LoggerFactory.getLogger(EmployeeController.class);
    
    @PostMapping("/add")
    public ResponseEntity<?> insertEmployee(@RequestBody @Valid @NonNull EmployeePojo employeePojo){
        return new ResponseEntity<>(employeeService.insertEmployee(employeePojo), HttpStatus.OK);
    }
    
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllEmployee(){
        return new ResponseEntity<>(employeeService.getAllEmployee(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
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
