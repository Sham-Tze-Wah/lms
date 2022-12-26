package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.repo.LeaveDTORepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class LeaveMapper {

    @Autowired
    private static LeaveDTORepo leaveDTORepo;

    public static LeaveDTO entityToDTO(LeaveEntity leaveEntity){
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setDateLeave(leaveEntity.getDateLeave());
        leaveDTO.setLeaveStatus(leaveEntity.getLeaveStatus());
        leaveDTO.setReason(leaveEntity.getReason());
        leaveDTO.setDescription(leaveEntity.getDescription());
        //leaveDTO.setEmployeePojo(EmployeeMapper.entityToPojo(leaveEntity.getEmployeeEntity()));

        return leaveDTO;
    }

    public static LeaveEntity DTOToEntity(LeaveDTO leaveDTO){
        LeaveEntity leave = new LeaveEntity();
        Optional<Integer> id = leaveDTORepo.findByReasonAndEmployeeAndDate(leaveDTO.getReason(),
                leaveDTO.getEmployeeName(),
                leaveDTO.getDateLeave());
        if(!id.get().equals(null)){
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
}
