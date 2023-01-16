package com.rbtsb.lms.service;

import com.rbtsb.lms.pojo.BossPojo;

import java.util.Optional;

public interface BossService {
    Optional<BossPojo> getBossByEmail(String fromEmail);
}
