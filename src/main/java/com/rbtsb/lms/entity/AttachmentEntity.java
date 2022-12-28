package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.FileType;
import com.rbtsb.lms.util.SqlDataType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Table(name="Attachment")
@Entity
@Data
@Builder
public class AttachmentEntity {

    @JsonIgnore
    @NonNull
    @Column(name="file_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Id
    private String fileId = UUID.randomUUID().toString();

    @Column(name="file_name", nullable = false, unique=true, length = SqlDataType.VARCHAR30)
    private String fileName;

    @Column(name="file_type", nullable = false, length= SqlDataType.VARCHAR50)
    //@Enumerated(value=EnumType.STRING)
    private String fileType;

    @Column(name="directory", nullable = false, length= SqlDataType.VARCHAR255)
    private String directory;

    @Column(name="file_data", nullable=false, length = SqlDataType.VARCHAR2000)
    @Lob //for storing binary data
    private byte[] fileData;

    @ManyToOne()
    @JoinColumn(name="leave_id", referencedColumnName = "leave_id")
    private LeaveEntity leaveEntity;
}
