package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.AssignerEntity;
import com.rbtsb.lms.entity.HREntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HRRepo extends JpaRepository<HREntity,String> {
}
