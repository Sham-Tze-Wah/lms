package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.util.SqlDataType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="LeaveApplication")
@Entity
@Data
@Builder
public class LeaveEntity {
    @Id
    @Column(name="leave_id", unique = true)
    //@GeneratedValue(generator = "uuid") //you need to make the dao throw error as it is generated and set into entity.
    //https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data (24 vote)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    @NonNull
    private int leaveId;

    @Enumerated(value=EnumType.STRING)
    @Column(name="leave_status", length= SqlDataType.VARCHAR64, nullable = false)
    private LeaveStatus leaveStatus;

    @Column(name="reason", length= SqlDataType.VARCHAR64, nullable = false)
    private String reason;

    @Column(name="description", length= SqlDataType.VARCHAR255)
    private String description;

    @Column(name = "date_leave") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date dateLeave = new Date();

//    @Column(name = "date_leave_start") //in format of dd/mm/yyyy
//    @Temporal(TemporalType.TIMESTAMP)
//    @NonNull
//    private Date dateLeaveStart = new Date();
//
//    @Column(name = "date_leave_end")
//    @Temporal(TemporalType.TIMESTAMP)
//    @NonNull
//    private Date dateLeaveEnd = new Date();

    @ManyToOne()
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id")
    private EmployeeEntity employeeEntity;
}
