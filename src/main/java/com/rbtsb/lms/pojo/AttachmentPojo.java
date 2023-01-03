package com.rbtsb.lms.pojo;

import com.rbtsb.lms.entity.LeaveEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentPojo {
    private String fileId = UUID.randomUUID().toString();
    private String fileName;
    private String fileType;
    private String directory;
    private byte[] fileData;
    private LeavePojo leavePojo;
}
