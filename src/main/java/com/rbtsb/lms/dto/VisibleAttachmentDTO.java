package com.rbtsb.lms.dto;

import com.rbtsb.lms.pojo.LeavePojo;
import lombok.Data;

import java.util.UUID;

@Data
public class VisibleAttachmentDTO {
    private String fileId = UUID.randomUUID().toString();
    private String fileName;
    private String fileType;
    private String directory;
    private Object fileData;
    private LeavePojo leavePojo;
}
