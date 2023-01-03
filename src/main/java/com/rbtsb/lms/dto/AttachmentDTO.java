package com.rbtsb.lms.dto;

import com.rbtsb.lms.constant.FileType;
import com.rbtsb.lms.entity.AttachmentEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttachmentDTO {
    private String fileName;
    private String fileType;
    private String directory;
    private Date startDateLeave = new Date();
    private Date endDateLeave = new Date();
    private String leaveReason;
    private String employeeName;
}