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
        leaveDTO.setDateLeave(leaveEntity.getDateLeave());
        leaveDTO.setLeaveStatus(leaveEntity.getLeaveStatus());
        leaveDTO.setReason(leaveEntity.getReason());
        leaveDTO.setDescription(leaveEntity.getDescription());
        //leaveDTO.setEmployeePojo(EmployeeMapper.entityToPojo(leaveEntity.getEmployeeEntity()));

        return leaveDTO;
    }

    public LeaveEntity DTOToEntity(LeaveDTO leaveDTO){
        LeaveEntity leave = new LeaveEntity();
        Optional<Integer> id = leaveDTORepo.findByReasonAndEmployeeAndDate(leaveDTO.getReason(),
                leaveDTO.getEmployeeName(),
                leaveDTO.getDateLeave());
        if(id.isPresent()){
            leave.setLeaveId(id.get());
            leave.setLeaveStatus(leaveDTO.getLeaveStatus());
            leave.setReason(leaveDTO.getReason());
            leave.setDescription(leaveDTO.getDescription());
            //leave.setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
            return leave;
        }
        else{
            return null;
        }
    }

    public LeaveEntity DTOToEntityCreate(LeaveDTO leaveDTO){
        LeaveEntity leave = new LeaveEntity();
        try{
            Optional<Integer> id = leaveDTORepo.findByReasonAndEmployeeAndDate(leaveDTO.getReason(),
                    leaveDTO.getEmployeeName(),
                    leaveDTO.getDateLeave());
            if(!id.isPresent()){
                leave.setLeaveStatus(leaveDTO.getLeaveStatus());
                leave.setReason(leaveDTO.getReason());
                leave.setDescription(leaveDTO.getDescription());
                leave.setDateLeave(DateTimeUtil.yyyyMMddDate(leaveDTO.getDateLeave()));
                Optional<EmployeeEntity> emp = employeeRepo.getEmployeeByName(leaveDTO.getEmployeeName());
                if(emp.isPresent()){
                    leave.setEmployeeEntity(emp.get());
                    //leave.setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
                    return leave;
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
        catch(NoSuchElementException | ParseException ex){
            leave.setLeaveStatus(leaveDTO.getLeaveStatus());
            leave.setReason(leaveDTO.getReason());
            leave.setDescription(leaveDTO.getDescription());
            leave.setDateLeave(leaveDTO.getDateLeave());
            Optional<EmployeeEntity> emp = employeeRepo.getEmployeeByName(leaveDTO.getEmployeeName());
            if(emp.isPresent()){
                leave.setEmployeeEntity(emp.get());
                //leave.setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
                return leave;
            }
            else{
                return null;
            }
        }

    }
}
