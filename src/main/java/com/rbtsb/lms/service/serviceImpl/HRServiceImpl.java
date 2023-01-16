package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.HREntity;
import com.rbtsb.lms.pojo.BossPojo;
import com.rbtsb.lms.pojo.HRPojo;
import com.rbtsb.lms.repo.HRRepo;
import com.rbtsb.lms.service.HRService;
import com.rbtsb.lms.service.mapper.BossMapper;
import com.rbtsb.lms.service.mapper.HRMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HRServiceImpl implements HRService {

    @Autowired
    private HRRepo hrRepo;

    @Override
    public Optional<HRPojo> getHRByEmail(String fromEmail) {
        Optional<HREntity> hrEntity = hrRepo.findByEmail(fromEmail);
        if(hrEntity.isPresent()){
            HRPojo hrPojo = HRMapper.entityToPojo(hrEntity.get());
            if(hrPojo == null){
                return null;
            }
            return Optional.of(hrPojo);
        }
        else{
            return null;
        }
    }
}
