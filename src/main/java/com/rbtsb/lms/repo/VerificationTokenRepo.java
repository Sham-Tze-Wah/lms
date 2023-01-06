package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.VerificationTokenEntity;
import com.rbtsb.lms.pojo.VerificationTokenPojo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends
        JpaRepository<VerificationTokenEntity, Long> {
    VerificationTokenPojo findByToken(String token);
}
