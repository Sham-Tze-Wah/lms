package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveDTORepo leaveDTORepo;

    @Override
    public String insertLeave(LeaveDTO leaveDTO) {

        if(!leaveDTO.getReason().equalsIgnoreCase("")){
            if(!leaveDTO.getLeaveStatus().equals(null)){
                if(!leaveDTO.getEmployeePojo().equals(null)){
                    if(!leaveDTO.getAttachment().equals(null)){
                        leaveDTORepo.saveAndFlush(leaveDTO);
                        return "Insert successfully.";
                    }
                    else{
                        return "evidence with attachment must be provided";
                    }
                }
                else {
                    return "leave does not belongs to any employee.";
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

    @Override
    public List<LeaveDTO> getAllLeave() {
        return leaveDTORepo.findAll();
    }

    @Override
    public String updateLeaveStatus(int id, LeaveDTO leaveDTO) {
        Optional<LeaveDTO> leave = leaveDTORepo.findById(id);

        if(leave.isPresent()){
            if(!leaveDTO.getReason().equalsIgnoreCase("")){
                if(!leaveDTO.getLeaveStatus().equals(null)){
                    if(!leaveDTO.getEmployeePojo().equals(null)){
                        if(!leaveDTO.getAttachment().equals(null)){
                            leave.get().setLeaveStatus(leaveDTO.getLeaveStatus());
                            leave.get().setReason(leaveDTO.getReason());
                            leave.get().setDescription(leaveDTO.getDescription());
                            leave.get().setAttachment(leaveDTO.getAttachment());
                            leave.get().setEmployeePojo(leaveDTO.getEmployeePojo());
                            leaveDTORepo.saveAndFlush(leave.get());
                            return "Updated successfully.";
                        }
                        else{
                            return "evidence with attachment must be provided";
                        }
                    }
                    else {
                        return "leave does not belongs to any employee.";
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
        Optional<LeaveDTO> leave = leaveDTORepo.findById(id);

        if(leave.isPresent()){
            leaveDTORepo.deleteById(id);
            return "deleted successfully";
        }
        else{
            return "the id provided is not exist";
        }
    }
}
