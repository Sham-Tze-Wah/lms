package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.serviceImpl.EmployeeServiceImpl;
import com.rbtsb.lms.util.DateTimeUtil;
import com.rbtsb.lms.util.validation.EmployeeValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.DateTimeAtCompleted;
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
    public ResponseEntity<?> addEmployee(@RequestParam(value="name", required = false) String name,
                                         @RequestParam(value="phoneNo", required = false) String phoneNo,
                                         @RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "address", required = false) String address,
                                         @RequestParam(value = "position", required = false) String position,
                                         @RequestParam(value = "dateJoined", required = false) String dateJoined,
                                         @RequestParam(value = "dateLeave", required = false) String dateLeave){
        EmployeePojo employeePojo = new EmployeePojo();
        try{
            if(!name.equalsIgnoreCase("")){
                if(!phoneNo.equalsIgnoreCase("") && EmployeeValidation.isInteger(phoneNo)){
                    if(!email.equalsIgnoreCase("")){
                        if(!position.equals(null)){
//                            if(!employeePojo.getRole().equals(null)){
                                //if(!employeePojo.getDateJoined().equals(null)){
                            if(dateJoined != null && !dateJoined.equalsIgnoreCase("")){
                                if(dateLeave != null && !dateJoined.equalsIgnoreCase("")){
                                    employeePojo.setDateLeave(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateLeave)));
                                }
                                employeePojo.setName(name.trim());
                                employeePojo.setPhoneNo(phoneNo.trim());
                                employeePojo.setEmail(email.trim());
                                employeePojo.setAddress(address.trim());
                                employeePojo.setPosition(Position.valueOf(position.trim()));
                                employeePojo.setDateJoined(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateJoined.trim())));
                                if(dateLeave != null && !dateLeave.equalsIgnoreCase("")){
                                    employeePojo.setDateLeave(DateTimeUtil.yyyyMMddDate(DateTimeUtil.stringToDate(dateLeave.trim())));
                                }

                                return new ResponseEntity<>(employeeService.insertEmployee(employeePojo),HttpStatus.CREATED);
                            }
                            else{
                                return new ResponseEntity<> ( "date joined cannot be null",HttpStatus.UNPROCESSABLE_ENTITY);
                            }
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
                                                @RequestParam(value="name", required = false) String name,
                                                @RequestParam(value="phoneNo", required = false) String phoneNo,
                                                @RequestParam(value = "email", required = false) String email,
                                                @RequestParam(value = "address", required = false) String address,
                                                @RequestParam(value = "position", required = false) String position,
                                                @RequestParam(value = "dateJoined", required = false) String dateJoined,
                                                @RequestParam(value = "dateLeave", required = false) String dateLeave){
        return new ResponseEntity<>(employeeService.updateEmployeeById(id, name, phoneNo, email, address, position, dateJoined, dateLeave), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id){
        return new ResponseEntity<>(employeeService.deleteEmployeeById(id), HttpStatus.OK);
    }


}
