package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Table(name="Attachment")
@Entity
@Data
public class AttachmentEntity {

    @JsonIgnore
    @NonNull
    @Column(name="file_id", unique = true, nullable = false)
    @Id
    private String fileId;

    @ManyToOne()
    @JoinColumn(name="employee_id", referencedColumnName = "employee_id")
    private EmployeeEntity employeeEntity;
}
