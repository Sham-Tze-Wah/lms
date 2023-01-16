package com.rbtsb.lms.service;

import com.rbtsb.lms.pojo.HRPojo;

import java.util.Optional;

public interface HRService {
    Optional<HRPojo> getHRByEmail(String fromEmail);
}
