package com.rbtsb.lms.service.serviceImpl;

//Sender
//Email: leongkxking@gmail.com
//Password: Leongkxking1234
//12 Feb 2000
//Spring Boot Mail: zzhvcepndbmpeyrp

//Receiver
//Username: hockanj@gmail.com
//Password: Hockanj1234
//12 Feb 2000
//Mail in-app password: yzasoiaazuahchqm
//Spring Boot Mail (preferences): tecqwgnwxrgnizka


import com.rbtsb.lms.service.MailService;
import com.rbtsb.lms.service.ValidatorService;
import com.sun.xml.internal.ws.api.message.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ValidatorService validatorService;

        public Object sendSimpleEmail(String toEmail, String body, String subject){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("leongkxking@gmail.com");
        msg.setTo(toEmail);
        msg.setText(body);
        msg.setSubject(subject);

        try{
            if(!msg.equals(null)){
                mailSender.send(msg);
                System.out.println("Mail Send..");
                return "Mail send successfully";
            }
            else{
                return "The mail is empty. Please provide some mail details.";
            }
        }
        catch(Exception ex){
            return ex.toString();
        }

    }

    public Object sendSimpleEmailWithAttachment(String toEmail, String body,
                                                String subject, String[] attachments) throws MessagingException {

        if(!mailSender.equals(null)){
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("leongkxking@gmail.com");

            if(!validatorService.validateEmail(toEmail).isEmpty()){


                if(!body.equalsIgnoreCase("")){


                    if(!subject.equalsIgnoreCase("")){


                        if(attachments.length > 0){


                            if(!mailSender.equals(null)){
                                mimeMessageHelper.setTo(toEmail);
                                mimeMessageHelper.setText(body);
                                mimeMessageHelper.setSubject(subject);

                                List<FileSystemResource> fileSystemResourceList = new ArrayList<>();
                                for(String attachment : attachments){
                                    FileSystemResource fileSystem =
                                            new FileSystemResource(new File(attachment));

                                    mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                                            fileSystem);
                                }

                                mailSender.send(mimeMessage);
                                System.out.println("Mail send....");
                                return "Mail send successfully.";
                            }
                            else if(mailSender.equals(null)){
                                return "mail send unsuccessfully due to empty mailSender.";
                            }
                            else{
                                return "mail send unsuccessfully due to other internal issue.";
                            }

                        }
                        else{
                            return "Mail send unsuccessfully due to invalid attachment.";
                        }
                    }
                    else{
                        return "Mail send unsuccessfully due to invalid subject.";
                    }
                }
                else{
                    return "Mail send unsuccessfully due to invalid body.";
                }
            }
            else{
                return "Mail send unsuccessfully due to invalid receiver email.";
            }

        }
        else{
            return "Mail send unsuccessfully.";
        }

    }

}

