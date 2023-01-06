package com.rbtsb.lms.repo;


import com.rbtsb.lms.entity.PasswordResetTokenEntity;
import com.rbtsb.lms.entity.VerificationTokenEntity;
import com.rbtsb.lms.pojo.PasswordResetTokenPojo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo extends
        JpaRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenPojo findByToken(String token);
}
