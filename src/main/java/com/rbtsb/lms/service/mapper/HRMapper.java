package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.HREntity;
import com.rbtsb.lms.pojo.BossPojo;
import com.rbtsb.lms.pojo.HRPojo;

public class HRMapper {
    public static HRPojo entityToPojo(HREntity hrEntity){
        HRPojo hrPojo = new HRPojo();
        hrPojo.setId(hrEntity.getHrId());
        hrPojo.setEmployeePojo(EmployeeMapper.entityToPojo(hrEntity.getEmployeeEntity()));
        return hrPojo;
    }
}
