package com.rbtsb.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    String fromEmail;
    String toEmail;
    String body;
    String subject;
    String[] attachments;
}
