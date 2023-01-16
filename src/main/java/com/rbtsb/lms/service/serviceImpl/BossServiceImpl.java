package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.BossPojo;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.repo.BossRepo;
import com.rbtsb.lms.service.BossService;
import com.rbtsb.lms.service.mapper.BossMapper;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BossServiceImpl implements BossService {

    @Autowired
    private BossRepo bossRepo;

    @Override
    public Optional<BossPojo> getBossByEmail(String fromEmail) {
        Optional<BossEntity> bossEntity = bossRepo.findByEmail(fromEmail);
        if(bossEntity.isPresent()){
            BossPojo bossPojo = BossMapper.entityToPojo(bossEntity.get());
            if(bossPojo == null){
                return null;
            }
            return Optional.of(bossPojo);
        }
        else{
            return null;
        }
    }
}
