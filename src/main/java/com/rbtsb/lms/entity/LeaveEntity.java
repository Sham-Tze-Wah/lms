package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.util.SqlDataType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="Leave")
@Entity
@Data
public class LeaveEntity {
    @Id
    @Column(name="emp_id", unique = true, nullable = false)
    //@GeneratedValue(generator = "uuid") //you need to make the dao throw error as it is generated and set into entity.
    //https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data (24 vote)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NonNull
    @JsonIgnore
    private int leaveId;

    @Enumerated(value=EnumType.STRING)
    @Column(name="leave_status", length= SqlDataType.VARCHAR64, nullable = false)
    private LeaveStatus leaveStatus;

    @Column(name="reason", length= SqlDataType.VARCHAR64, nullable = false)
    private String reason;

    @Column(name="description", length= SqlDataType.VARCHAR255)
    private String description;

    @ManyToOne()
    @JoinColumn(name="file_id", referencedColumnName = "file_id")
    private AttachmentDTO attachment;

    @ManyToOne()
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id")
    private EmployeePojo employeePojo;
}
