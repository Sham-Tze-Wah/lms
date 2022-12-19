package com.rbtsb.lms.dto;

import com.rbtsb.lms.pojo.EmployeePojo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttachmentDTO {
    private String fileId;
    private String fileName;
    private String directory;
    private List<File> files;
    private EmployeePojo employeePojo;
}
