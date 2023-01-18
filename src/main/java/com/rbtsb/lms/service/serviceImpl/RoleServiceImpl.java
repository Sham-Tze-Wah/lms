package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.*;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.RolePojo;
import com.rbtsb.lms.repo.*;
import com.rbtsb.lms.service.RoleService;
import com.rbtsb.lms.service.mapper.AppUserMapper;
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

    @Autowired
    private HRRepo hrRepo;

    @Autowired
    private BossRepo bossRepo;

    @Autowired
    private AssignerRepo assignerRepo;

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

    private HREntity createHRDetails(EmployeeEntity emp){
        //Optional<EmployeeEntity> emp = employeeRepo.findById(empId);
        //if(emp.isPresent()){
            HREntity hrEntity = new HREntity();
            hrEntity.setEmployeeEntity(emp);
            hrRepo.save(hrEntity);
            return hrEntity;
//        }
//        else{
//            throw new NullPointerException("empId is not exist");
//        }
    }

    private BossEntity createBossDetails(EmployeeEntity emp){
//        Optional<EmployeeEntity> emp = employeeRepo.findById(empId);
//        if(emp.isPresent()){
            BossEntity bossEntity = new BossEntity();
            bossEntity.setEmployeeEntity(emp);
            bossRepo.save(bossEntity);
            return bossEntity;
//        }
//        else{
//            throw new NullPointerException("empId is not exist");
//        }
    }

    private AssignerEntity createAssignerDetails(EmployeeEntity emp){
        //Optional<EmployeeEntity> emp = employeeRepo.findById(empId);
//        if(emp.isPresent()){
            AssignerEntity assignerEntity = new AssignerEntity();
            assignerEntity.setEmployeeEntity(emp);
            assignerRepo.save(assignerEntity);
            return assignerEntity;
//        }
//        else{
//            throw new NullPointerException("empId is not exist");
//        }
    }

    private void createRolesEntityBasedOnRoleName(String role, String empId){
        Optional<EmployeeEntity> emp = employeeRepo.findById(empId);

        if(emp.isPresent()){

            if(role.equalsIgnoreCase("hr")){
                createHRDetails(emp.get());
            }
            else if(role.equalsIgnoreCase("boss")){
                createBossDetails(emp.get());
            }
            else if(role.equalsIgnoreCase("assigner")){
                createAssignerDetails(emp.get());
            }

        }
        else{
            throw new NullPointerException("empId is not exist");
        }
    }

    @Override
    public String assignRole(String role, String empId) {
        if(empId != null && !empId.equalsIgnoreCase("")){
            Optional<EmployeeEntity> employeeEntity = employeeRepo.findById(empId);
            if(employeeEntity.isPresent()){
                if(role != null && !role.equalsIgnoreCase("")){
                    AppUserEntity appUserEntity = appUserRepo.findByUsernameAndRoleName(employeeEntity.get().getEmail(), role);
                    RoleEntity roleEntity = RoleMapper.pojoToEntity(roleRepo.findByRoleName(role)); //RoleMapper.entityToPojo(

                    if(appUserEntity != null){ //existing app user
//                        appUserEntity.setUsername(appUser.getUsername());
//                        appUserEntity.setPassword(appUser.getPassword());
//                        appUserEntity.setMatchingPassword(appUser.getMatchingPassword());

//                        Set<RoleEntity> roleEntitySet = new HashSet<>();
                        appUserEntity.getRoles().add(roleEntity);
//                        for(RoleEntity roleEntities : appUserEntity.getRoles()){
//                            roleEntitySet.add(roleEntities);
//                        }
//                        appUserEntity.setRoles(roleEntitySet);

                    }
                    else{
                        appUserEntity = appUserRepo.findByUsername(employeeEntity.get().getEmail());
                        if(appUserEntity != null){
                            appUserEntity.setUsername(appUserEntity.getUsername());
                            appUserEntity.setPassword(appUserEntity.getPassword());
                            appUserEntity.setMatchingPassword(appUserEntity.getMatchingPassword());

                            Set<RoleEntity> roleEntitySet = new HashSet<>();
                            roleEntitySet.add(roleEntity);
                            appUserEntity.setRoles(roleEntitySet);
                            appUserEntity.setEmployeeEntity(employeeEntity.get());
                        }
                        else{
                            throw new NullPointerException("the emp id provided is invalid.");
                        }
                    }
                    createRolesEntityBasedOnRoleName(role, empId);
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

    private HREntity deleteHRDetails(String empId){
        Optional<HREntity> hr = hrRepo.findByEmpId(empId);
        if(hr.isPresent()){
            hrRepo.delete(hr.get());
            return hr.get();
        }
        else{
            throw new NullPointerException("empId is not exist");
        }
    }

    private BossEntity deleteBossDetails(String empId){
        Optional<BossEntity> boss = bossRepo.findByEmpId(empId);
        if(boss.isPresent()){
            bossRepo.delete(boss.get());
            return boss.get();
        }
        else{
            throw new NullPointerException("empId is not exist");
        }
    }

    private AssignerEntity deleteAssignerDetails(String empId){
        Optional<AssignerEntity> assigner = assignerRepo.findByEmpId(empId);
        if(assigner.isPresent()){
            assignerRepo.delete(assigner.get());
            return assigner.get();
        }
        else{
            throw new NullPointerException("empId is not exist");
        }
    }

    private void deleteRolesEntityBasedOnRoleName(String role, String empId){
        if(role.equalsIgnoreCase("hr")){
            deleteHRDetails(empId);
        }
        else if(role.equalsIgnoreCase("boss")){
            deleteBossDetails(empId);
        }
        else if(role.equalsIgnoreCase("assigner")){
            deleteAssignerDetails(empId);
        }
    }

    @Override
    public String unassignRole(String role, String empId) {
        if(empId != null && !empId.equalsIgnoreCase("")){
            Optional<EmployeeEntity> employeeEntity = employeeRepo.findById(empId);
            if(employeeEntity.isPresent()){
                if(role != null && !role.equalsIgnoreCase("")){
                    AppUserEntity appUserEntityFromDB = appUserRepo.findByUsernameAndRoleName(employeeEntity.get().getEmail(), role);
                    //AppUserEntity appUserEntityToBeSaved = new AppUserEntity();
                    RolePojo rolePojoFromDB = roleRepo.findByRoleName(role); //RoleMapper.entityToPojo(

                    if(appUserEntityFromDB != null){
//                        appUserEntityToBeSaved.setUsername(appUserFromDB.getUsername());
//                        appUserEntityToBeSaved.setPassword(appUserFromDB.getPassword());
//                        appUserEntityToBeSaved.setMatchingPassword(appUserFromDB.getMatchingPassword());

                        Set<RoleEntity> roleEntitySet = new HashSet<>();
                        appUserEntityFromDB.getRoles().add(RoleMapper.pojoToEntity(rolePojoFromDB));
                        for(RoleEntity roleEntities : appUserEntityFromDB.getRoles()){
                            if(!roleEntities.getRoleName().equalsIgnoreCase(role)){
                                roleEntitySet.add(roleEntities);
                            }
                        }
                        appUserEntityFromDB.setRoles(roleEntitySet);
//                        appUserEntityToBeSaved.setEmployeeEntity(employeeEntity.get());

                        deleteRolesEntityBasedOnRoleName(role, empId);
                        appUserRepo.save(appUserEntityFromDB);
                        return appUserEntityFromDB + "unassign role successfully.";
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
        return RoleMapper.pojoToEntity(roleRepo.findByRoleName(roleName));
    }
}
