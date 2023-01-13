package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.VerificationTokenEntity;
import com.rbtsb.lms.pojo.VerificationTokenPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepo extends
        JpaRepository<VerificationTokenEntity, Long> {

    @Query(nativeQuery = true)
    VerificationTokenEntity findByToken(String token);
}
