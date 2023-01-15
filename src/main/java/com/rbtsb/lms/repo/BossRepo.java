package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.BossEntity;
import com.rbtsb.lms.entity.HREntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossRepo extends JpaRepository<BossEntity,String> {
}
