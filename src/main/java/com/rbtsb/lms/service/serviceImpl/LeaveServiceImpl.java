package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.constant.LeaveType;
import com.rbtsb.lms.dto.LeaveDTO;
import com.rbtsb.lms.entity.*;
import com.rbtsb.lms.pojo.ApiErrorPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.pojo.LeavePojo;
import com.rbtsb.lms.repo.*;
import com.rbtsb.lms.service.LeaveService;
import com.rbtsb.lms.service.mapper.AttachmentMapper;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.mapper.LeaveMapper;
import com.rbtsb.lms.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private AssignerRepo assignerRepo;

    @Autowired
    private HRRepo hrRepo;

    @Autowired
    private BossRepo bossRepo;

    @Override
    public ApiErrorPojo insertLeave(LeaveDTO leaveDTO) {
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        try {

            leaveDTORepo.saveAndFlush(
                    leaveMapper.DTOToEntity("", leaveDTO)
            );
            apiErrorPojo.setResponseStatus("200");
            apiErrorPojo.setResponseMessage("Insert successfully.");
        } catch (ParseException paEx) {
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(paEx.toString());
        } catch (Exception ex) {
            apiErrorPojo.setResponseStatus("400");
            apiErrorPojo.setResponseMessage(ex.toString());
        }
        return apiErrorPojo;
    }

    @Deprecated
    @Override
    public ApiErrorPojo checkDuplicateReason(LeaveDTO leaveDTO) {
//        Optional<LeaveEntity> leaveEntity = leaveDTORepo.findByEmployeeNameAndStartDateLeaveAndEndDateLeave(
//          leaveDTO.getEmployeeName(),
//          leaveDTO.getStartDateLeave(),
//          leaveDTO.getEndDateLeave()
//        );
//        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
//        try{
//            if(leaveEntity.isPresent()){
//                //cannot save into db
//                apiErrorPojo.setResponseStatus("422");
//                apiErrorPojo.setResponseMessage("Unable to accept the reason due to duplicate reason. Please provide another reason");
//            }
//            else{
//                apiErrorPojo.setResponseStatus("200");
//                apiErrorPojo.setResponseMessage("OK");
//            }
//
//        }
//        catch(Exception ex){
//            apiErrorPojo.setResponseStatus("400");
//            apiErrorPojo.setResponseMessage(ex.toString());
//        }
//        return apiErrorPojo;
        return null;
    }

    @Override
    public List<LeaveDTO> getLeaveApplicationByEmpId(String empId) {
        List<LeaveDTO> leaveDTOList = new ArrayList<>();
        List<LeaveEntity> leaveEntityList = leaveDTORepo.findByEmpId(empId);
        if (leaveEntityList != null && !leaveEntityList.isEmpty()) {
            for (LeaveEntity leaveEntity : leaveEntityList) {
                LeaveDTO leaveDTO = leaveMapper.entityToDTO(leaveEntity);
                leaveDTOList.add(leaveDTO);
            }

            return leaveDTOList;
        } else {
            return null;
        }
    }

    @Override
    public String assignHR(String leaveId, String assignerId, String hrId) throws ParseException {
        try{
            Optional<LeaveEntity> leaveEntity = leaveDTORepo.findById(leaveId);
            if (leaveEntity.isPresent()) {
                if (leaveEntity.get().getLeaveStatus() != null &&
                        leaveEntity.get().getLeaveStatus().toString().equalsIgnoreCase("New")) {
                    Optional<AssignerEntity> assignerEntity = assignerRepo.findById(assignerId);
                    if (assignerEntity.isPresent()) {
                        Optional<HREntity> hrEntity = hrRepo.findById(hrId);
                        if (hrEntity.isPresent()) {
                            boolean leaveIsNotExpired = DateTimeUtil.compareDate(DateTimeUtil.DateToString(leaveEntity.get().getStartDateLeave()), DateTimeUtil.DateToString(leaveEntity.get().getEndDateLeave()));
                            if (leaveIsNotExpired) {
                                HREntity assignedHREntity = leaveEntity.get().getHrEntity();
                                if (assignedHREntity == null) {
                                    AssignerEntity assignedAssignerEntity = leaveEntity.get().getAssignerEntity();
                                    if(assignedAssignerEntity == null){
                                        leaveEntity.get().setAssignerEntity(assignerEntity.get());
                                    } //TODO check Assigner account whether it match with the existing acc in leave?
                                    leaveEntity.get().setHrEntity(hrEntity.get());
                                    leaveDTORepo.saveAndFlush(leaveEntity.get());
                                    return "Assign successfully.";
                                } else {
                                    throw new NullPointerException("The hr is assigned to someone already.");
                                }

                            } else {
                                leaveEntity.get().setLeaveStatus(LeaveStatus.Rejected);
                                leaveDTORepo.saveAndFlush(leaveEntity.get());
                                return "Rejected successfully due to expiration. Reapply if you think this is necessary.";
                            }
                        } else {
                            throw new NullPointerException("The hr id is not exist.");
                        }

                    } else {
                        throw new NullPointerException("The assigner id is not exist.");
                    }
                } else {
                    throw new NullPointerException("This leave application cannot be assigned as it is not new.");
                }
            } else {
                throw new NullPointerException("The leave id is not exist.");
            }
        }
        catch(Exception ex){
            return "Internal server error. Please contact system admin for help.";
        }

    }


    @Override
    public String validateLeave(String leaveId, String assignerId, String hrId, String rejectOrValidated) {
        try {
            Optional<LeaveEntity> leaveEntity = leaveDTORepo.findById(leaveId);
            if (leaveEntity.isPresent()) {
                if (leaveEntity.get().getLeaveStatus() != null &&
                        leaveEntity.get().getLeaveStatus().toString().equalsIgnoreCase("New")) {
                    Optional<AssignerEntity> assignerEntity = assignerRepo.findById(assignerId);
                    if (assignerEntity.isPresent()) {
                        Optional<HREntity> hrEntity = hrRepo.findById(hrId);
                        if (hrEntity.isPresent()) {
                            boolean leaveIsNotExpired = DateTimeUtil.compareDate(DateTimeUtil.DateToString(leaveEntity.get().getStartDateLeave()), DateTimeUtil.DateToString(leaveEntity.get().getEndDateLeave()));
                            if (leaveIsNotExpired) {
                                if(leaveEntity.get().getHrEntity() != null){
                                    if(hrEntity.get().equals(leaveEntity.get().getHrEntity())){
                                        if(rejectOrValidated.equalsIgnoreCase("Rejected") || rejectOrValidated.equalsIgnoreCase("Validated")){
                                            leaveEntity.get().setLeaveStatus(LeaveStatus.valueOf(rejectOrValidated));
                                            leaveDTORepo.saveAndFlush(leaveEntity.get());
                                            //TODO send email to leave applier that his form is being validated
                                            return "Validate successfully.";
                                        }
                                        else{
                                            throw new NullPointerException("The leave status is invalid.");
                                        }
                                    }
                                    else{
                                        throw new NullPointerException("This leave is not assigned to you.");
                                    }
                                }
                                else{
                                    throw new NullPointerException("This leave is not assigned to anyone yet. Please contact an assigner to assign this leave first.");
                                }

                            } else {
                                leaveEntity.get().setLeaveStatus(LeaveStatus.Rejected);
                                leaveDTORepo.saveAndFlush(leaveEntity.get());
                                return "Rejected successfully due to expiration. Reapply if you think this is necessary.";
                            }
                        } else {
                            throw new NullPointerException("The hr id is not exist.");
                        }
                    } else {
                        throw new NullPointerException("The assigner id is not exist.");
                    }
                } else {
                    throw new NullPointerException("This leave application cannot be assigned as it is not new.");
                }

            } else {
                throw new NullPointerException("leave id does not exist.");
            }
        } catch (Exception ex) {
            return "Internal server error. Please contact system admin for help.";
        }
    }

    @Override
    public List<LeaveDTO> getAssignedLeaveApplicationByHRId(String hrId) {
        List<LeaveEntity> leaveEntities = leaveDTORepo.findByHRId(hrId);
        List<LeaveDTO> leaveDTOList = new ArrayList<>();
        if(!leaveEntities.isEmpty()){
            for(LeaveEntity leaveEntity : leaveEntities){
                LeaveDTO leaveDTO = leaveMapper.entityToDTO(leaveEntity);
                leaveDTOList.add(leaveDTO);
            }
            return leaveDTOList;
        }
        else{
            throw new NullPointerException("The id does not exist.");
        }
    }

    @Override
    public List<LeaveDTO> getAllLeave() {
        List<LeaveEntity> leaveEntities = leaveDTORepo.findByPriority();
        List<LeaveDTO> leaveDTOList = new ArrayList<>();
        leaveEntities.forEach(leaveEntity -> {
            leaveDTOList.add(leaveMapper.entityToDTO(leaveEntity));
        });

        if (!leaveDTOList.isEmpty()) {
            return leaveDTOList;
        } else {
            return null;
        }
    }

    @Override
    public ApiErrorPojo updateLeaveApplication(String id, String leaveStatus, String reason, String description, String leaveType, String startDateLeave, String endDateLeave, String employeeId) {
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();
        try {
            if (id != null && !id.equalsIgnoreCase("")) {
                Optional<LeaveEntity> leaveFromId = leaveDTORepo.findById(id);

                if (leaveFromId.isPresent()) {

                    if (reason != null && !reason.equalsIgnoreCase("")) {

                        if (leaveStatus != null && !leaveStatus.equalsIgnoreCase("")) {

                            if (leaveType != null && !leaveType.equalsIgnoreCase("")) {

                                if (employeeId != null && !employeeId.equalsIgnoreCase("")) {
                                    Optional<EmployeeEntity> emp = employeeRepo.findById(employeeId);

                                    if (emp.isPresent()) {
                                        if (startDateLeave != null && !startDateLeave.equalsIgnoreCase("")) {
                                            if (endDateLeave != null && !endDateLeave.equalsIgnoreCase("")) {
                                                Optional<LeaveEntity> leaveEntityFromReqBody = leaveDTORepo.findById(id);
                                                if (leaveEntityFromReqBody.isPresent()) {
                                                    LeaveEntity leaveEntity = leaveEntityFromReqBody.get();
                                                    leaveFromId.get().setLeaveId(id);
                                                    leaveFromId.get().setLeaveStatus(LeaveStatus.valueOf(leaveStatus));
                                                    leaveFromId.get().setLeaveType(LeaveType.valueOf(leaveType));
                                                    leaveFromId.get().setReason(reason);
                                                    if (description == null || description.equalsIgnoreCase("")) {
                                                        leaveFromId.get().setDescription(leaveFromId.get().getDescription());
                                                    } else {
                                                        leaveFromId.get().setDescription(description);
                                                    }
                                                    leaveFromId.get().setStartDateLeave(DateTimeUtil.stringToDate(startDateLeave));
                                                    leaveFromId.get().setEndDateLeave(DateTimeUtil.stringToDate(endDateLeave));
                                                    //leave.get().setEmployeeEntity(EmployeeMapper.pojoToEntity(leaveDTO.getEmployeePojo()));
                                                    leaveDTORepo.saveAndFlush(leaveFromId.get());
                                                    apiErrorPojo.setResponseMessage("Updated successfully.");
                                                    apiErrorPojo.setResponseStatus("200");
                                                } else {
                                                    apiErrorPojo.setResponseMessage("The id is not exist.");
                                                    apiErrorPojo.setResponseStatus("422");
                                                }

                                            } else {
                                                apiErrorPojo.setResponseMessage("The end leave date must be provided.");
                                                apiErrorPojo.setResponseStatus("422");
                                            }

                                        } else {
                                            apiErrorPojo.setResponseMessage("The start leave date must be provided.");
                                            apiErrorPojo.setResponseStatus("422");
                                        }
                                    } else {
                                        apiErrorPojo.setResponseMessage("The given employee must belongs to the company.");
                                        apiErrorPojo.setResponseStatus("422");
                                    }

                                } else {
                                    apiErrorPojo.setResponseMessage("leave application must belongs to at least one employee.");
                                    apiErrorPojo.setResponseStatus("422");
                                }
                            } else {
                                apiErrorPojo.setResponseMessage("leave type cannot be null");
                                apiErrorPojo.setResponseStatus("422");
                            }
                        } else {
                            apiErrorPojo.setResponseMessage("leave status cannot be null");
                            apiErrorPojo.setResponseStatus("422");
                        }
                    } else {
                        apiErrorPojo.setResponseMessage("reason cannot be null");
                        apiErrorPojo.setResponseStatus("422");
                    }
                } else {
                    apiErrorPojo.setResponseMessage("Please apply the new leave first before update.");
                    apiErrorPojo.setResponseStatus("422");
                }
            } else {
                apiErrorPojo.setResponseMessage("The id is null.");
                apiErrorPojo.setResponseStatus("422");
            }
        } catch (ParseException paEx) {
            apiErrorPojo.setResponseMessage("date time conversion error. Please contact system admin.");
            apiErrorPojo.setResponseStatus("400");
        } catch (Exception ex) {
            apiErrorPojo.setResponseMessage("Internal server error occurs. Please contact system admin.");
            apiErrorPojo.setResponseStatus("400");
        }
        return apiErrorPojo;
    }

    @Override
    public String deleteLeaveById(String id) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);

        if (leave.isPresent()) {
            leaveDTORepo.deleteById(id);
            return "Deleted successfully";
        } else {
            return "the id provided is not exist";
        }
    }

    @Override
    public ApiErrorPojo approveLeaveStatus(String id, String bossId) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        if (leave.isPresent()) {
            if (leave.get().getLeaveStatus().toString().equalsIgnoreCase("Validated")) {
                Optional<BossEntity> bossEntity = bossRepo.findById(bossId);
                if(bossEntity.isPresent()){
                    leave.get().setBossEntity(bossEntity.get());
                    leave.get().setLeaveStatus(LeaveStatus.Approved);
                    leaveDTORepo.saveAndFlush(leave.get());
                    apiErrorPojo.setResponseStatus("200");
                    apiErrorPojo.setResponseMessage("Approved status updated successfully.");
                }
                else{
                    apiErrorPojo.setResponseStatus("422");
                    apiErrorPojo.setResponseMessage("the boss id does not exist.");
                }
            } else if(leave.get().getLeaveStatus().toString().equalsIgnoreCase("Approved")
                    || leave.get().getLeaveStatus().toString().equalsIgnoreCase("Rejected")){
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("the leave status has been approved or rejected.");
            }
            else{
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("the leave status haven't been validated yet.");
            }
        } else {
            apiErrorPojo.setResponseStatus("422");
            apiErrorPojo.setResponseMessage("the id provided is not exist");
        }
        return apiErrorPojo;
    }

    @Override
    public ApiErrorPojo rejectLeaveStatus(String id, String bossId) {
        Optional<LeaveEntity> leave = leaveDTORepo.findById(id);
        ApiErrorPojo apiErrorPojo = new ApiErrorPojo();

        if (leave.isPresent()) {
            if (leave.get().getLeaveStatus().toString().equalsIgnoreCase("Validated")) {
                Optional<BossEntity> bossEntity = bossRepo.findById(bossId);
                if(bossEntity.isPresent()){
                    leave.get().setBossEntity(bossEntity.get());
                    leave.get().setLeaveStatus(LeaveStatus.Rejected);
                    leaveDTORepo.saveAndFlush(leave.get());
                    apiErrorPojo.setResponseStatus("200");
                    apiErrorPojo.setResponseMessage("Rejected status updated successfully.");
                }
                else{
                    apiErrorPojo.setResponseStatus("422");
                    apiErrorPojo.setResponseMessage("the boss id does not exist.");
                }

            }
            else if(leave.get().getLeaveStatus().toString().equalsIgnoreCase("Approved")
                    || leave.get().getLeaveStatus().toString().equalsIgnoreCase("Rejected")){
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("the leave status has been approved or rejected.");
            }
            else{
                apiErrorPojo.setResponseStatus("422");
                apiErrorPojo.setResponseMessage("the leave status haven't been validated yet.");
            }

        } else {
            apiErrorPojo.setResponseStatus("422");
            apiErrorPojo.setResponseMessage("the id provided is not exist");
        }
        return apiErrorPojo;
    }
}
