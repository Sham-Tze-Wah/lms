package com.rbtsb.lms.service;

import javax.mail.MessagingException;

public interface MailService {
    public Object sendSimpleEmail(String toEmail, String body, String subject);

    public Object sendSimpleEmailWithAttachment(String toEmail, String body,
                                                String subject, String[] attachment) throws MessagingException;
}
