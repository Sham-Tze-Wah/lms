package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.LeaveService;
import com.rbtsb.lms.service.mapper.AttachmentMapper;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveDTORepo leaveDTORepo;

    @Override
    public String insertLeave(LeaveDTO leaveDTO) {
        leaveDTORepo.saveAndFlush(LeaveMapper.DTOToEntity(leaveDTO));
        return "Insert successfully.";
    }

    @Override
    public List<LeaveDTO> getAllLeave() {
        List<LeaveEntity> leaveEntities = leaveDTORepo.findAll();
        List<LeaveDTO> leaveDTOList = new ArrayList<>();
        leaveEntities.forEach(leaveEntity -> {
            leaveDTOList.add(LeaveMapper.entityToDTO(leaveEntity));
        });

        if(!leaveDTOList.isEmpty()){
            return leaveDTOList;
        }
        else{
            return null;
        }
    }

    @Override
    public String updateLeaveStatus(int id, LeaveDTO leaveDTO) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);

        if(leave.isPresent()){
            if(!leaveDTO.getReason().equalsIgnoreCase("")){
                if(!leaveDTO.getLeaveStatus().equals(null)){
                    if(!leaveDTO.getEmployeePojo().equals(null)){
                        leave.get().setLeaveId(leaveDTO.getLeaveId());
                        leave.get().setLeaveStatus(leaveDTO.getLeaveStatus());
                        leave.get().setReason(leaveDTO.getReason());
                        leave.get().setDescription(leaveDTO.getDescription());
                        //leave.get().setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
                        leaveDTORepo.saveAndFlush(leave.get());
                        return "Updated successfully.";
                    }
                    else{
                        return "leave application must belongs to at least one employee.";
                    }
                }
                else{
                    return "leave status cannot be null";
                }
            }
            else{
                return "reason cannot be null";
            }
        }
        else{
            return "the updated leave is null";
        }

    }

    @Override
    public String deleteLeaveById(int id) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);

        if(leave.isPresent()){
            leaveDTORepo.deleteById(id);
            return "deleted successfully";
        }
        else{
            return "the id provided is not exist";
        }
    }
}
