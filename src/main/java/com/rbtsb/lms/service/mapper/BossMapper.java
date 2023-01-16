package com.rbtsb.lms.service.mapper;

import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.pojo.BossPojo;

public class BossMapper {

    public static BossPojo entityToPojo(BossEntity bossEntity){
        BossPojo bossPojo = new BossPojo();
        bossPojo.setId(bossEntity.getBossId());
        bossPojo.setEmployeePojo(EmployeeMapper.entityToPojo(bossEntity.getEmployeeEntity()));
        return bossPojo;
    }
}
