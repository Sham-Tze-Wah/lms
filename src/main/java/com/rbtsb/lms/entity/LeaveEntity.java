package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.LeaveStatus;
import com.rbtsb.lms.constant.LeaveType;
import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.util.SqlDataType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="LeaveApplication")
@Entity
@Data
@Builder
public class LeaveEntity {

    //@GeneratedValue(generator = "uuid") //you need to make the dao throw error as it is generated and set into entity.
    //https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data (24 vote)
//    @GeneratedValue(generator = "emp_gen",strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name="emp_gen", sequenceName="emp_seq", allocationSize=1)
    @Id
    @Column(name="leave_id", unique = true)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonIgnore
    @NonNull
    private String leaveId = UUID.randomUUID().toString();

    @Enumerated(value=EnumType.STRING)
    @Column(name="leave_status", length= SqlDataType.VARCHAR64, nullable = false)
    private LeaveStatus leaveStatus;

    @Column(name = "leave_type", length = SqlDataType.VARCHAR64, nullable = false)
    @Enumerated(value=EnumType.STRING)
    private LeaveType leaveType;

    @Column(name="reason", length= SqlDataType.VARCHAR64, nullable = false)
    private String reason;

    @Column(name="description", length= SqlDataType.VARCHAR255)
    private String description;

//    @Column(name = "date_leave") //in format of dd/mm/yyyy
//    @Temporal(TemporalType.TIMESTAMP)
//    @NonNull
//    private Date dateLeave = new Date();

    @Column(name = "start_date_leave") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date startDateLeave = new Date();

    @Column(name = "end_date_leave")
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date endDateLeave = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id")
    private EmployeeEntity employeeEntity;
}
