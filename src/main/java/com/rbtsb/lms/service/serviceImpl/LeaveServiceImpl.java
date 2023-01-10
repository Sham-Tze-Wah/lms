package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.entity.LeaveEntity;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.LeavePojo;
import com.rbtsb.lms.repo.EmployeeRepo;
import com.rbtsb.lms.repo.LeaveDTORepo;
import com.rbtsb.lms.service.LeaveService;
import com.rbtsb.lms.service.mapper.AttachmentMapper;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public ApiErrorPojo insertLeave(LeaveDTO leaveDTO) {
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        try{

            leaveDTORepo.saveAndFlush(
                    leaveMapper.DTOToEntity("",leaveDTO)
            );
            apiErrorPojo.setResponseStatus("200");
            apiErrorPojo.setResponseMessage("Insert successfully.");
        }
        catch(ParseException paEx){
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(paEx.toString());
        }
        catch(Exception ex){
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(ex.toString());
        }
        return apiErrorPojo;
    }

    @Deprecated
    @Override
    public ApiErrorPojo checkDuplicateReason(LeaveDTO leaveDTO){
        Optional<LeaveEntity> leaveEntity = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
          leaveDTO.getEmployeeName(),
          leaveDTO.getStartDateLeave(),
          leaveDTO.getEndDateLeave()
        );
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
        try{
            if(leaveEntity.isPresent()){
                //cannot save into db
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("Unable to accept the reason due to duplicate reason. Please provide another reason");
            }
            else{
                apiErrorPojo.setResponseStatus("200");
                apiErrorPojo.setResponseMessage("OK");
            }

        }
        catch(Exception ex){
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(ex.toString());
        }
        return apiErrorPojo;
    }

    @Override
    public List<LeaveDTO> getLeaveApplicationByEmpId(String empId) {
        List<LeaveDTO> leaveDTOList = new ArrayList<>();
        List<LeaveEntity> leaveEntityList = leaveDTORepo.findByEmpId(empId);
        if(leaveEntityList != null && !leaveEntityList.isEmpty()){
            for(LeaveEntity leaveEntity : leaveEntityList){
                LeaveDTO leaveDTO = leaveMapper.entityToDTO(leaveEntity);
                leaveDTOList.add(leaveDTO);
            }

            return leaveDTOList;
        }
        else{
            return null;
        }
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
    public ApiErrorPojo updateLeaveApplication(String id, LeaveDTO leaveDTO) {
        Optional<LeaveEntity> leaveFromId = leaveDTORepo.findById(id);
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
        if(leaveFromId.isPresent()){
            if(!leaveDTO.getReason().equalsIgnoreCase("")){
                if(!leaveDTO.getLeaveStatus().equals(null)){
                    if(leaveDTO.getLeaveType() != null && !leaveDTO.getLeaveType().toString().equalsIgnoreCase("")){
                        if(leaveDTO.getEmployeeName() != null && !leaveDTO.getEmployeeName().equalsIgnoreCase("")){
                            Optional<EmployeeEntity> emp = employeeRepo.findByName(leaveDTO.getEmployeeName());

                            if(emp.isPresent()){
                                if(leaveDTO.getStartDateLeave() != null && !leaveDTO.getStartDateLeave().equals(null)){
                                    Optional<LeaveEntity> leaveEntityFromReqBody = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
                                            leaveDTO.getEmployeeName(),
                                            leaveDTO.getStartDateLeave(),
                                            leaveDTO.getEndDateLeave()
                                    );
                                    if(leaveFromId.isPresent()){
                                        LeaveEntity leaveEntity = leaveEntityFromReqBody.get();
                                        leaveFromId.get().setLeaveId(leaveEntity.getLeaveId());
                                        leaveFromId.get().setLeaveStatus(leaveDTO.getLeaveStatus());
                                        leaveFromId.get().setLeaveType(leaveDTO.getLeaveType());
                                        leaveFromId.get().setReason(leaveDTO.getReason());
                                        leaveFromId.get().setDescription(leaveDTO.getDescription());
                                        leaveFromId.get().setStartDateLeave(leaveDTO.getStartDateLeave());
                                        leaveFromId.get().setEndDateLeave(leaveDTO.getEndDateLeave());
                                        //leave.get().setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
                                        leaveDTORepo.saveAndFlush(leaveFromId.get());
                                        apiErrorPojo.setResponseMessage("Updated successfully.");
                                        apiErrorPojo.setResponseStatus("200");
                                    }
                                    else{
                                        apiErrorPojo.setResponseMessage("Please apply the new leave first before update.");
                                        apiErrorPojo.setResponseStatus("422");
                                    }
                                }
                                else{
                                    apiErrorPojo.setResponseMessage("The leave date must be provided.");
                                    apiErrorPojo.setResponseStatus("422");
                                }
                            }
                            else{
                                apiErrorPojo.setResponseMessage("The given employee must belongs to the company.");
                                apiErrorPojo.setResponseStatus("422");
                            }

                        }
                        else{
                            apiErrorPojo.setResponseMessage("leave application must belongs to at least one employee.");
                            apiErrorPojo.setResponseStatus("422");
                        }
                    }
                    else{
                        apiErrorPojo.setResponseMessage("leave type cannot be null");
                        apiErrorPojo.setResponseStatus("422");
                    }
                }
                else{
                    apiErrorPojo.setResponseMessage("leave status cannot be null");
                    apiErrorPojo.setResponseStatus("422");
                }
            }
            else{
                apiErrorPojo.setResponseMessage("reason cannot be null");
                apiErrorPojo.setResponseStatus("422");
            }
        }
        else{
            apiErrorPojo.setResponseMessage("the leave id is null");
            apiErrorPojo.setResponseStatus("422");
        }
        return apiErrorPojo;
    }

    @Override
    public String deleteLeaveById(String id) {
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
    public ApiErrorPojo approveLeaveStatus(String id) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        if(leave.isPresent()){
            if(!leave.get().getLeaveStatus().toString().equalsIgnoreCase("Approved")
            && !leave.get().getLeaveStatus().toString().equalsIgnoreCase("Rejected")){
                leave.get().setLeaveStatus(LeaveStatus.Approved);
                leaveDTORepo.saveAndFlush(leave.get());
                apiErrorPojo.setResponseStatus("200");
                apiErrorPojo.setResponseMessage("Approved status updated successfully.");
            }
            else{
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("the leave status has been approved or rejected.");
            }
        }
        else{
            apiErrorPojo.setResponseStatus("422");
            apiErrorPojo.setResponseMessage("the id provided is not exist");
        }
        return apiErrorPojo;
    }

    @Override
    public ApiErrorPojo rejectLeaveStatus(String id) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        if(leave.isPresent()){
            if(!leave.get().getLeaveStatus().toString().equalsIgnoreCase("Approved")
                    && !leave.get().getLeaveStatus().toString().equalsIgnoreCase("Rejected")){
                leave.get().setLeaveStatus(LeaveStatus.Rejected);
                leaveDTORepo.saveAndFlush(leave.get());
                apiErrorPojo.setResponseStatus("200");
                apiErrorPojo.setResponseMessage("Rejected status updated successfully.");
            }
            else{
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("the leave status has been approved or rejected.");
            }

        }
        else{
            apiErrorPojo.setResponseStatus("422");
            apiErrorPojo.setResponseMessage("the id provided is not exist");
        }
        return apiErrorPojo;
    }
}
