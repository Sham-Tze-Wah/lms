package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.LeaveEntity;

public class LeaveMapper {
    public static LeaveDTO entityToDTO(LeaveEntity leaveEntity){
        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setLeaveId(leaveEntity.getLeaveId());
        leaveDTO.setLeaveStatus(leaveEntity.getLeaveStatus());
        leaveDTO.setReason(leaveEntity.getReason());
        leaveDTO.setDescription(leaveEntity.getDescription());
        //leaveDTO.setEmployeePojo(EmployeeMapper.entityToPojo(leaveEntity.getEmployeeEntity()));

        return leaveDTO;
    }

    public static LeaveEntity DTOToEntity(LeaveDTO leaveDTO){
        LeaveEntity leave = new LeaveEntity();
        leave.setLeaveId(leaveDTO.getLeaveId());
        leave.setLeaveStatus(leaveDTO.getLeaveStatus());
        leave.setReason(leaveDTO.getReason());
        leave.setDescription(leaveDTO.getDescription());
        //leave.setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));

        return leave;
    }
}
