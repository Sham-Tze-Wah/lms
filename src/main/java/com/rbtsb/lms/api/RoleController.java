package com.rbtsb.lms.api;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.pojo.RolePojo;
import com.rbtsb.lms.repo.RoleRepo;
import com.rbtsb.lms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasAnyAuthority('BOSS', 'ASSIGNER')")
    @PostMapping(path="/post")
    public ResponseEntity<?> insertRole(@RequestBody RolePojo rolePojo){
        return new ResponseEntity<>(roleService.insertRole(rolePojo), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BOSS', 'ASSIGNER')")
    @GetMapping(path="/get/all")
    public ResponseEntity<?> getAllRoles(){
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BOSS', 'ASSIGNER')")
    @GetMapping(path = "/get/roleName")
    public ResponseEntity<?> getRoleByRoleName(@RequestParam(value = "roleName") String roleName){
        return new ResponseEntity<>(roleService.getRoleByName(roleName), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BOSS')")
    @PutMapping(path="/put/{id}")
    public ResponseEntity<?> updateExistedRoleNameById(@RequestParam(value = "id", required = false) String id,
                                                       @RequestParam(value = "roleName", required = false) String name){
        return new ResponseEntity<>(roleService.updateRoleNameById(id, name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BOSS', 'ASSIGNER')")
    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<?> deleteRoles(@PathVariable("id") String id){
        return new ResponseEntity<>(roleService.deleteRoleById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BOSS', 'ASSIGNER')")
    @PostMapping(path = "/assignRole")
    public ResponseEntity<?> assignRoleToAUser(@RequestParam(value = "role", required = false) String role,
                                               @RequestParam(value = "id", required = false) String empId){
        return new ResponseEntity<>(roleService.assignRole(role, empId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BOSS', 'ASSIGNER')")
    @PostMapping(path = "/unassignRole")
    public ResponseEntity<?> unassignRoleFromAUser(@RequestParam(value = "role", required = false) String role,
                                                   @RequestParam(value = "empId", required = false) String empId){

        return new ResponseEntity<>(roleService.unassignRole(role, empId), HttpStatus.OK);
    }



}
