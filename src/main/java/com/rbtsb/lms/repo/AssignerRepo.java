package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.AppUserEntity;
import com.rbtsb.lms.entity.AssignerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignerRepo extends JpaRepository<AssignerEntity,String> {

}
