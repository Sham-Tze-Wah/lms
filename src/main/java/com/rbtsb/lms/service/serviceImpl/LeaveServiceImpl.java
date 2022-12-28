package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.LeaveStatus;
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

    @Autowired
    private LeaveMapper leaveMapper;

    @Override
    public String insertLeave(LeaveDTO leaveDTO) {
        leaveDTORepo.saveAndFlush(
                leaveMapper.DTOToEntityCreate(leaveDTO)
        );
        return "Insert successfully.";
    }

    @Override
    public List<LeaveDTO> getAllLeave() {
        List<LeaveEntity> leaveEntities = leaveDTORepo.findAll();
        List<LeaveDTO> leaveDTOList = new ArrayList<>();
        leaveEntities.forEach(leaveEntity -> {
            leaveDTOList.add(leaveMapper.entityToDTO(leaveEntity));
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
                    if(!leaveDTO.getEmployeeName().equals(null)){
                        if(!leaveDTO.getDateLeave().equals(null)){
                            Optional<Integer> leave_id = leaveDTORepo.findByReasonAndEmployeeAndDate(leaveDTO.getReason(), leaveDTO.getEmployeeName(), leaveDTO.getDateLeave());
                            if(leave_id.isPresent()){
                                leave.get().setLeaveId(leave_id.get());
                                leave.get().setLeaveStatus(leaveDTO.getLeaveStatus());
                                leave.get().setReason(leaveDTO.getReason());
                                leave.get().setDescription(leaveDTO.getDescription());
                                leave.get().setDateLeave(leaveDTO.getDateLeave());
                                //leave.get().setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
                                leaveDTORepo.saveAndFlush(leave.get());
                                return "Updated successfully.";
                            }
                            else{
                                return "Please apply the new leave first before update.";
                            }
                        }
                        else{
                            return "The leave date must be provided.";
                        }
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
            return "Deleted successfully";
        }
        else{
            return "the id provided is not exist";
        }
    }

    @Override
    public String approveLeaveStatus(int id) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);

        if(leave.isPresent()){
            leave.get().setLeaveStatus(LeaveStatus.Approved);
            leaveDTORepo.saveAndFlush(leave.get());
            return "Approved status updated successfully.";
        }
        else{
            return "the id provided is not exist";
        }
    }

    @Override
    public String rejectLeaveStatus(int id) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);

        if(leave.isPresent()){
            leave.get().setLeaveStatus(LeaveStatus.Rejected);
            leaveDTORepo.saveAndFlush(leave.get());
            return "Rejected status updated successfully.";
        }
        else{
            return "the id provided is not exist";
        }
    }
}
