package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.RoleEntity;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.RolePojo;
import com.rbtsb.lms.repo.AppUserRepo;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.repo.RoleRepo;
import com.rbtsb.lms.service.RoleService;
import com.rbtsb.lms.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public String insertRole(RolePojo rolePojo) {
        try{
//            System.out.println(rolePojo.getRoleName());
            if(rolePojo.getRoleId() != null && !rolePojo.getRoleId().equalsIgnoreCase("")){
                if(rolePojo.getRoleName() != null && !rolePojo.getRoleName().equalsIgnoreCase("")){
                    roleRepo.saveAndFlush(RoleMapper.pojoToEntity(rolePojo));
                    return "Insert successfully!";
                }
                else{
                    throw new NullPointerException("role name is null");
                }
            }
            else{
                throw new NullPointerException("role id is null");
            }
        }
        catch(Exception ex){
            return ex.toString() + ".Internal server error occurs.";
        }
    }

    @Override
    public Set<RolePojo> getAllRoles() {
        //convert to pojo entity
//        number.stream().map(x->x*x).forEach(y->System.out.println(y));
        Set<RolePojo> rolePojoSet = roleRepo.findAll()
                .stream().map(rolePojo -> RoleMapper.entityToPojo(rolePojo))
                .collect(Collectors.toSet());

        //save into db
        return rolePojoSet;
    }

    @Override
    public String updateRoleNameById(String id, String roleName) {
        if(id != null && !id.equalsIgnoreCase("")){
            Optional<RoleEntity> roleEntity = roleRepo.findById(id);
            if(roleEntity.isPresent()){
                if(roleName != null && !roleName.equalsIgnoreCase("")){
                    roleRepo.save(roleEntity.get());
                    return "Updated successfully.";
                }
                else{
                    throw new NullPointerException("The role name should not be empty");
                }
            }
            else{
                throw new NullPointerException("No such id exist.");
            }
        }
        else{
            throw new NullPointerException("id should not be null");
        }
    }

    @Override
    public String deleteRoleById(String id) {
        if(id != null && !id.equalsIgnoreCase("")){
            Optional<RoleEntity> roleEntity = roleRepo.findById(id);
            if(roleEntity.isPresent()){
                roleRepo.delete(roleEntity.get());
                return "Deleted successfully.";
            }
            else{
                throw new NullPointerException("No such id exist.");
            }
        }
        else{
            throw new NullPointerException("id should not be null");
        }
    }

    @Override
    public String assignRole(String role, String empId) {
        if(empId != null && !empId.equalsIgnoreCase("")){
            Optional<EmployeeEntity> employeeEntity = employeeRepo.findById(empId);
            if(employeeEntity.isPresent()){
                if(role != null && !role.equalsIgnoreCase("")){
                    AppUserPojo appUser = appUserRepo.findByUsernameAndRoleName(employeeEntity.get().getEmail(), role);
                    AppUserEntity appUserEntity = new AppUserEntity();
                    RolePojo rolePojo = RoleMapper.entityToPojo(roleRepo.findByRoleName(role));

                    if(appUser != null){
                        appUserEntity.setUsername(appUser.getUsername());
                        appUserEntity.setPassword(appUser.getPassword());
                        appUserEntity.setMatchingPassword(appUser.getMatchingPassword());

                        Set<RoleEntity> roleEntitySet = new HashSet<>();
                        appUser.getRoles().add(rolePojo);
                        for(RolePojo rolePojos : appUser.getRoles()){
                            roleEntitySet.add(RoleMapper.pojoToEntity(rolePojos));
                        }
                        appUserEntity.setRoles(roleEntitySet);

                        appUserEntity.setEmployeeEntity(employeeEntity.get());
                    }
                    else{
                        appUserEntity.setUsername(appUser.getUsername());
                        appUserEntity.setPassword(appUser.getPassword());
                        appUserEntity.setMatchingPassword(appUser.getMatchingPassword());

                        Set<RoleEntity> roleEntitySet = new HashSet<>();
                        roleEntitySet.add(RoleMapper.pojoToEntity(rolePojo));
                        appUserEntity.setRoles(roleEntitySet);
                        appUserEntity.setEmployeeEntity(employeeEntity.get());
                    }
                    appUserRepo.save(appUserEntity);
                    return appUserEntity + "assign role successfully.";
                }
                else{
                    throw new NullPointerException("role is null");
                }
            }
            else{
                throw new NullPointerException("id is not exist");
            }
        }
        else{
            throw new NullPointerException("id is null");
        }
    }

    @Override
    public String unassignRole(String role, String empId) {
        if(empId != null && !empId.equalsIgnoreCase("")){
            Optional<EmployeeEntity> employeeEntity = employeeRepo.findById(empId);
            if(employeeEntity.isPresent()){
                if(role != null && !role.equalsIgnoreCase("")){
                    AppUserPojo appUserFromDB = appUserRepo.findByUsernameAndRoleName(employeeEntity.get().getEmail(), role);
                    AppUserEntity appUserEntityToBeSaved = new AppUserEntity();
                    RolePojo rolePojoFromDB = RoleMapper.entityToPojo(roleRepo.findByRoleName(role));

                    if(appUserFromDB != null){
                        appUserEntityToBeSaved.setUsername(appUserFromDB.getUsername());
                        appUserEntityToBeSaved.setPassword(appUserFromDB.getPassword());
                        appUserEntityToBeSaved.setMatchingPassword(appUserFromDB.getMatchingPassword());

                        Set<RoleEntity> roleEntitySet = new HashSet<>();
                        appUserFromDB.getRoles().add(rolePojoFromDB);
                        for(RolePojo rolePojos : appUserFromDB.getRoles()){
                            if(!rolePojos.getRoleName().equalsIgnoreCase(role)){
                                roleEntitySet.add(RoleMapper.pojoToEntity(rolePojos));
                            }
                        }
                        appUserEntityToBeSaved.setRoles(roleEntitySet);
                        appUserEntityToBeSaved.setEmployeeEntity(employeeEntity.get());

                        appUserRepo.save(appUserEntityToBeSaved);
                        return appUserEntityToBeSaved + "unassign role successfully.";
                    }
                    else{
                        throw new NullPointerException("This employee is not assign with this role yet");
                    }
                }
                else{
                    throw new NullPointerException("role is null");
                }
            }
            else{
                throw new NullPointerException("id is not exist");
            }
        }
        else{
            throw new NullPointerException("id is null");
        }
    }

    @Override
    public RoleEntity getRoleByName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }
}
