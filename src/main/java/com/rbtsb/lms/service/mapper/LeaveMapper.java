package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LeaveMapper {

    @Autowired
    private LeaveDTORepo leaveDTORepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    public LeaveDTO entityToDTO(LeaveEntity leaveEntity){
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setStartDateLeave(leaveEntity.getStartDateLeave());
        leaveDTO.setEndDateLeave(leaveEntity.getEndDateLeave());
        leaveDTO.setLeaveStatus(leaveEntity.getLeaveStatus());
        leaveDTO.setLeaveType(leaveEntity.getLeaveType());
        leaveDTO.setReason(leaveEntity.getReason());
        leaveDTO.setDescription(leaveEntity.getDescription());
        leaveDTO.setEmployeeId(leaveEntity.getEmployeeEntity().getEmpId());
        //leaveDTO.setEmployeePojo(EmployeeMapper.entityToPojo(leaveEntity.getEmployeeEntity()));

        return leaveDTO;
    }

    public LeaveEntity DTOToEntity(String id, LeaveDTO leaveDTO) throws ParseException {
        LeaveEntity leave = new LeaveEntity();
//        Optional<LeaveEntity> leaveFromDB = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
//                leaveDTO.getEmployeeName(),
//                leaveDTO.getStartDateLeave(),
//                leaveDTO.getEndDateLeave()
//        );
        if(id!=null && !id.equalsIgnoreCase("")){
            leave.setLeaveId(id);
        }
            leave.setLeaveStatus(leaveDTO.getLeaveStatus());
            leave.setLeaveType(leaveDTO.getLeaveType());
            leave.setStartDateLeave(DateTimeUtil.yyyyMMddDate(leaveDTO.getStartDateLeave()));
            leave.setEndDateLeave(DateTimeUtil.yyyyMMddDate(leaveDTO.getEndDateLeave()));
            leave.setReason(leaveDTO.getReason());

            if(leaveDTO.getDescription() != null && !leaveDTO.getDescription().equalsIgnoreCase("")){
                leave.setDescription(leaveDTO.getDescription());
            }

            Optional<EmployeeEntity> emp = employeeRepo.findById(leaveDTO.getEmployeeId());
            leave.setEmployeeEntity(emp.get());
            return leave;

//        else{
//            return null;
//        }
    }

//    @Deprecated
//    public LeaveEntity DTOToEntityCreate(LeaveDTO leaveDTO){
//        LeaveEntity leave = new LeaveEntity();
//        try{
//            Optional<LeaveEntity> leaveEntity = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
//                    leaveDTO.getEmployeeName(),
//                    leaveDTO.getStartDateLeave(),
//                    leaveDTO.getEndDateLeave()
//            );
//            if(!leaveEntity.isPresent()){
//                leave.setLeaveStatus(leaveDTO.getLeaveStatus());
//                leave.setReason(leaveDTO.getReason());
//                leave.setDescription(leaveDTO.getDescription());
//                leave.setLeaveType(leaveDTO.getLeaveType());
//                leave.setEndDateLeave(DateTimeUtil.yyyyMMddDate(leaveDTO.getEndDateLeave()));
//                leave.setStartDateLeave(DateTimeUtil.yyyyMMddDate(leaveDTO.getStartDateLeave()));
//                Optional<EmployeeEntity> emp = employeeRepo.findByName(leaveDTO.getEmployeeName());
//                leave.setEmployeeEntity(emp.get());
//
//                if(emp.isPresent()){
//                    leave.setEmployeeEntity(emp.get());
//                    //leave.setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
//                    return leave;
//                }
//                else{
//                    return null;
//                }
//            }
//            else{
//                return null;
//            }
//        }
//        catch(NoSuchElementException | ParseException ex){
//            leave.setLeaveStatus(leaveDTO.getLeaveStatus());
//            leave.setReason(leaveDTO.getReason());
//            leave.setDescription(leaveDTO.getDescription());
//            leave.setStartDateLeave(leaveDTO.getStartDateLeave());
//            leave.setEndDateLeave(leaveDTO.getEndDateLeave());
//            Optional<EmployeeEntity> emp = employeeRepo.findByName(leaveDTO.getEmployeeName());
//            if(emp.isPresent()){
//                leave.setEmployeeEntity(emp.get());
//                //leave.setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
//                return leave;
//            }
//            else{
//                return null;
//            }
//        }
//
//    }
}
